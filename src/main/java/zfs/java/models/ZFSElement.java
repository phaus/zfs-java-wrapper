/**
 * ZFSElement 03.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

public class ZFSElement {

    public enum State {

        AVAIL("AVAIL"),
        DEGRADED("DEGRADED"),
        FAULTED("FAULTED"),
        OFFLINE("OFFLINE"),
        ONLINE("ONLINE"),
        REMOVED("REMOVED"),
        UNAVAIL("UNAVAIL"),
        INUSE("INUSE");
        private final String value;
        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String CKSUM = "CKSUM";
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF
    public State state;
    public String message = "";
    public int read = 0;
    public int write = 0;
    public int cksum = 0;
    //CHECKSTYLE:ON

    public String getMessage() {
        if (message == null) {
            return "";
        }
        return message;
    }

    @Override
    public String toString() {
        return String.format("%-12s", state.toString()) + "\t" + read + "\t" + write + "\t" + cksum + "\t" + getMessage();
    }
}
