/*
 * Copyright 2017 Eonian Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eoniantech.secretslocker.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.eoniantech.secretslocker.SecretsLocker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotEmpty;
import static com.eoniantech.secretslocker.aws.Constants.BUCKET_NAME;
import static com.eoniantech.secretslocker.aws.Constants.BUCKET_PATH;
import static com.eoniantech.secretslocker.aws.Constants.LOCKER_PATH_NOT_WRITEABLE;
import static com.eoniantech.secretslocker.aws.Constants.NAME;
import static com.eoniantech.secretslocker.aws.Constants.S3_BUCKET_DOES_NOT_EXIST_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.S3_PATH_SEPARATOR;
import static com.eoniantech.secretslocker.aws.Constants.SECRET_NOT_FOUND;

/**
 * An implementation of the {@link SecretsLocker} that is backed by Amazon S3. 
 * Encrypted secrets do not need to exists locally and are only downloaded
 * when needed. AWS credentials are required.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 * @see DefaultAWSCredentialsProviderChain
 */
public class S3SecretsLocker extends AbtractFileSystemSecretsLocker {

    private String bucketName;
    private String bucketPath;
    private AmazonS3 s3Client; 

    /**
     * Constructor.
     * 
     * @param buckerName The name of the s3 bucket.
     * @param bucketPath The path in the bucket.
     * @param localLockerPath The path to the local locker.
     */
    public S3SecretsLocker(
            final String buckerName, 
            final String bucketPath, 
            final String localLockerPath) {

        this(buckerName, 
                bucketPath, 
                localLockerPath, 
                true);

    }

    /**
     * Constructor.
     * 
     * @param buckerName The name of the s3 bucket.
     * @param bucketPath The path in the bucket.
     * @param localLockerPath The path to the local locker.
     * @param validateBucket Validate that the S3 bucket exists when the 
     * locker is created.
     */
    public S3SecretsLocker(
            final String buckerName, 
            final String bucketPath, 
            final String localLockerPath, 
            final boolean validateBucket) {
        
        super(localLockerPath, false);

        setBucketPath(bucketPath);
        setClient();
        setBucketName(buckerName, validateBucket); 
    }

    @Override
    public String get(
            final String name) {

        assertArgumentNotEmpty(
                NAME, 
                name); 

        if (!contains(name))
            throw new SecretsLockerException(
                    SECRET_NOT_FOUND);

        File localFile 
                = secrets().get(
                        name);

        if (!localFile.exists()) 
            writeLocalFile(
                    downloadS3Object(
                            localFile.getName()),
                    localFile);

        return decrypt(
                localFile);
    }

    @Override
    public boolean contains(
            final String name) {
        
        assertArgumentNotEmpty(
                NAME, 
                name);

        return containsName(name) 
                && (containsLocalFile(name) 
                || containsRemoteFile(name));
        }

    private boolean containsName(
            final String name) {

        return secrets().containsKey(name);
    }

    private boolean containsLocalFile(
            final String name) {

        return secrets().get(name).exists();
    }

    private boolean containsRemoteFile(
            final String name) {

        return s3Client
                .doesObjectExist(
                        bucketName, 
                        buildS3ObjectName(
                                secrets()
                                        .get(name)
                                        .getName()));
    }

    private S3Object downloadS3Object(
            final String fileName) {
        
        return s3Client
                .getObject(
                        new GetObjectRequest(
                                bucketName,
                                buildS3ObjectName(
                                        fileName)));
    }

    private String buildS3ObjectName(
            final String fileName) {
        
        return new StringBuilder()
                .append(bucketPath)
                .append(S3_PATH_SEPARATOR)
                .append(fileName)
                .toString();
    }

    private void writeLocalFile(
            final S3Object s3Object, 
            final File file) {

        try(FileOutputStream fileOutputStream 
                = new FileOutputStream(file)) {

            IOUtils.copy(
                    s3Object.getObjectContent(), 
                    fileOutputStream);
            
        } catch (IOException ioException) {
            throw new SecretsLockerException(
                    ioException);
        }

        file.deleteOnExit();
    }

    private void setBucketName(
            final String bucketName,
            final boolean validateBucket) {

        assertArgumentNotEmpty(
                BUCKET_NAME, 
                bucketName);

        if (validateBucket 
                && !s3Client
                        .doesBucketExistV2(
                                bucketName))
            
            throw new IllegalArgumentException(
                    String.format(
                            S3_BUCKET_DOES_NOT_EXIST_PATTERN,
                            bucketName));
        
        this.bucketName = bucketName;
    }

    private void setBucketPath(final String bucketPath) {
        assertArgumentNotEmpty(
                BUCKET_PATH, 
                bucketPath);

        this.bucketPath = bucketPath;
    }

    private void setClient() {
        this.s3Client 
                = AmazonS3ClientBuilder.
                        standard()
                        .withCredentials(
                                credentialsProvider())
                        .build();
    }

    @Override
    protected File validateLockerPath(final String lockerPath) {
        File locker 
                = super.validateLockerPath(
                        lockerPath);

        if (!locker.canWrite())
            throw new IllegalArgumentException(
                    String.format(
                            LOCKER_PATH_NOT_WRITEABLE,
                            lockerPath));

        return locker;
    }

    private AWSCredentialsProvider credentialsProvider() {
        return DefaultAWSCredentialsProviderChain.getInstance();
    }
}
