package zfs.java;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import zfs.java.helper.DeviceDetector;
import zfs.java.helper.ZPOOLDetector;
import zfs.java.helper.common.CommonZPOOLDetector;
import zfs.java.helper.freebsd.FreeBSDDeviceDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;
import zfs.java.models.Pool;
import zfs.java.models.ZPOOL;

/**
 * FreeBSDPoolTest 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
public class FreeBSDPoolTest extends TestParent {

    private static Host HOST = new Host("root", "localhost");
    private final static String TANK = "tank";
    private final static String TANK_2 = "tank2";

    private Map<String, Device> getDeviceMapping() {
        DeviceDetector fd = new FreeBSDDeviceDetector(HOST);
        String file;
        file = "resources/freenas.dmesg.txt";
        parseFile(file, fd);
        file = "resources/freenas.glabel.status.txt";
        parseFile(file, fd);
        return fd.getDevices();
    }

    private Set<Device> getDectectedDevices() {
        Map<String, Device> devicesMap = getDeviceMapping();
        Set<Device> devices = new TreeSet<Device>();
        devices.addAll(devicesMap.values());
        return devices;
    }

    @Test
    public void testZPoolStatus1() {
        HOST.setDevices(getDectectedDevices());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/freenas.zpool.status1.txt";
        parseFile(file, fd);
        ZPOOL zPool = (ZPOOL) first(fd.getPools());
        debug(zPool, "zPool");
        assertTrue(zPool != null);
        assertTrue(TANK.equals(zPool.name));
        assertTrue(zPool.pools.size() == 1);
        Pool subPool = (Pool) first(zPool.pools);
        debug(zPool.pools, "zPool.pools");
        assertTrue(subPool != null);
        assertTrue(subPool.type.equals(Pool.Type.MIRROR));
        assertTrue(subPool.devices.size() == 2);
    }

    @Test
    public void testZPoolStatus2() {
        HOST.setDevices(getDectectedDevices());
        ZPOOLDetector fd = new CommonZPOOLDetector(HOST);
        String file = "resources/freenas.zpool.status2.txt";
        parseFile(file, fd);
        ZPOOL zPool = (ZPOOL) first(fd.getPools());
        debug(zPool, "zPool");
        assertTrue(zPool != null);
        assertTrue(TANK_2.equals(zPool.name));
        assertTrue(zPool.pools.size() == 1);
        Pool subPool = (Pool) first(zPool.pools);
        debug(zPool.pools, "zPool.pools");
        assertTrue(subPool != null);
        assertTrue(subPool.type.equals(Pool.Type.RAIDZ1));
        assertTrue(subPool.devices.size() == 3);
    }
}
