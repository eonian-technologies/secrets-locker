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

/**
 * Interface containing constants. Do not implement this class. Use static 
 * imports instead.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
interface Constants {

    // General
    static final String COLON = ":";
    static final String SUFFIX = ".encrypted";
    static final String EMPTY = "";

    // Paramter names
    static final String SECRET = "secret";
    static final String SECRET_FILES = "secretFiles";
    static final String SECRET_FILE = "secretFile";
    static final String SECRETS = "secrets";
    static final String NAME = "name";
    static final String NAMES = "names";
    static final String LOCKER_PATH = "lockerPath";
    static final String ACCOUNT_ID = "accountId";
    static final String ALIAS = "alias";
    static final String REGIONS = "regions";
    static final String REGION = "region";
    static final String FILE_NAME = "fileName";
    static final String BUCKET_NAME = "bucketName";
    static final String BUCKET_PATH = "bucketPath";

    // Message Patters
    static final String FILE_DOES_NOT_EXIST_PATTERN 
            = "file does not exists: %s";
    static final String FILE_IS_NOT_READABLE_PATTERN 
            = "file is not readable: %s";
    static final String FILE_IS_NOT_A_NORMAL_FILE_PATTERN 
            = "file is not a normal file: %s";
    static final String DIRECTORY_DOES_NOT_EXIST_PATTERN
            = "directory does not exist: %s";
    static final String DIRECTORY_IS_NOT_READABLE
            = "directory is not readable: %s";
    static final String NAME_NOT_IN_LOCKER_PATTERN
            = "the secret is not in the locker: %s"; 
    static final String LOCKER_PATH_NOT_WRITEABLE
            = "the locker path is tno writeable: %s";
    static final String IS_REQUIRED_PATTERN
            = "%s is required";
    static final String CAN_NOT_BE_EMPTY_PATTERN
            = "%s can not be empty";

    // AWS
    static final String S3_BUCKET_DOES_NOT_EXIST_PATTERN
            = "the bucket does not exists: %s";
     static final String INVALID_REGION_PATTERN
            = "invlid region: %s"; 
     static final String S3_PATH_SEPARATOR = "/";

    // Error Messages
    static final String GET_AS_PROPERTIES_EXCEPTION
            = "could not create properties from decrypted secret";
    static final String SECRET_NOT_FOUND
            = "The secret was not found in the locker.";
}
