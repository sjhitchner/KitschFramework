package com.kitschframework.common.io.reader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.kitschframework.common.io.DataRow;
import com.kitschframework.common.io.DataRowException;
import com.kitschframework.common.s3.S3Bucket;
import com.kitschframework.common.s3.S3Client;
import com.kitschframework.common.s3.S3Object;

/**
 * FileDataReader
 * 
 * Abstract class that reads from a local file system file
 * 
 * 
 * 
 */
public class S3DataReader implements DataReader
{
    private BufferedReader reader;
    private DataRowParser  parser;

    private final String   s3FileName;
    private final S3Client client;

    private DataRow        nextDataRow = null;
    private final S3Bucket bucket;

    public S3DataReader(final S3Bucket bucket, final String s3FileName, final DataRowParser parser)
            throws Exception {
        this(new S3Client(), bucket, s3FileName, parser);
    }

    public S3DataReader(final S3Client client, final S3Bucket bucket, final String s3FileName,
            final DataRowParser parser) throws DataRowException {
        this.bucket = bucket;
        this.s3FileName = s3FileName;
        this.client = client;
        reset();
    }

    @Override
    public final boolean hasNextDataRow() {
        return nextDataRow != null;
    }

    @Override
    public final DataRow getNextDataRow() throws DataRowException {
        final DataRow thisRow = nextDataRow;
        nextDataRow = parser.readDataRow(reader);
        return thisRow;
    }

    @Override
    public final void reset() throws DataRowException {
        try {
            final S3Object object = client.getObject(bucket, s3FileName);

            final InputStream input = new GZIPInputStream(object.getDataStream());

            reader = new BufferedReader(new InputStreamReader(new DataInputStream(input)));
            parser.readMetadata(reader);
            nextDataRow = parser.readDataRow(reader);
        }
        catch (final FileNotFoundException e) {
            throw new DataRowException(e);
        }
        catch (final IOException e) {
            throw new DataRowException(e);
        }
    }

    @Override
    public final void close() {
        try {
            reader.close();
        }
        catch (final IOException e) {
            // TODO what do you do when you get this exception, honestly?
            e.printStackTrace();
        }
    }

    @Override
    public final String getDescription() {
        return bucket + "/" + s3FileName + "[" + parser.getDescription() + "]";
    }
}