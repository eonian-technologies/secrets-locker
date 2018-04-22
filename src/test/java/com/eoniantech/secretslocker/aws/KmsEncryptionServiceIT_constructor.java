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
import com.eoniantech.secretslocker.EncryptionService;
import org.junit.Test;

/** 
 * Unit tests for the {@link KmsEncryptionService} constructor.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class KmsEncryptionServiceIT_constructor extends AbstractTest {

    @SuppressWarnings("unused")
    private EncryptionService encryptionService;
    
    @Test
    public void testKmsEncryptionService() {
        encryptionService
                = new KmsEncryptionService(
                        "alias/some-alias",
                        "us-east-1","us-west-2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKmsEncryptionService_null_key() {
        encryptionService
                = new KmsEncryptionService(
                        null,
                        "us-east-1","us-west-2");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testKmsEncryptionService_empty_key() {
        encryptionService
                = new KmsEncryptionService(
                        " ",
                        "us-east-1","us-west-2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKmsEncryptionService_null_regions() {
        encryptionService
                = new KmsEncryptionService(
                        "alias/some-alias",
                        (String[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKmsEncryptionService_empty_regions() {
        encryptionService
                = new KmsEncryptionService(
                        "alias/some-alias",
                        new String[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKmsEncryptionService_invalid_region() {
        encryptionService
                = new KmsEncryptionService(
                        "alias/some-alias",
                        new String[] {
                            "us-east-1",
                            "us-foo-2"
                        });
    }
}

     
