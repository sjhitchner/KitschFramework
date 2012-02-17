/**
 * 
 */
package com.stephenhitchner.common.io.reader;

import com.stephenhitchner.common.io.DataRowException;

public abstract class DataReaderFactory
{
    public abstract DataReader create(String fileName) throws DataRowException;

    public final static DataReaderFactory
            getFactory(String dataReaderFactoryClassName) throws DataRowException {

        Class<? extends DataReaderFactory> clazz;
        try {
            clazz = Class.forName(dataReaderFactoryClassName).asSubclass(DataReaderFactory.class);
            return clazz.newInstance();
        }
        catch (ClassNotFoundException e) {
            throw new DataRowException(e);
        }
        catch (InstantiationException e) {
            throw new DataRowException(e);
        }
        catch (IllegalAccessException e) {
            throw new DataRowException(e);
        }
    }
}