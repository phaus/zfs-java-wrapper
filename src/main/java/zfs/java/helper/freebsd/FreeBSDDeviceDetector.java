/**
 * FreeBSDDeviceDetector 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper.freebsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.common.CommonDeviceDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;
import zfs.java.models.Pair;
import zfs.java.models.Partition;

public class FreeBSDDeviceDetector extends CommonDeviceDetector implements DeviceDetector {

    /*
     thx to http://www.cyberciti.biz/faq/freebsd-hard-disk-information
     a] IDE Hard disk names starts with ad - /dev/ad0 first IDE hard disk, /dev/ad1 second hard disk and so on
     b] SCSI Hard disk names starts with da - /dev/da*
     c] IDE CDROM/RW/DVD names starts with acd - /dev/acd*
     d] SCSI CDROM/RW/DVD names starts with cd - /dev/cd*
     */
    private final static String IDE_HDD = "ad";
    private final static String SCSI_HDD = "da";
    private final static String IDE_CDROM = "acd";
    private final static String SCSI_CDROM = "cd";
    private final static int DEVICE_TRANSFER = 0;
    private final static int DEVICE_BUS = 1;
    private final static int DEVICE_SIZE = 2;
    private final static int DEVICE_DESCRIPTION = 3;
    private final static String GPTID_HEADER = "Name  Status  Components";
    private static final String DEVICE_BUS_MARKER = "at";
    private static final String DEVICE_DESCRIPTION_MARKER = "<";
    private static final String DEVICE_SIZE_MARKER = "byte sectors:";
    private static final String DEVICE_TRANSFER_MARKER = "MB/s ";
    private static final String DEVICE_SIZE_DEVIDER = "MB ";
    private final static Logger LOG = Logger.getLogger(FreeBSDDeviceDetector.class.getName());
    private boolean GPTID_MODE = false;

    public FreeBSDDeviceDetector(Host host) {
        super(host);
    }

    @Override
    public void parse(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(IDE_HDD)) {
                    handleIDEHDD(line);
                } else if (line.startsWith(SCSI_HDD)) {
                    handleSCSIHDD(line);
                    //} else if (line.startsWith(IDE_CDROM)) {
                    // NOP
                    //} else if (line.startsWith(SCSI_CDROM)) {
                    // NOP
                } else if (line.startsWith(GPTID_HEADER)) {
                    GPTID_MODE = true;
                } else if (GPTID_MODE) {
                    handleGptId(line);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
    }

    private void handleIDEHDD(String line) {
        setParts(line);
        String name = parts[0].trim();
        //Logger.debug("adding IDE HDD " + name);
        if (devices.containsKey(name)) {
            addToDevice(devices.get(name), line);
        } else {
            createDevice(name, line, Device.IDE_HDD);
        }
    }

    private void handleSCSIHDD(String line) {
        setParts(line);
        String name = parts[0].trim();
        //Logger.debug("adding SCSI HDD " + name);
        if (devices.containsKey(name)) {
            addToDevice(devices.get(name), line);
        } else {
            createDevice(name, line, Device.SCSI_HDD);
        }
    }

    private void addToDevice(Device device, String line) {
        if (device != null) {
            infoPair = getInfo(device.name, line);
            device = addInfoToDevice(device, infoPair);
            devices.put(device.name, device);
        }
    }

    private void createDevice(String deviceName, String line, int type) {
        GPTID_MODE = false;
        //Logger.debug("creating device " + deviceName);
        Device device = new Device(deviceName, type);
        device.host = host;
        infoPair = getInfo(deviceName, line);
        if (infoPair != null) {
            device = addInfoToDevice(device, infoPair);
        }
        devices.put(deviceName, device);
    }

    private Device addInfoToDevice(Device device, Pair<Integer, String> pair) {
        if (pair != null) {
            //Logger.debug("addInfo: " + pair.getKey() + " = " + pair.getValue() + "\n");
            switch (pair.getKey().intValue()) {
                case DEVICE_TRANSFER:
                    device.transfer = pair.getValue();
                    return device;
                case DEVICE_DESCRIPTION:
                    device.description = pair.getValue();
                    return device;
                case DEVICE_SIZE:
                    long size = 0L;
                    if (pair.getValue() != null) {
                        size = Long.parseLong(pair.getValue());
                    }
                    device.size = size;
                    return device;
                case DEVICE_BUS:
                    device.bus = pair.getValue();
                    return device;
                default:
                /* NOP */
            }
        }
        return device;
    }

    private Pair<Integer, String> getInfo(String deviceName, String line) {
        String data = line.replace(deviceName + ":", "");
        data = data.replace(deviceName, "").trim();
        if (data.indexOf(DEVICE_TRANSFER_MARKER) != -1) {
            parts = data.split(DEVICE_TRANSFER_MARKER);
            return new Pair(DEVICE_TRANSFER, parts[0]);
        }
        if (data.indexOf(DEVICE_SIZE_MARKER) != -1) {
            parts = data.split(DEVICE_SIZE_DEVIDER);
            return new Pair(DEVICE_SIZE, parts[0]);
        }
        if (data.contains(DEVICE_DESCRIPTION_MARKER)) {
            return new Pair(DEVICE_DESCRIPTION, data);
        }
        if (data.startsWith(DEVICE_BUS_MARKER)) {
            return new Pair(DEVICE_BUS, data);
        }
        return null;
    }

    private void setParts(String line) {
        parts = line.split(":");
        if (parts.length < 2) {
            parts = line.split(" ");
        }
    }

    private void handleGptId(String line) {
        Device device;
        parts = cleanSplit(line, " ");
        if (parts.length == 3) {
            for (String key : devices.keySet()) {
                if (parts[2].startsWith(key)) {
                    device = devices.get(key);
                    device.addPartition(new Partition(parts[2], parts[0]));
                }
            }
        }
    }
}
