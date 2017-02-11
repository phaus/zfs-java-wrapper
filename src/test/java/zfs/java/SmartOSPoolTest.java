package zfs.java;

import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.ZPOOLDetector;
import zfs.java.helper.common.CommonDeviceDetector;
import zfs.java.helper.common.CommonZPOOLDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;

/**
 * SmartOSPoolTest 11.02.2017
 *
 * @author Philipp Haussleiter
 *
 */
public class SmartOSPoolTest extends TestParent {

    private static final Host HOST = new Host("root", "localhost");
    private Map<String, Device> devices = null;

    private void dectectDevices(final String file) {
        DeviceDetector fd = new CommonDeviceDetector(HOST);
        parseFile(file, fd);
        devices = fd.getDevices();
    }

    @Test
    public void testZPoolStatus1() {
        dectectDevices("resources/smartOS/smartOS.format1.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/smartOS/smartOS.zpool.status1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
        assertTrue(fd.getPools().containsKey("zones"));
    }

    @Test
    public void testZPoolStatus2() {
        dectectDevices("resources/smartOS/smartOS.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/smartOS/smartOS.zpool.status2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
        assertTrue(fd.getPools().containsKey("zones"));
    }

    @Test
    public void testZPoolStatus3() {
        dectectDevices("resources/smartOS/smartOS.format3.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/smartOS/smartOS.zpool.status3.txt";
        parseFile(file, fd);
        debug(fd.getPools());        
        assertTrue(fd.getPools().containsKey("tank"));
        assertTrue(fd.getPools().containsKey("temp"));
        assertTrue(fd.getPools().containsKey("zones"));
    }

}
