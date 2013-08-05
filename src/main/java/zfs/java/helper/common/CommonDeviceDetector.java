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
        SIZE_MAP.put("MB", 1024L);
        SIZE_MAP.put("GB", 1024 * 1024L);
        SIZE_MAP.put("TB", 1024 * 1024 * 1024L);
        SIZE_MAP.put("PB", 1024 * 1024 * 1024 * 1024L);
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
                p1 = Long.parseLong(line.substring(cylIdx + 4, altIdx).trim());
                p2 = Long.parseLong(line.substring(altIdx + 4, hdIdx).trim());
                p3 = Long.parseLong(line.substring(hdIdx + 3, secIdx).trim());
                p4 = Long.parseLong(line.substring(secIdx + 4, endIdx).trim());
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
