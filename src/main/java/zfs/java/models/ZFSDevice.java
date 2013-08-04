/**
 * ZFSDevice 03.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

public class ZFSDevice extends ZFSElement {

    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF
    public String name;
    public Partition partition;
    public Pool pool;
    //CHECKSTYLE:ON

    public ZFSDevice(Partition partition) {
        this.partition = partition;
        this.name = partition.name;
    }

    @Override
    public String toString() {
        return this.partition.toString() + "\t" + super.toString();
    }
}
