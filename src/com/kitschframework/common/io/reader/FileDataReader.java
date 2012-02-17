package com.stephenhitchner.common.io.reader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;

/**
 * FileDataReader
 * 
 * Abstract class that reads from a local file system file
 * 
 * 
 *
 */
public class FileDataReader implements DataReader {

	private BufferedReader reader;
	private DataRowParser parser;
	private String fileName;
	private DataRow nextDataRow = null;

	public FileDataReader(String fileName, DataRowParser parser)  throws DataRowException {
		this.fileName = fileName;
		this.parser = parser;
		reset();
	}
	
	@Override
	public final boolean hasNextDataRow() {
		return nextDataRow != null;
	}

	@Override
	public final DataRow getNextDataRow() throws DataRowException {
		DataRow thisRow = nextDataRow;
		nextDataRow = parser.readDataRow(reader);
		return thisRow;
	}
	
	@Override
	public final void reset() throws DataRowException {
	    try {
			// If the provided input file is a GZIP file pass the FileInputStream to a GZIPInputStream.
			FileInputStream fileInput = new FileInputStream(fileName);
			InputStream input = null;
			
			if (fileName.endsWith(".gz")) {
			    input = new GZIPInputStream(fileInput);
			}
			else {
			    input = fileInput;
			}
			
			reader = new BufferedReader(new InputStreamReader(new DataInputStream(input)));
			parser.readMetadata(reader);
			nextDataRow = parser.readDataRow(reader);
		}
	    catch (FileNotFoundException e) {
			throw new DataRowException(e);
		}
	    catch (IOException e) {
			throw new DataRowException(e);
		}
	}

	@Override
	public final void close() {
	    try {
			reader.close();
		}
	    catch (IOException e) {
	    	//TODO what do you do when you get this exception, honestly?
			e.printStackTrace();
		}
	}
		
	protected final BufferedReader getReader() {
		return reader;
	}
	
	public final String getDescription() {
		return fileName + "[" + parser.getDescription() + "]";
	}
}