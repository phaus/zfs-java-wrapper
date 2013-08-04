/**
 * ZFS 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

import java.util.ArrayList;
import java.util.List;

public class ZFS {

    public final static String NAME = "NAME";
    public final static String USED = "USED";
    public final static String AVAIL = "AVAIL";
    public final static String REFER = "REFER";
    public final static String MOUNTPOINT = "MOUNTPOINT";
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF    
    public String name;
    public String used;
    public String avail;
    public String refer;
    public String mountpoint;
    public ZPOOL pool;
    public List<ZFSAttribute> attributes;
    //CHECKSTYLE:ON

    public ZFS() {
        attributes = new ArrayList<ZFSAttribute>();
    }
}
