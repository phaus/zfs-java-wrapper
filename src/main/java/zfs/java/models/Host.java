/**
 * Host 03.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Host {

    public static final int DEFAULT_SSH_PORT = 22;
    // Public Modifier are correct here, see http://www.playframework.org/documentation/1.2.5/model .
    //CHECKSTYLE:OFF
    public String url;
    public String user;
    public int port;
    // TODO Maybe we should use a Map<String,Device> here.
    public Set<Device> devices;
    public List<ZPOOL> pools;
    //CHECKSTYLE:ON

    public Host(final String user, final String url, final int port) {
        this.user = user;
        this.url = url;
        this.port = port;
        this.devices = new TreeSet<Device>();
        this.pools = new ArrayList<ZPOOL>();
    }

    public Host(final String user, final String url) {
        this(user, url, DEFAULT_SSH_PORT);
    }

    public void setDevices(Collection<Device> devices) {
        this.devices = new TreeSet<Device>();
        for (Device d : devices) {
            this.devices.add(d);
        }
    }

    @Override
    public String toString() {
        return user + "@" + url;
    }
}
