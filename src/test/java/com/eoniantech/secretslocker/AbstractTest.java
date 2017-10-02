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
package com.eoniantech.secretslocker;

import org.junit.Rule;

/**
 * Abstract base class that adds a {@link Stopwatch} and provides constants.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public abstract class AbstractTest  {

    protected static final String LOCAL_LOCKER
            = System.getProperty("java.io.tmpdir");

    protected static final String SECRET_FILE_CONTENTS
            =  "This is secret text.";

    @Rule
    public Stopwatch stopwatch = new Stopwatch();
}
