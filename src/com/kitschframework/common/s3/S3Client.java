package com.stephenhitchner.common.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3Client
{
    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";

    private final AmazonS3      s3;

    public S3Client() {
        AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        this.s3 = new AmazonS3Client(myCredentials);
    }

    public S3Object getObject(S3Bucket bucket, String simpleFileName) {
        // TODO Auto-generated method stub
        return null;
    }
}
