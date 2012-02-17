/**
 * 
 */
package com.kitschframework.common.io.writer;

import com.kitschframework.common.io.DataRowException;

/**
 * TabDelimitedDataWriterFactory
 * 
 * Constructs a TabDelimitedDataWriter
 * 
 * 
 *
 */
public class TabDelimitedDataWriterFactory extends	DataWriterFactory {

	/**
	 * Returns a TabDelimitedDataWriter
	 */
	@Override
	public DataWriter create(String fileName) throws DataRowException {
		return new TabDelimitedDataWriter(fileName);
	}	
}
