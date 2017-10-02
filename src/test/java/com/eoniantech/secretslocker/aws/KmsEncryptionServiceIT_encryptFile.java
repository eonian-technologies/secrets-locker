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
import static com.eoniantech.secretslocker.aws.KmsEncryptionServiceIT.KMS_KEY_ALIAS;
import static com.eoniantech.secretslocker.aws.KmsEncryptionServiceIT.KMS_KEY_REGIONS;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Integration tests for the {@link KmsEncryptionService} encryptFile method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class KmsEncryptionServiceIT_encryptFile extends AbstractTest {

    private static KmsEncryptionService encryptionService;
    private static File plainTextFile;
    private static String plainTextFilename;
    private static String decryptedFilename;

    @AfterClass
    public static final void afterClass() {
        plainTextFile.delete();
    }

    @BeforeClass
    public static final void beforeClass() throws IOException {
        plainTextFile
                = File.createTempFile(
                        "secret", 
                        ".txt");

        try (BufferedWriter bw
                = new BufferedWriter(
                        new FileWriter(plainTextFile))) {

            bw.write(
                    SECRET_FILE_CONTENTS);
        }

        plainTextFile
                .deleteOnExit();

        plainTextFilename
                = plainTextFile
                        .getAbsolutePath();

        decryptedFilename
                = plainTextFilename + ".decrypted";

        encryptionService 
                = new KmsEncryptionService(
                        KMS_KEY_ALIAS,
                        KMS_KEY_REGIONS);
    }

    @Test
    public void testEncryptFile_filename() throws IOException {

        File encryptedFile
                = new File(
                        encryptionService
                                .encryptFile(
                                        plainTextFilename));

        encryptedFile
                .deleteOnExit();

        KmsDecryptionService
                .instance()
                .decryptFile(
                        encryptedFile,
                        decryptedFilename);

        assertEquals(
                SECRET_FILE_CONTENTS,
                new String(
                        Files.readAllBytes(
                                Paths.get(
                                        decryptedFilename))));
    }

    @Test
    public void testEncryptFile_file() throws IOException {

        File encryptedFile
                = encryptionService
                        .encryptFile(
                                plainTextFile);

        encryptedFile
                .deleteOnExit();

        KmsDecryptionService
                .instance()
                .decryptFile(
                        encryptedFile,
                        decryptedFilename);

        File decryptedFile
                = new File(decryptedFilename);

        decryptedFile
                .deleteOnExit();

        assertEquals(
                SECRET_FILE_CONTENTS,
                new String(
                        Files.readAllBytes(
                                Paths.get(
                                        decryptedFilename))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncryptFile_file_does_not_exists() throws IOException {
        encryptionService.encryptFile(new File("/foo/bar/DoesNotExists")); 
    }

    // @Test(expected = IllegalArgumentException.class)
    public void testEncryptFile_file_is_not_readable() throws IOException {
        File tmp = File.createTempFile("somefile", ".tmp");
        tmp.deleteOnExit();
        tmp.setReadable(false, false);
        encryptionService.encryptFile(tmp);
    }

    // @Test(expected = IllegalArgumentException.class)
    public void testEncryptFile_parent_is_not_writable() throws IOException {
        File tmpDir = new File(
                new StringBuilder()
                        .append( System.getProperty("java.io.tmpdir"))
                        .append(File.separator)
                        .append("tempdir")
                        .toString());

        tmpDir.mkdir();
        tmpDir.deleteOnExit();

        File tmpFile = new File(tmpDir, "tmpFile.txt");
        tmpFile.createNewFile();
        tmpFile.setReadable(true, true);
        tmpFile.deleteOnExit(); 
        tmpFile.getParentFile().setWritable(false, false);

        encryptionService.encryptFile(tmpFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncryptFile_parent_not_regualar_file() throws IOException {
        File tmpDir = new File(
                new StringBuilder()
                        .append( System.getProperty("java.io.tmpdir"))
                        .append(File.separator)
                        .append("tempdir2")
                        .toString());

        tmpDir.mkdir();
        tmpDir.deleteOnExit();

        encryptionService.encryptFile(tmpDir);
    }
}
