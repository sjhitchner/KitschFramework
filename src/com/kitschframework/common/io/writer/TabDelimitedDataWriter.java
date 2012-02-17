/**
 * 
 */
package com.stephenhitchner.common.io.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stephenhitchner.common.io.DataRow;
import com.stephenhitchner.common.io.DataRowException;

/**
 * TabDelimitedDataWriter
 * 
 * Self explanatory
 * 
 * 
 * 
 */
public class TabDelimitedDataWriter extends FileDataWriter {

    private boolean hasOutputHeaderRow;

    private final List<String> header = new ArrayList<String>();

    /**
	 * 
	 */
    public TabDelimitedDataWriter(final String fileName) throws DataRowException {
        super(fileName);
        hasOutputHeaderRow = false;
    }

    @Override
    protected void serializeDataRow(final DataRow dataRow) throws IOException {
        // TODO Auto-generated method stub
        if (hasOutputHeaderRow == false) {
            writeHeaderRow(dataRow);
            hasOutputHeaderRow = true;
        }

        boolean isFirst = true;
        for (final String key : header) {
            if (!isFirst) {
                writeTab();
            }
            writeString(dataRow.getValueAsString(key));
            isFirst = false;
        }
        writeNewLine();
    }

    private void writeHeaderRow(final DataRow dataRow) throws IOException {
        boolean isFirst = true;
        for (final String headerString : dataRow) {
            header.add(headerString);

            if (!isFirst) {
                writeTab();
            }
            writeString(headerString);
            isFirst = false;
        }
        writeNewLine();
    }

    private void writeString(final String str) throws IOException {
        if (str != null) {
            getWriter().write(str);
        }
    }

    private void writeTab() throws IOException {
        getWriter().write('\t');
    }

    private void writeNewLine() throws IOException {
        getWriter().write('\n');
    }

    @Override
    public String toString() {
        return "TabDelimitedDataWriter(" + getFileName() + ")";
    }
}
