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

import com.eoniantech.secretslocker.SecretsLocker;
import com.eoniantech.secretslocker.SecretsLocker.SecretsLockerException;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotEmpty;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotNull;
import static com.eoniantech.secretslocker.aws.Constants.DIRECTORY_DOES_NOT_EXIST_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.DIRECTORY_IS_NOT_READABLE;
import static com.eoniantech.secretslocker.aws.Constants.FILE_DOES_NOT_EXIST_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.FILE_NAME;
import static com.eoniantech.secretslocker.aws.Constants.GET_AS_PROPERTIES_EXCEPTION;
import static com.eoniantech.secretslocker.aws.Constants.LOCKER_PATH;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import static com.eoniantech.secretslocker.aws.Constants.NAME;
import static com.eoniantech.secretslocker.aws.Constants.SECRET_NOT_FOUND;

/**
 * Abstract base class for file system backed {@link SecretsLocker} 
 * implementations.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 * @see ClassPathSecretsLocker
 * @see FileSystemSecretsLocker
 * @see S3SecretsLocker
 */
abstract class AbtractFileSystemSecretsLocker implements SecretsLocker {

    private File locker;
    private final boolean validateFiles;
    private final Map<String, File> secrets;

    /**
     * Constructor.
     * 
     * @param lockerPath The absolute path to the local locker. E.g., 
     * /foo/bar where bar is a readable directory that contains encrypted files.
     * @param validateFiles When adding secrets, validate that the encrypted 
     * file exists in the lockerPath. See {@link #add} for details.
     */
    AbtractFileSystemSecretsLocker(
            final String lockerPath,
            final boolean validateFiles) { 

       setLocker(lockerPath); 

       this.secrets = new HashMap<>();
       this.validateFiles = validateFiles;
    }
    
    @Override
    public void add(
            final String name, 
            final String fileName) {

        assertArgumentNotEmpty(
                NAME, 
                name);
        
        assertArgumentNotEmpty(
                FILE_NAME, 
                fileName);

        File file 
                = new File(
                        locker(), 
                        fileName);

        if (validateFiles())
            validateFileExist(file);

        secrets().put(name, file);
    }

    @Override
    public boolean contains(final String name) {
        assertArgumentNotEmpty(
                NAME, 
                name); 
        
        return secrets().containsKey(name) 
                && secrets().get(name).exists();
    }

    @Override
    public String get(final String name) {
        assertArgumentNotEmpty(
                NAME, 
                name); 

        if (!contains(name))
            throw new SecretsLockerException(
                    SECRET_NOT_FOUND);

        return decrypt(
                secrets().get(
                        name));
    }

    @Override
    public Properties getAsProperties(final String name) {
        String value = get(name); 

        if (value == null)
            throw new SecretsLockerException(
                    SECRET_NOT_FOUND);

        Properties secretProperties
                = new Properties();

        try(InputStream inputStream
                = new ByteArrayInputStream(
                        value.getBytes())) {

            secretProperties.load(
                    inputStream);

            return secretProperties;

        } catch (IOException ioEx) {
            throw new SecretsLockerException(
                    GET_AS_PROPERTIES_EXCEPTION, 
                    ioEx); 
        }
    } 
   
    /**
     * Decrypts the file.
     * 
     * @param file The file to decrypt.
     * @return The decrypted file contents as a Java String.
     */
    protected String decrypt(final File file) {
        return KmsDecryptionService
                .instance()
                .decryptFile(
                        file);
    }

    /**
     * Validates the the lockerPath is a readable directory and returns the
     * lockerPath as a Java File object.
     * 
     * @param lockerPath The absolute path to the local locker.
     * @return The localPath as a Java File object.
     */
    protected File validateLockerPath(final String lockerPath) {
        assertArgumentNotNull(
                LOCKER_PATH, 
                lockerPath);

        File directory
                = new File(lockerPath);

        if (!directory.exists() 
                || !directory.isDirectory())

            throw new IllegalArgumentException(
                    String.format(
                            DIRECTORY_DOES_NOT_EXIST_PATTERN, 
                            lockerPath));

        if (!directory.canRead())
            throw new IllegalArgumentException(
                    String.format(
                            DIRECTORY_IS_NOT_READABLE, 
                            lockerPath));

        return directory;
    }

    /**
     * @return The locker File object.
     */
    protected File locker() {
        return locker;
    }

    /**
     * @return The secrets Map.
     */
    protected Map<String, File> secrets() {
        return secrets;
    }

    /**
     * @return {@code true} if files should be validated, otherwise 
     * {@code false).
     */
    protected boolean validateFiles() {
        return validateFiles; 
    }

    /**
     * Validates that the given file exists.
     * 
     * @param file The file to validate.
     */
    protected void validateFileExist(final File file) {
        if (!file.exists())
            throw new IllegalArgumentException(
                    String.format(
                            FILE_DOES_NOT_EXIST_PATTERN,
                            file.getAbsolutePath()));
    }

    private void setLocker(final String lockerPath) {
        this.locker = validateLockerPath(lockerPath);
    } 
}
