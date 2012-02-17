/**
 * 
 */
package com.stephenhitchner.common.io.reader;

import java.io.BufferedReader;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;

public interface DataRowParser
{
    public abstract void readMetadata(BufferedReader reader) throws DataRowException;

    public abstract DataRow readDataRow(BufferedReader reader);

    public abstract String getDescription();

}