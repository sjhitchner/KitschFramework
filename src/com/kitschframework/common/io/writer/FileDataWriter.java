/**
 * 
 */
package com.stephenhitchner.common.io.writer;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;

/**
 * FileDataWriter 
 * 
 * Abstract class that writes to a local file system file
 * 
 * 
 *
 */
public abstract class FileDataWriter implements DataWriter {

	private int writeCount = 0;
	private String fileName;
	private BufferedWriter writer;
	
	public FileDataWriter(String fileName) throws DataRowException {
		this.fileName = fileName;
		reset();
	}
	
	protected abstract void serializeDataRow(DataRow dataRow) throws IOException;

	@Override
	public final void writeDataRow(DataRow dataRow) {
		try {
			serializeDataRow(dataRow);
			writeCount++;
			if (writeCount%1000 == 0) {
				writer.flush();
			}
		}
		catch (IOException e) {
			//TODO exception handling
			e.printStackTrace();
		}
	}
	
	@Override
	public final void reset() throws DataRowException {
		try {
			// If the provided input file is a GZIP file pass the FileInputStream to a GZIPInputStream.
			FileOutputStream fileOutput = new FileOutputStream(fileName);
			OutputStream output = null;
			
			if (fileName.endsWith(".gz")) {
			    output = new GZIPOutputStream(fileOutput);
			}
			else {
			    output = fileOutput;
			}
			
			writer = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(output)));
		}
	    catch (FileNotFoundException e) {
			throw new DataRowException(e);
		}
	    catch (IOException e) {
			throw new DataRowException(e);
		}	// TODO Auto-generated method stub

	}
		
	@Override
	public final void close() {
		try {
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			//TODO what do you do when you get this exception?
			e.printStackTrace();
		}
	}

	protected final BufferedWriter getWriter() {
		return writer;
	}
	
	public final String getFileName() {
		return fileName;
	}
}
