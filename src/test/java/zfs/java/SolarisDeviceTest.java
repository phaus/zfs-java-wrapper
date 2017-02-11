package zfs.java;

import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.common.CommonDeviceDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;

/**
 * SolarisDeviceTest 20.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
public class SolarisDeviceTest extends TestParent {

    private static final Host HOST = new Host("root", "localhost");
    private Map<String, Device> devices = null;

    private void dectectDevices(final String file) {
        DeviceDetector fd = new CommonDeviceDetector(HOST);
        parseFile(file, fd);
        devices = fd.getDevices();
    }

    @Test
    public void testDeviceListing1() {
        dectectDevices("resources/openindiana/openindiana.format1.txt");
        assertTrue(devices.size() == 10);
    }

    @Test
    public void testDeviceListing2() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertTrue(devices.size() == 48);
    }

    @Test
    public void testDeviceListing3() {
        dectectDevices("resources/solaris/solaris.format3.txt");
        assertTrue(devices.size() == 28);
    }
}
