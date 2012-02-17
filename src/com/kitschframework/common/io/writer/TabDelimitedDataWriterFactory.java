/**
 * 
 */
package com.stephenhitchner.common.io.writer;

import com.stephenhitchner.common.io.DataRowException;

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
