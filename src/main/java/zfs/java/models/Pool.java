/**
 * Pool 03.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

import java.util.ArrayList;
import java.util.List;

public class Pool extends ZFSElement {

    public static final String POOL = "pool";

    public enum Type {

        MIRROR("mirror"),
        STRIPED("striped"),
        RAIDZ1("raidz1"),
        RAIDZ2("raidz2"),
        RAIDZ3("raidz3");

        private Type(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
        private final String type;
    }
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF    
    public Type type;
    public List<ZFSDevice> devices;
    public List<ZFSDevice> spares;
    public List<ZFSDevice> logs;
    public List<ZFSDevice> caches;
    //CHECKSTYLE:ON

    public Pool(Type type) {
        this.type = type != null ? type : Type.STRIPED;
        devices = new ArrayList<ZFSDevice>();
        spares = new ArrayList<ZFSDevice>();
        logs = new ArrayList<ZFSDevice>();
        caches = new ArrayList<ZFSDevice>();
    }

    public Pool() {
        this(Type.STRIPED);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(type).append("\t\t").append(super.toString()).append("\n");
        sb.append("devices: ").append("\n");
        for (ZFSDevice d : devices) {
            sb.append("\t").append(d).append("\n");
        }
        sb.append("caches: ").append("\n");
        for (ZFSDevice d : caches) {
            sb.append("\t").append(d).append("\n");
        }
        sb.append("logs: ").append("\n");
        for (ZFSDevice d : logs) {
            sb.append("\t").append(d).append("\n");
        }
        sb.append("spares: ").append("\n");
        for (ZFSDevice d : spares) {
            sb.append("\t").append(d).append("\n");
        }
        return sb.toString();
    }
}
