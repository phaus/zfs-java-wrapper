/**
 * SmartOSDeviceTest 07.08.2013
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java;

import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.common.CommonDeviceDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;

public class SmartOSDeviceTest extends TestParent {

    private static final Host HOST = new Host("root", "localhost");
    private Map<String, Device> devices = null;

    private void dectectDevices(final String file) {
        DeviceDetector fd = new CommonDeviceDetector(HOST);
        parseFile(file, fd);
        devices = fd.getDevices();
    }

    @Test
    public void testDeviceListing1() {
        dectectDevices("resources/smartOS/smartOS.format1.txt");
        assertTrue(devices.size() == 2);
    }

    @Test
    public void testDeviceListing2() {
        dectectDevices("resources/smartOS/smartOS.format2.txt");
        assertTrue(devices.size() == 2);
    }
    
    @Test
    public void testDeviceListing3() {
        dectectDevices("resources/smartOS/smartOS.format3.txt");
        assertTrue(devices.size() == 8);
    }    
}
