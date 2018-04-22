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

import java.io.File;

/**
 * Interface for an encryption service. The exact details on how files and 
 * values are encrypted are implementation-specific.
 * 
 * @author Michael.Andrews@eoniantech.com
 * @since 1.0
 */
public interface EncryptionService {

    /**
     * Encrypts a file.
     * 
     * @param filename The absolute path to the file to encrypt.
     * @return the absolute path to the encrypted file.
     * 
     */
    public String encryptFile(String filename);

    /**
     * Encrypts a file.
     * 
     * @param file The File object to encrypt.
     * @return the encrypted File object.
     */
    public File encryptFile(File file);

    /**
     * Encrypts a value.
     * 
     * @param value The value to encrypt.
     * @return The encrypted value.
     */
    public String encryptValue(String value);

    /**
     * Exception indicating a problem while encrypting. It is available to 
     * implementations to use as needed and ONLY allows for the underlying 
     * cause to be added to he chain.
     * 
     * @author Michael Andrews | Michael.Andrews@eoniantech.com
     * @since 1.0
     */
    public static class EncryptionException extends RuntimeException {

        private static final long serialVersionUID = -4764225434232196564L;

        /**
         * Constructor.
         * 
         * @param cause The cause of the exception.
         */
        public EncryptionException(Throwable cause) {
            super(cause);
        }
    }
}