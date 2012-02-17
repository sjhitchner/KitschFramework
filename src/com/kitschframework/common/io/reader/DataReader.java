package com.stephenhitchner.common.io.reader;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;

public interface DataReader
{
    public abstract void reset() throws DataRowException;

    public abstract void close();

    public abstract boolean hasNextDataRow();

    public abstract DataRow getNextDataRow() throws DataRowException;

    public abstract String getDescription();
}
