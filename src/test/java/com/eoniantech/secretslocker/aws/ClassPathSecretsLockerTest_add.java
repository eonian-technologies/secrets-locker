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
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@link ClassPathSecretsLocker} constructor.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class ClassPathSecretsLockerTest_add extends AbstractTest {

    private static SecretsLocker secretsLocker;

    @BeforeClass
    public static final void beforeClass() {
        secretsLocker = new ClassPathSecretsLocker();
    }

    @Test
    public void testAdd() {
        secretsLocker.add("MySecret", "secret.txt.encrypted");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_invalidFilename() {
        secretsLocker.add("MySecret", "doesNotExists.txt.encrypted");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_nullName() {
        secretsLocker.add(null, "secret.txt.encrypted");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_emptyName() {
        secretsLocker.add("", "secret.txt.encrypted");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_emptyNameWithSpaces() {
        secretsLocker.add("   ", "secret.txt.encrypted");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_nullFileName() {
        secretsLocker.add("MySecret", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_emptyFileName() {
        secretsLocker.add("MySecret", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_emptyFileNameWithSpace() {
        secretsLocker.add("MySecret", "    ");
    } 
}
