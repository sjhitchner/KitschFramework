/**
 * 
 */
package com.stephenhitchner.common.io.reader;

import java.io.BufferedReader;
import java.io.IOException;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;
import com.stephenhitchner.common.io.DataRowMetadata;

public class TabDelimitedDataRowParser implements DataRowParser
{

    private DataRowMetadata metadata;

    public TabDelimitedDataRowParser() throws DataRowException {
    }

    /** {@inheritDoc} */
    @Override
    public void readMetadata(BufferedReader reader) throws DataRowException {
        try {
            String headerLine = reader.readLine();
            if (headerLine != null) {
                String[] header = headerLine.split("\t");
                this.metadata = new DataRowMetadata(header);
            }
        }
        catch (IOException e) {
            throw new DataRowException("Malformed TabDelimited header row", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public DataRow readDataRow(BufferedReader reader) {
        String rawRow = "";

        // TODO this is broken
        while (rawRow != null && rawRow.trim().length() == 0) {
            try {
                rawRow = reader.readLine();

                if (rawRow != null && rawRow.trim().length() > 0) {
                    String[] tokens = rawRow.split("\t");
                    if (tokens.length > 0 && tokens.length <= metadata.getKeyCount()) {
                        return new DataRow(metadata, tokens);
                    }
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
            catch (DataRowException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return "TabDelimitedDataParser";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
