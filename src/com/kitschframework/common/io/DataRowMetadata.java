/**
 * 
 */
package com.stephenhitchner.common.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 */
public class DataRowMetadata implements Iterable<String>, Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> map = new LinkedHashMap<String, Integer>();
    private List<String> keys = new ArrayList<String>();

    public DataRowMetadata() {
    }

    public DataRowMetadata(final String header[]) {
        int index = 0;
        for (final String key : header) {
            map.put(key, index);
            keys.add(key);
            index++;
        }
    }

    public int getIndexOf(final String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        else {
            return -1;
        }
    }

    public int addKey(final String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        else {
            keys.add(key);
            final int index = keys.size() - 1;
            map.put(key, index);
            return index;
        }
    }

    public Set<String> getKeySet() {
        return map.keySet();
    }

    public boolean containsKey(final String key) {
        return map.containsKey(key);
    }

    /**
     * Returns an iterator of the keys, TODO might not be completely obvious, might change
     * this to an Entry set, allows string allows for simple code
     */
    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    public int getKeyCount() {
        return keys.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keys == null) ? 0 : keys.hashCode());
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DataRowMetadata other = (DataRowMetadata) obj;
        if (keys == null) {
            if (other.keys != null)
                return false;
        }
        else if (!keys.equals(other.keys))
            return false;
        if (map == null) {
            if (other.map != null)
                return false;
        }
        else if (!map.equals(other.map))
            return false;
        return true;
    }
}
