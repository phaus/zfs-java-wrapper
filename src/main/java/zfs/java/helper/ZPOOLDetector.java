/**
 * ZPOOLDetector 03.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper;

import de.javastream.javassh.parser.ProcessParser;
import java.util.Map;
import zfs.java.models.ZPOOL;

/**
 *
 * @author Philipp Haußleiter
 */
public interface ZPOOLDetector extends ProcessParser {

    public Map<String, ZPOOL> getPools();
}
