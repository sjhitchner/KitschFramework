/**
 * 
 */
package com.kitschframework.common.io;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectWriter;

import com.kitschframework.common.utils.SerializationUtils;

public class DataRow implements Iterable<String>, Serializable
{
    private static final long serialVersionUID = 1L;

    private DataRowMetadata   metadata         = null;

    private List<String>      values           = new ArrayList<String>();

    public DataRow() {
        this.metadata = new DataRowMetadata();
    }

    public DataRow(final DataRow dataRow) throws DataRowException {
        if (metadata.equals(dataRow.metadata)) {
            this.values = dataRow.values;
        }
        else {
            throw new DataRowException("Metadata is incompatable");
        }
    }

    public DataRow(final DataRowMetadata metadata, final String[] values) throws DataRowException {
        this.metadata = metadata;
        if (values.length <= metadata.getKeyCount()) {
            for (final String value : values) {
                this.values.add(value);
            }
        }
        else {
            throw new DataRowException("metadata size does not match value size");
        }
    }

    public Set<String> getKeySet() {
        return metadata.getKeySet();
    }

    public boolean containsKey(final String key) {
        return metadata.containsKey(key);
    }

    public String getValueAsString(final int index) {
        if (index != -1 && index < values.size()) {
            return values.get(index);
        }
        return null;
    }

    public String getValueAsString(final String key) {
        final int index = metadata.getIndexOf(key);
        if (index != -1) {
            return getValueAsString(index);
        }
        return null;
    }

    public long getValueAsLong(final int index) {
        final String value = getValueAsString(index);
        if (!isNull(value)) {
            return Long.parseLong(value);
        }
        return 0L;
    }

    public long getValueAsLong(final String key) {
        final int index = metadata.getIndexOf(key);
        return getValueAsLong(index);
    }

    public int getValueAsInt(final int index) {
        final String value = getValueAsString(index);
        if (!isNull(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public int getValueAsInt(final String key) {
        final int index = metadata.getIndexOf(key);
        return getValueAsInt(index);
    }

    public double getValueAsDouble(final int index) {
        final String value = getValueAsString(index);
        if (!isNull(value)) {
            return Double.parseDouble(value);
        }
        return 0.0;
    }

    public double getValueAsDouble(final String key) {
        final int index = metadata.getIndexOf(key);
        return getValueAsDouble(index);
    }

    public boolean getValueAsBoolean(final int index) {
        final String value = getValueAsString(index);
        if (!isNull(value)) {
            return Boolean.parseBoolean(value);
        }
        return false;
    }

    public boolean getValueAsBoolean(final String key) {
        final int index = metadata.getIndexOf(key);
        return getValueAsBoolean(index);
    }

    public Date getValueAsDate(final int index, final String format) throws DataRowException {
        final String value = getValueAsString(index);

        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(value);
        }
        catch (final ParseException e) {
            throw new DataRowException("Invalid date format: " + format + " for date " + value);
        }
    }

    public Date getValueAsDate(final String key, final String format) throws DataRowException {
        final int index = metadata.getIndexOf(key);
        return getValueAsDate(index, format);
    }

    public void addOrReplaceStringValue(final String key, final String value) {
        final int index = metadata.addKey(key);
        if (index < values.size()) {
            values.set(index, value);
        }
        else {
            values.add(index, value);
        }
    }

    public void addOrReplaceIntValue(final String key, final int value) {
        addOrReplaceStringValue(key, Integer.toString(value));
    }

    public void addOrReplaceLongValue(final String key, final long value) {
        addOrReplaceStringValue(key, Long.toString(value));
    }

    public void addOrReplaceDoubleValue(final String key, final double value) {
        addOrReplaceStringValue(key, Double.toString(value));
    }

    public void addOrReplaceBooleanValue(final String key, final boolean value) {
        addOrReplaceStringValue(key, Boolean.toString(value));
    }

    public void addOrReplaceDateValue(final String key, final Date date, final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        addOrReplaceStringValue(key, sdf.format(date));
    }

    private boolean isNull(final String value) {
        return value == null || value.length() == 0 || value.equals("null");
    }

    /**
     * Returns an iterator of the keys, TODO might not be completely obvious,
     * might change this to an Entry set, allows string allows for simple code
     */
    @Override
    public Iterator<String> iterator() {
        return metadata.iterator();
    }

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<String, String>();
        for (String key : metadata) {
            int index = metadata.getIndexOf(key);
            map.put(key, values.get(index));
        }
        ObjectWriter writer = SerializationUtils.getObjectPrettyPrinter();
        try {
            return writer.writeValueAsString(map);
        }
        catch (Exception e) {
            return "";
        }
    }
}
