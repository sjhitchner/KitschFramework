package com.kitschframework.common.io.writer;

import com.kitschframework.common.io.DataRow;
import com.kitschframework.common.io.DataRowException;

/**
 * Abstraction of how to write Data to a file or other source
 * 
 *
 * Usage:
 * <code>
 * DataWriter writer = task.getDataWriter();
 * for (DataRow row : task.getItemsSelected()) {
 *     writer.writeDataRow(row);
 * }
 * writer.close();
 * </code
 */
public interface DataWriter {
	public abstract void reset() throws DataRowException;
	public abstract void close();
	public abstract void writeDataRow(DataRow dataRow);
}
