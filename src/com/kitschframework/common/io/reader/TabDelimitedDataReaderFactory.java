/**
 * 
 */
package com.kitschframework.common.io.reader;

import com.kitschframework.common.io.DataRowException;

public class TabDelimitedDataReaderFactory extends DataReaderFactory
{

    @Override
    public DataReader create(String fileName) throws DataRowException {
        return new FileDataReader(fileName, new TabDelimitedDataRowParser());
    }
}
