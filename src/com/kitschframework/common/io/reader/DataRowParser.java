/**
 * 
 */
package com.kitschframework.common.io.reader;

import java.io.BufferedReader;

import com.kitschframework.common.io.DataRow;
import com.kitschframework.common.io.DataRowException;

public interface DataRowParser
{
    public abstract void readMetadata(BufferedReader reader) throws DataRowException;

    public abstract DataRow readDataRow(BufferedReader reader);

    public abstract String getDescription();

}