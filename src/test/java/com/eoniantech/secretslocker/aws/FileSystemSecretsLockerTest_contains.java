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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Unit tests for the {@link FileSystemSecretsLocker} contains method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class FileSystemSecretsLockerTest_contains extends AbstractTest {
    
    private static FileSystemSecretsLocker secretsLocker; 

    @BeforeClass
    public static final void beforeClass() 
            throws IOException, URISyntaxException {
        
        ClassLoader loader 
                = FileSystemSecretsLockerIT_get.class
                        .getClassLoader();

        URL fileUrl
                = loader.getResource(
                        "secret.txt.encrypted");

        File encryptedFile
                = new File(
                        fileUrl.toURI()); 
    
        secretsLocker
                = new FileSystemSecretsLocker(
                        encryptedFile
                                .getParent());

        secretsLocker
                .add("MySecret", "secret.txt.encrypted"); 
    }

    @Test
    public void testContains_true() {
        assertTrue(secretsLocker.contains("MySecret"));
    }

    @Test
    public void testContains_false() {
        assertFalse(secretsLocker.contains("DoesNotExist"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContains_null() {
        assertFalse(secretsLocker.contains(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContains_empty() {
        assertFalse(secretsLocker.contains(""));
    } 

    @Test(expected = IllegalArgumentException.class)
    public void testContains_emptyWithSpaces() {
        assertFalse(secretsLocker.contains("    "));
    }
}
