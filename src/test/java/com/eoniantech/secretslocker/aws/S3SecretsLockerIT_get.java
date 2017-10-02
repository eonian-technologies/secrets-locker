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

import com.eoniantech.secretslocker.AbstractTest;
import com.eoniantech.secretslocker.SecretsLocker.SecretsLockerException;
import static com.eoniantech.secretslocker.aws.S3SecretsLockerIT.S3_BUCKET_NAME;
import static com.eoniantech.secretslocker.aws.S3SecretsLockerIT.S3_BUCKET_PATH;
import static com.eoniantech.secretslocker.aws.S3SecretsLockerIT.S3_ENCRYPTED_FILE;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration tests for the {@link S3SecretsLocker} get method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class S3SecretsLockerIT_get extends AbstractTest {

    private static S3SecretsLocker secretsLocker;

    @BeforeClass
    public static void beforeClass() {
        secretsLocker 
                = new S3SecretsLocker(
                        S3_BUCKET_NAME,
                        S3_BUCKET_PATH,
                        LOCAL_LOCKER);

        secretsLocker
                .add("MySecret", S3_ENCRYPTED_FILE); 
    }

    @Test
    public void testGet() {
        assertEquals(
                SECRET_FILE_CONTENTS, 
                secretsLocker
                        .get("MySecret"));
    }

    @Test(expected = SecretsLockerException.class)
    public void testGet_doesNotExist() {
        secretsLocker
                .add("MySecretThatDoesNotExist", "DoesNotExistsInS3");

        secretsLocker
                .get("MySecretThatDoesNotExist");
    } 
}    
