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

/**
 * An implementation of the {@link SecretsLocker} that requires each secret to 
 * exist in the specified local directory. The directory must exist and be 
 * readable.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class FileSystemSecretsLocker extends AbtractFileSystemSecretsLocker {

    /**
     * Constructor.
     * 
     * @param lockerPath The absolute path to the local locker. E.g., 
     * /foo/bar where bar is a readable directory.
     */
    public FileSystemSecretsLocker(final String lockerPath) { 
        super(lockerPath, true);
    }
}
