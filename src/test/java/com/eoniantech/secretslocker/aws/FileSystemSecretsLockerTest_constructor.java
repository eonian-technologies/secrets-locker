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
import com.eoniantech.secretslocker.SecretsLocker;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@link FileSystemSecretsLocker} constructor.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class FileSystemSecretsLockerTest_constructor extends AbstractTest {

    private static String secretsLockerPath;

    @SuppressWarnings("unused")
    private SecretsLocker secretsLocker;

    @BeforeClass
    public static final void beforeClass() 
            throws IOException, URISyntaxException {
        
        ClassLoader loader 
                = FileSystemSecretsLockerTest_constructor.class
                        .getClassLoader();

        URL fileUrl
                = loader.getResource(
                        "secret.txt.encrypted");

        File encryptedFile
                = new File(
                        fileUrl.toURI());

        secretsLockerPath 
                = encryptedFile
                        .getParent();
    }

    @Test
    public void testFileSystemSecretsLocker() {
        secretsLocker = new FileSystemSecretsLocker(secretsLockerPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemSecretsLocker_invlid() {
        secretsLocker = new FileSystemSecretsLocker("/dir/does/not/exist");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemSecretsLocker_notReadable() {
        File tmpdir = new File(System.getProperty("java.io.tmpdir") + "/foo");

        tmpdir.deleteOnExit();
        tmpdir.mkdir();
        tmpdir.setReadable(false);

        secretsLocker = new FileSystemSecretsLocker(tmpdir.getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemSecretsLocker_null() {
        secretsLocker = new FileSystemSecretsLocker(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemSecretsLocker_empty() {
        secretsLocker = new FileSystemSecretsLocker("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemSecretsLocker_emptyWithSpaces() {
        secretsLocker = new FileSystemSecretsLocker("    ");
    }
}
