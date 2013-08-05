/**
 * ZPOOL 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

import java.util.ArrayList;
import java.util.List;

public class ZPOOL extends ZFSElement {

    public static final String NAME = "NAME";
    public static final String SIZE = "SIZE";
    public static final String ALLOC = "ALLOC";
    public static final String FREE = "FREE";
    public static final String EXPANDSZ = "EXPANDSZ";
    public static final String CAP = "CAP";
    public static final String DEDUP = "DEDUP";
    public static final String HEALTH = "HEALTH";
    public static final String ALTROOT = "ALTROOT";
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF    
    public String name;
    public String size;
    public String alloc;
    public String free;
    public String expandsz;
    public String cap;
    public String dedup;
    public String altroot;
    public String status;
    public String action;
    public String errors;
    public String see;
    public String scrub;
    public String scan;
    public int version;
    public String history;
    public Host host;
    public List<ZFS> filesystems;
    public List<Pool> pools;
    public List<ZFSAttribute> attributes;
    //CHECKSTYLE:ON

    public ZPOOL() {
        filesystems = new ArrayList<ZFS>();
        pools = new ArrayList<Pool>();
        attributes = new ArrayList<ZFSAttribute>();
    }

    public State getHealth() {
        return state;
    }

    public void setHealth(final State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(host).append(":").append("\n");
        if (scan != null) {
            sb.append("scan: ").append(scan).append("\n");
        }
        if (scrub != null) {
            sb.append("scrub: ").append(scrub).append("\n");
        }
        if (status != null) {
            sb.append("status: ").append(status).append("\n");
        }
        if (see != null) {
            sb.append("see: ").append(see).append("\n");
        }
        if (action != null) {
            sb.append("action: ").append(action).append("\n");
        }
        if (errors != null) {
            sb.append("errors: ").append(errors).append("\n");
        }
        sb.append(name).append("\t\t").append(super.toString()).append("\n");
        for (Pool pool : pools) {
            sb.append(pool).append("\n");
        }
        return sb.toString();
    }
}
