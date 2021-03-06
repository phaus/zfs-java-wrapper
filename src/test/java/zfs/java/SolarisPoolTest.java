package zfs.java;

import java.util.Map;
import static org.junit.Assert.assertEquals;
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

    private static final Host HOST = new Host("root", "localhost");
    private Map<String, Device> devices = null;

    private void dectectDevices(final String file) {
        DeviceDetector fd = new CommonDeviceDetector(HOST);
        parseFile(file, fd);
        devices = fd.getDevices();
    }

    @Test
    public void testOIZPoolStatus1() {
        dectectDevices("resources/openindiana/openindiana.format1.txt");
        assertEquals(devices.values().size(), 10);         
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/openindiana/openindiana.zpool.status1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testOIZPoolStatus2() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48); 
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);

        String file = "resources/openindiana/openindiana.zpool.status2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testOIZPoolStatus3() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48);      
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/openindiana/openindiana.zpool.status3.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus1() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48);       
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.thumper1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus2() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48);         
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.thumper2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
        debugJson(fd.getPools(), "solaris.zpool.status.thumper2");
    }

    @Test
    public void testSolarisZPoolStatus3() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48);        
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.thumper3.txt";
        parseFile(file, fd);
        debug(fd.getPools());
        debugJson(fd.getPools(), "solaris.zpool.status.thumper3");        
    }

    @Test
    public void testSolarisZPoolStatus4() {
        dectectDevices("resources/openindiana/openindiana.format2.txt");
        assertEquals(devices.values().size(), 48);      
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status4.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolStatus5() {
        dectectDevices("resources/solaris/solaris.format3.txt");
        assertEquals(devices.values().size(), 28);          
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status5.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolDegraded1() {
        dectectDevices("resources/solaris/solaris.format4.txt");
        assertEquals(devices.values().size(), 4);          
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.degraded1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolDegraded2() {
        dectectDevices("resources/solaris/solaris.format4.txt");
        assertEquals(devices.values().size(), 4);        
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.degraded2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolReplacment1() {
        dectectDevices("resources/solaris/solaris.format5.txt");
        assertEquals(devices.values().size(), 4); 
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.replacement1.txt";
        parseFile(file, fd);
        debug(fd.getPools());
    }

    @Test
    public void testSolarisZPoolReplacment2() {
        dectectDevices("resources/solaris/solaris.format5.txt");
        assertEquals(devices.values().size(), 4);        
        HOST.setDevices(devices.values());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/solaris/solaris.zpool.status.replacement2.txt";
        parseFile(file, fd);
        debug(fd.getPools());
        debugJson(fd.getPools(), "solaris.zpool.status.replacement2");
    }
}
