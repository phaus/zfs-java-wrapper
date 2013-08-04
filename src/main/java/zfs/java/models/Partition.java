/**
 * Partition 04.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

public class Partition {

    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF
    public Device device;
    public String name;
    public String gptId;
    //CHECKSTYLE:ON

    public Partition(final String name) {
        this(name, name);
    }

    public Partition(final String name, final String gptId) {
        this.name = name;
        this.gptId = gptId;
    }

    @Override
    public String toString() {
        return gptId;
    }
}
