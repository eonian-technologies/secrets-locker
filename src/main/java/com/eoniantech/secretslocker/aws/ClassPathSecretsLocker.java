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

import com.eoniantech.secretslocker.SecretsLocker;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotEmpty;
import static com.eoniantech.secretslocker.aws.Constants.EMPTY;
import static com.eoniantech.secretslocker.aws.Constants.FILE_DOES_NOT_EXIST_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.FILE_NAME;
import static com.eoniantech.secretslocker.aws.Constants.NAME;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Implementation of {@link SecretsLocker} that requires each secret to exist 
 * on the class path.
 * 
 * @author Michael.Andrews@eoniantech.com
 * @since 1.0
 */
public final class ClassPathSecretsLocker 
        extends AbtractFileSystemSecretsLocker {

    /**
     * Constructor.
     */
    public ClassPathSecretsLocker() { 
        super(null, true);
    }

    @Override
    protected File validateLockerPath(
            final String lockerPath) {
        
        // this implementaion does not use a lockerPath
        return new File(EMPTY);   
    }

    @Override
    public void add(
            final String name, 
            final String fileName) {
        
        assertArgumentNotEmpty(
                NAME, 
                name);
        
        assertArgumentNotEmpty(
                FILE_NAME, 
                fileName);

        try {
            ClassLoader classLoader
                = ClassPathSecretsLocker.class
                        .getClassLoader();

            URL fileUrl
                = classLoader
                        .getResource(
                                fileName);

            File file
                    = new File(
                            fileUrl.toURI()); 

            secrets().put(name, file);

        } catch (SecurityException 
                | URISyntaxException 
                | NullPointerException exception) {

            throw new IllegalArgumentException(
                    String.format(
                            FILE_DOES_NOT_EXIST_PATTERN, 
                            fileName),
                    exception);
        }
    }
}