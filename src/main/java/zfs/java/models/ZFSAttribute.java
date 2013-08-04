/**
 * ZFSAttribute 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

public class ZFSAttribute {

    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF
    public String name;
    public String property;
    public String value;
    public String source;
    public ZPOOL pool;
    public ZFS filesystem;
    //CHECKSTYLE:ON
}
