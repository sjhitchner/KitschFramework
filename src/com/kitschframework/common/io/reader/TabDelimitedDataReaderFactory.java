/**
 * 
 */
package com.stephenhitchner.common.io.reader;

import com.stephenhitchner.common.io.DataRowException;

public class TabDelimitedDataReaderFactory extends DataReaderFactory
{

    @Override
    public DataReader create(String fileName) throws DataRowException {
        return new FileDataReader(fileName, new TabDelimitedDataRowParser());
    }
}
