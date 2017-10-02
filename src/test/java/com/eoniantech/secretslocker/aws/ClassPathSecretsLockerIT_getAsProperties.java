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
import com.eoniantech.secretslocker.SecretsLocker.SecretsLockerException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;

/**
 * Integration tests for the {@link ClassPathSecretsLocker} get method.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class ClassPathSecretsLockerIT_getAsProperties extends AbstractTest {

    private static SecretsLocker secretsLocker;

    @BeforeClass
    public static final void beforeClass() {
        secretsLocker = new ClassPathSecretsLocker(); 
        secretsLocker.add( "MySecret", "secret.properties.encrypted");
    }

    @Test
    public void testGetAsProperties() {
        assertEquals("value", 
                secretsLocker.getAsProperties("MySecret")
                        .getProperty( "property"));
    }

    @Test(expected = SecretsLockerException.class)
    public void testGet_doesNotExist() {
        assertNull(secretsLocker.getAsProperties("DoesNotExist"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_null() {
        secretsLocker.getAsProperties(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGet_empty() {
        secretsLocker.getAsProperties("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_emptyWithSpaces() {
        secretsLocker.getAsProperties("    ");
    }
}
