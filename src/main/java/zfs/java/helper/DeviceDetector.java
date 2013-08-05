/**
 * DeviceDetector 01.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper;

import de.javastream.javassh.parser.ProcessParser;
import java.util.Map;
import zfs.java.models.Device;

/**
 *
 * @author Philipp Haußleiter
 */
public interface DeviceDetector extends ProcessParser {

    Map<String, Device> getDevices();
}
