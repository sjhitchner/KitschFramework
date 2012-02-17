package com.kitschframework.common.io.reader;

import com.kitschframework.common.io.DataRow;
import com.kitschframework.common.io.DataRowException;

public interface DataReader
{
    public abstract void reset() throws DataRowException;

    public abstract void close();

    public abstract boolean hasNextDataRow();

    public abstract DataRow getNextDataRow() throws DataRowException;

    public abstract String getDescription();
}
