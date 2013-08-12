/**
 * CommonDeviceDetector 13.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import zfs.java.helper.DeviceDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;
import zfs.java.models.Pair;
import zfs.java.models.Partition;

public class CommonDeviceDetector implements DeviceDetector {

    private static final long MB_SIZE = 1024L;
    private static final long GB_SIZE = 1024 * 1024L;
    private static final long TB_SIZE = 1024 * 1024 * 1024L;
    private static final long PB_SIZE = 1024 * 1024 * 1024 * 1024L;
    private static final int CYL_SIZE_OFFSET = 4;
    private static final int ALT_SIZE_OFFSET = 4;
    private static final int HD_SIZE_OFFSET = 3;
    private static final int SEC_SIZE_OFFSET = 4;
    protected String[] parts;
    protected Map<String, Device> devices;
    protected Pair infoPair;
    protected Host host;
    private boolean collectDevices = false;
    private boolean newDevice = false;
    private String currentDeviceName = null;
    private static final Map<String, Long> SIZE_MAP = new HashMap<String, Long>();
    private static final Logger LOG = Logger.getLogger(CommonDeviceDetector.class.getName());

    static {
        SIZE_MAP.put("MB", MB_SIZE);
        SIZE_MAP.put("GB", GB_SIZE);
        SIZE_MAP.put("TB", TB_SIZE);
        SIZE_MAP.put("PB", PB_SIZE);
    }

    public CommonDeviceDetector(Host host) {
        this.devices = new TreeMap<String, Device>();
        this.host = host;
    }

    @Override
    public Map<String, Device> getDevices() {
        return devices;
    }

    @Override
    public void parse(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(CommonKeys.DEVICE_LISTING_START_MARKER)) {
                    collectDevices = true;
                } else if (line.startsWith(CommonKeys.DEVICE_LISTING_END_MARKER)) {
                    return;
                } else if (collectDevices && newDevice) {
                    handleBus(line);
                } else if (collectDevices) {
                    handleDevice(line);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
    }

    protected String[] cleanSplit(final String line, final String sep) {
        List<String> list = new ArrayList<String>();
        for (String part : line.split(sep)) {
            if (!part.trim().isEmpty()) {
                list.add(part);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    private void handleBus(final String line) {
        Device device = devices.get(currentDeviceName);
        if (device != null) {
            device.bus = line.trim();
            devices.put(currentDeviceName, device);
        }
        newDevice = false;
    }

    private void handleDevice(final String line) {
        int start = line.indexOf('.');
        int end = line.indexOf('<');
        if (end > start && start > 0) {
            Device device;
            currentDeviceName = line.substring(start + 1, end).trim();
            // we need to find a better conversion.
            if (line.indexOf(CommonKeys.IDE_HDD) > -1) {
                device = new Device(currentDeviceName, Device.IDE_HDD);
            } else {
                device = new Device(currentDeviceName, Device.SCSI_HDD);
            }
            device.size = getSize(line);
            device.description = getDescription(line);
            device.host = host;
            device.partitions.add(new Partition(currentDeviceName));
            devices.put(currentDeviceName, device);
            newDevice = true;
        }
    }

    private static long getSize(final String line) {
        Long size = 0L;
        int startIdx, endIdx;
        int cylIdx = line.indexOf("cyl ");
        int altIdx = line.indexOf("alt ");
        int hdIdx = line.indexOf("hd ");
        int secIdx = line.indexOf("sec ");
        try {
            if (cylIdx != -1 && altIdx != -1 && hdIdx != -1 && secIdx != -1) {
                //c6t0d0 <VBOX-HARDDISK-1.0 cyl 4094 alt 2 hd 128 sec 32>
                Long p1, p2, p3, p4;
                endIdx = line.indexOf('>');
                p1 = Long.parseLong(line.substring(cylIdx + CYL_SIZE_OFFSET, altIdx).trim());
                p2 = Long.parseLong(line.substring(altIdx + ALT_SIZE_OFFSET, hdIdx).trim());
                p3 = Long.parseLong(line.substring(hdIdx + HD_SIZE_OFFSET, secIdx).trim());
                p4 = Long.parseLong(line.substring(secIdx + SEC_SIZE_OFFSET, endIdx).trim());
                size = (p1 + p2) * p3 * p4 / 2;
            } else {
                //c5t7d0 <VBOX-HARDDISK-1.0-1.00GB>
                String sizeString;
                double rawSize;
                for (String sizeLabel : SIZE_MAP.keySet()) {
                    if (line.endsWith(sizeLabel + '>')) {
                        startIdx = line.lastIndexOf('-');
                        endIdx = line.indexOf(sizeLabel + ">");
                        sizeString = line.substring(startIdx + 1, endIdx);
                        rawSize = Double.parseDouble(sizeString) * SIZE_MAP.get(sizeLabel);
                        size = Math.round(rawSize);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
        return size;
    }

    private static String getDescription(final String line) {
        int startIndex, endIndex;
        startIndex = line.indexOf('<');
        endIndex = line.indexOf('>');
        if (startIndex != -1 && endIndex != -1) {
            return line.substring(startIndex + 1, endIndex).trim();
        }
        return "";
    }
}
