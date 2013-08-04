/**
 * Device 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

import java.util.ArrayList;
import java.util.List;

public class Device implements Comparable<Device> {

    // TODO create enum for this
    public final static int IDE_HDD = 0;
    public final static int SCSI_HDD = 1;
    public final static int IDE_CDROM = 2;
    public final static int SCSI_CDROM = 3;
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF    
    public String bus;
    public String name;
    public String description;
    public String transfer;
    public int type;
    public long size;
    public Host host;
    public List<Partition> partitions;
    //CHECKSTYLE:ON

    public Device() {
        this.partitions = new ArrayList<Partition>();
    }

    public Device(final String name, final int type) {
        this.partitions = new ArrayList<Partition>();
        this.name = name;
        this.type = type;
    }

    public void addPartition(Partition partition) {
        this.partitions.add(partition);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name).append("\n");
        sb.append("bus: ").append(bus).append("\n");
        sb.append("description: ").append(description).append("\n");
        sb.append("transfer: ").append(transfer).append("\n");
        sb.append("size: ").append(size).append("\n");
        if (partitions.size() > 0) {
            sb.append("partitions: ").append("\n");
            for (Partition p : partitions) {
                sb.append("\t").append(p).append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public int compareTo(Device o) {
        if (o == null) {
            return 1;
        }
        return this.name.compareTo(o.name);
    }
}
