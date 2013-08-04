package zfs.java;

import java.util.Map;
import org.junit.Test;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.ZPOOLDetector;
import zfs.java.helper.common.CommonDeviceDetector;
import zfs.java.helper.common.CommonZPOOLDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;

/**
 * SolarisPoolTest 04.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
public class SolarisPoolTest extends TestParent {

    private static Host HOST = new Host("root", "localhost");
    private Map<String, Device> devices = null;

    private void dectectDevices(final String file) {
        DeviceDetector fd = new CommonDeviceDetector(HOST);
        parseFile(file, fd);
        devices = fd.getDevices();
    }

    @Test
    public void testOIZPoolStatus1() {
        dectectDevices("resources/openindiana.format1.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/openindiana.zpool.status1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testOIZPoolStatus2() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/openindiana.zpool.status2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testOIZPoolStatus3() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/openindiana.zpool.status3.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus1() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.thumper1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus2() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.thumper2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus3() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.thumper3.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus4() {
        dectectDevices("resources/openindiana.format2.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status4.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus5() {
        dectectDevices("resources/solaris.format3.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status5.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolDegraded1() {
        dectectDevices("resources/solaris.format4.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.degraded1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }
    
    @Test
    public void testSolarisZPoolDegraded2() {
        dectectDevices("resources/solaris.format4.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.degraded2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }    

    @Test
    public void testSolarisZPoolReplacment1() {
        dectectDevices("resources/solaris.format5.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.replacement1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }   
    
    @Test
    public void testSolarisZPoolReplacment2() {
        dectectDevices("resources/solaris.format5.txt");
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris.zpool.status.replacement2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }       
}
