/**
 * Pair 20.10.2012
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.models;

public class Pair<TYPEA, TYPEB> implements Comparable< Pair<TYPEA, TYPEB>> {

    private final TYPEA key;
    private final TYPEB value;

    public Pair(TYPEA key, TYPEB value) {
        this.key = key;
        this.value = value;
    }

    public TYPEA getKey() {
        return key;
    }

    public TYPEB getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("Key: ");
        buff.append(key);
        buff.append("\tValue: ");
        buff.append(value);
        return (buff.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (null != other) {
            if (!(other instanceof Pair)) {
                return false;
            }
            Pair p1 = (Pair) other;
            if (p1.key.equals(this.key) && value.equals(this.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = key.hashCode() + (31 * value.hashCode());
        return (hashCode);
    }

    public int compareTo(Pair<TYPEA, TYPEB> p1) {
        if (null != p1) {
            if (p1.equals(this)) {
                return 0;
            } else if (p1.hashCode() > this.hashCode()) {
                return 1;
            } else if (p1.hashCode() < this.hashCode()) {
                return -1;
            }
        }
        return (-1);
    }
}
