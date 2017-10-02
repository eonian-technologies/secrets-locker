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
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Integration tests for the {@link FileSystemSecretsLocker} get method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class FileSystemSecretsLockerIT_get extends AbstractTest {

    private static FileSystemSecretsLocker secretsLocker; 

    @BeforeClass
    public static final void beforeClass() 
            throws IOException, URISyntaxException {
        
        ClassLoader classLoader 
                = FileSystemSecretsLockerIT_get.class
                        .getClassLoader();

        URL encryptedfFileUrl 
                = classLoader
                        .getResource(
                                "secret.txt.encrypted");

        File encryptedFile 
                = new File(
                        encryptedfFileUrl
                                .toURI()); 
    
        secretsLocker 
                = new FileSystemSecretsLocker(
                        encryptedFile
                                .getParent());

        secretsLocker
                .add("MySecret", 
                        "secret.txt.encrypted");
    }

    @Test
    public void testGet() {
        assertEquals(
                SECRET_FILE_CONTENTS, 
                secretsLocker.get("MySecret"));
    }

    @Test(expected = SecretsLockerException.class)
    public void testGet_doesNotExist() { 
        assertNull(secretsLocker.get("DoesNotExists"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_null() {
        secretsLocker.get(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGet_empty() {
        secretsLocker.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_emptyWithSpaces() {
        secretsLocker.get("    ");
    }
}
