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
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Integration tests for the {@link KmsDecryptionService} decrypt method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class KmsDecryptionServiceIT_decryptFile extends AbstractTest {
    
    private static KmsDecryptionService decryptionService;
    private static File encryptedFile;
    private static String encryptedFilePath;

    @BeforeClass
    public static final void beforeClass() throws URISyntaxException {
        decryptionService
                = KmsDecryptionService
                        .instance();

        ClassLoader classLoader 
                = FileSystemSecretsLockerIT_get.class
                        .getClassLoader();

        URL encryptedFileUrl
                = classLoader
                        .getResource(
                                "secret.txt.encrypted");

        encryptedFile
                = new File(
                        encryptedFileUrl
                                .toURI());

        encryptedFilePath
                = encryptedFile
                        .getAbsolutePath();
    }

    @Test
    public void testDecryptFile_encryptedFilename() {

        assertEquals(
                SECRET_FILE_CONTENTS, 
                decryptionService
                        .decryptFile(
                                encryptedFilePath));
    }
}
