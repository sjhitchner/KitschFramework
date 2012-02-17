/**
 * 
 */
package com.stephenhitchner.common.io.writer;

import com.stephenhitchner.common.io.DataRowException;

/**
 * DataWriterFactory
 *  
 * The DataWriterFactory is responsible for constructing its corresponding DataReader.
 * 
 * TODO interface needs to be modified to make it less file system specific
 *  
 * 
 *
 */
public abstract class DataWriterFactory {
	/**
	 * Return corresponding DataWriter
	 * @return
	 */
	public abstract DataWriter create(String fileName) throws DataRowException;
	
	public final static DataWriterFactory getFactory(String dataWriterFactoryClassName) throws DataRowException {
		
		Class<? extends DataWriterFactory> clazz;
		try {
			clazz = Class.forName(dataWriterFactoryClassName).asSubclass(DataWriterFactory.class);
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
