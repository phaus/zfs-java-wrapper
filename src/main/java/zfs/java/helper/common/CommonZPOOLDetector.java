/**
 * CommonZPOOLDetector 04.11.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oval.internal.util.StringUtils;
import zfs.java.helper.ZPOOLDetector;
import zfs.java.models.Device;
import zfs.java.models.Host;
import zfs.java.models.Partition;
import zfs.java.models.Pool;
import zfs.java.models.ZFSDevice;
import zfs.java.models.ZFSElement;
import zfs.java.models.ZPOOL;

public class CommonZPOOLDetector implements ZPOOLDetector {

    protected enum InfoType {

        POOL, DEFAULT, SCAN, SCRUB, SEE, ACTION, STATUS, CONFIG, ERRORS, SPARES, LOGS, CACHE
    };
    private static final Logger LOG = Logger.getLogger(CommonDeviceDetector.class.getName());
    private InfoType infoType;
    private InfoType infoTypeSecond;
    private StringBuilder content = new StringBuilder();
    private Map<String, ZPOOL> zfsPools;
    /**
     * We need to have a reveres ordered TreeMap (since we have to catch c2t0d10
     * bevore c2t0d1).
     */
    private Map<String, ZFSDevice> partitionMap = new TreeMap<String, ZFSDevice>(Collections.reverseOrder());
    private String currentZpoolName;
    private Host host;
    private int currentPoolIndex;

    public CommonZPOOLDetector(Host host) {
        this.host = host;
        this.zfsPools = new HashMap<String, ZPOOL>();
        mapDevicesToPartitions();
    }

    @Override
    public Map<String, ZPOOL> getPools() {
        return zfsPools;
    }

    @Override
    public void parse(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(CommonKeys.POOL_NAME_LABEL)) {
                    saveAndResetContent();
                    currentZpoolName = line.replace(CommonKeys.POOL_NAME_LABEL, "").trim();
                    handlePoolName();
                } else if (line.trim().startsWith(CommonKeys.POOL_STATE_LABEL)) {
                    ZPOOL pool = zfsPools.get(currentZpoolName);
                    pool.state = ZFSElement.State.valueOf(line.replace(CommonKeys.POOL_STATE_LABEL, "").trim());
                    zfsPools.put(currentZpoolName, pool);
                } else {
                    parseContent(line);
                }
            }
            saveAndResetContent();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
    }

    // TODO we need to add the Pool ZFSElement Data to the ZPOOL.
    protected void handlePoolName() {
        if (!zfsPools.containsKey(currentZpoolName)) {
            ZPOOL pool = new ZPOOL();
            infoType = InfoType.DEFAULT;
            pool.host = host;
            pool.name = currentZpoolName;
            zfsPools.put(currentZpoolName, pool);
        }
    }

    protected void saveAndResetContent() {
        ZPOOL pool = zfsPools.get(currentZpoolName);
        if (infoType != InfoType.DEFAULT && pool != null && !content.toString().isEmpty()) {
            if (infoType == InfoType.SCAN) {
                pool.scan = content.toString();
            }
            if (infoType == InfoType.STATUS) {
                pool.status = content.toString();
            }
            if (infoType == InfoType.SCRUB) {
                pool.scrub = content.toString();
            }
            if (infoType == InfoType.SEE) {
                pool.see = content.toString();
            }
            if (infoType == InfoType.ACTION) {
                pool.action = content.toString();
            }
            if (infoType == InfoType.ERRORS) {
                pool.errors = content.toString();
            }
            zfsPools.put(currentZpoolName, pool);
            content = new StringBuilder();
        }
    }

    protected void handleConfig(final String line) {
        if (line != null
                && !line.trim().isEmpty()
                && !line.trim().startsWith(currentZpoolName)) {
            boolean isNew = false;
            for (Pool.Type type : Pool.Type.values()) {
                if (line.startsWith(type.toString())) {
                    addPool(type, line);
                    isNew = true;
                }
            }
            if (!isNew && infoTypeSecond == InfoType.CONFIG) {
                addPool(Pool.Type.STRIPED, line);
                // We do not need to skip actual line for device scanning here.
            }
            if (!isNew) {
                handleNewConfig(line);
            }
        }
    }

    protected Pool addDeviceToPool(Pool pool, ZFSDevice device) {
        if (device != null) {
            switch (infoTypeSecond) {
                case SPARES:
                    pool.spares.add(device);
                    break;
                case LOGS:
                    pool.logs.add(device);
                    break;
                case CACHE:
                    pool.caches.add(device);
                    break;
                default:
                    pool.devices.add(device);
            }
        }
        return pool;
    }

    protected ZFSDevice mapLabelToDevice(final String line) {
        ZFSDevice d = null;
        for (String name : partitionMap.keySet()) {
            if (line.startsWith(name)) {
                d = partitionMap.get(name);
                return d;
            }
        }
        return d;
    }

    protected Pool mapLineToPool(Pool.Type type, String line) {
        ZFSElement p = new Pool(type);
        p = addInfoToElement(p, line);
        return (Pool) p;
    }

    protected ZPOOL mapLineToZPool(String line) {
        ZFSElement zp = new ZPOOL();
        zp = addInfoToElement(zp, line);
        return (ZPOOL) zp;
    }

    private void mapDevicesToPartitions() {
        if (host.devices != null) {
            for (Device d : host.devices) {
                for (Partition p : d.partitions) {
                    // TODO replace with ZFSDevice search Statement.
                    partitionMap.put(p.name, new ZFSDevice(p));
                    partitionMap.put(p.gptId, new ZFSDevice(p));
                }
            }
        } else {
            LOG.log(Level.WARNING, "Pool: {0} has now Devices!", currentZpoolName);
        }


    }

    protected String[] cleanSplit(final String line, final String sep) {
        List<String> list = new ArrayList<String>();
        for (String part : line.split(sep)) {
            if (!part.trim().isEmpty()) {
                list.add(part);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    private void addPool(Pool.Type type, String line) {
        currentPoolIndex = zfsPools.get(currentZpoolName).pools.size();
        zfsPools.get(currentZpoolName).pools.add(mapLineToPool(type, line));
        infoTypeSecond = InfoType.DEFAULT;
    }

    private ZFSElement addInfoToElement(ZFSElement element, final String line) {

        if (element != null) {
            String parts[] = cleanSplit(line, " ");
            int c = 0;
            try {
                for (String part : parts) {
                    handleInfoElement(element, part, c);
                    c++;
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "line: {0}", line);
                element.message = StringUtils.implode(Arrays.copyOfRange(parts, c, parts.length), " ");
            }
        }
        return element;
    }

    private void handleInfoElement(ZFSElement element, final String part, final int c) {
        switch (c) {
            case 0:
                break;
            case 1:
                for (ZFSElement.State state : ZFSElement.State.values()) {
                    if (state.toString().equals(part)) {
                        element.state = state;
                    }
                }
                break;
            case 2:
                element.read = Integer.parseInt(part);
                break;
            case 3:
                element.write = Integer.parseInt(part);
                break;
            case 4:
                element.cksum = Integer.parseInt(part);
                break;
            default:
                element.message += part + " ";
        }
    }

    private void handleNewConfig(final String line) {
        if (line.startsWith(CommonKeys.POOL_CONFIG_HEADER)) {
            infoTypeSecond = InfoType.CONFIG;
        } else if (line.startsWith(CommonKeys.POOL_CACHE_LABEL)) {
            infoTypeSecond = InfoType.CACHE;
        } else if (line.startsWith(CommonKeys.POOL_LOGS_LABEL)) {
            infoTypeSecond = InfoType.LOGS;
        } else if (line.startsWith(CommonKeys.POOL_SPARES_LABEL)) {
            infoTypeSecond = InfoType.SPARES;
        } else {
            ZFSElement element = mapLabelToDevice(line);
            element = addInfoToElement(element, line.trim());
            Pool p = zfsPools.get(currentZpoolName).pools.get(currentPoolIndex);
            if (p != null && element instanceof ZFSDevice) {
                zfsPools.get(currentZpoolName).pools.set(currentPoolIndex, addDeviceToPool(p, (ZFSDevice) element));
            }
        }
    }

    // TODO we should remove complexity here  
    private void parseContent(final String line) {
        if (line.trim().startsWith(CommonKeys.POOL_SCAN_LABEL)) {
            saveAndResetContent();
            content.append(line.trim().replace(CommonKeys.POOL_SCAN_LABEL, "").trim());
            infoType = InfoType.SCAN;
        } else if (line.trim().startsWith(CommonKeys.POOL_SCRUB_LABEL)) {
            saveAndResetContent();
            content.append(line.replace(CommonKeys.POOL_SCRUB_LABEL, "").trim());
            infoType = InfoType.SCRUB;
        } else if (line.trim().startsWith(CommonKeys.POOL_SEE_LABEL)) {
            saveAndResetContent();
            content.append(line.replace(CommonKeys.POOL_SEE_LABEL, "").trim());
            infoType = InfoType.SEE;
        } else if (line.trim().startsWith(CommonKeys.POOL_ACTION_LABEL)) {
            saveAndResetContent();
            content.append(line.replace(CommonKeys.POOL_ACTION_LABEL, "").trim());
            infoType = InfoType.ACTION;
        } else if (line.trim().startsWith(CommonKeys.POOL_STATUS_LABEL)) {
            saveAndResetContent();
            content.append(line.replace(CommonKeys.POOL_STATUS_LABEL, "").trim());
            infoType = InfoType.STATUS;
        } else if (line.trim().startsWith(CommonKeys.POOL_CONFIG_LABEL)) {
            saveAndResetContent();
            infoType = InfoType.CONFIG;
        } else if (line.trim().startsWith(CommonKeys.POOL_ERRORS_LABEL)) {
            saveAndResetContent();
            content.append(line.replace(CommonKeys.POOL_ERRORS_LABEL, "").trim());
            infoType = InfoType.ERRORS;
        } else if (infoType == InfoType.CONFIG) {
            handleConfig(line.trim());
        } else {
            content.append(" ").append(line.trim());
        }
    }
}
