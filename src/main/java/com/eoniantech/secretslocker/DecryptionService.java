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
 * Interface for a decryption service. The exact details on how files and 
 * values are decrypted are implementation-specific.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public interface DecryptionService {

    /**
     * Decrypts a file.
     * 
     * @param encryptedFilename The absolute path to the file to decrypt.
     * @param decryptedFilename The absolute path to the decrypted file.
     */
    public void decryptFile(String encryptedFilename, String decryptedFilename);

    /**
     * Decrypts a file.
     * 
     * @param encryptedFile The {@link File} object to decrypt.
     * @param decryptedFilename The absolute path to the decrypted file.
     */
    public void decryptFile(File encryptedFile, String decryptedFilename);

    /**
     * Decrypts a file.
     * 
     * @param encryptedFile The {@link File} object to decrypt.
     * @param decryptedFile The decrypted {@link File} object.
     */ 
    public void decryptFile(File encryptedFile, File decryptedFile);
   
    /**
     * Decrypts a file.
     * 
     * @param encryptedFilename The absolute path to the file to decrypt.
     * @return The decrypted contents of the file.
     */
    public String decryptFile(String encryptedFilename);

    /**
     * Decrypts a file.
     * 
     * @param encryptedFile The absolute path to the file to decrypt.
     * @return The decrypted contents of the file.
     */
    public String decryptFile(File encryptedFile);

    /**
     * Decrypts an encrypted value.
     * 
     * @param encryptedValue The encrypted value.
     * @return The decrypted value.
     */
    public String decryptValue(String encryptedValue);

    /**
     * Exception indicating a problem while decrypting. It is available to 
     * implementations to use as needed and ONLY allows for the underlying 
     * cause to be added to he chain.
     * 
     * @author Michael Andrews | Michael.Andrews@eoniantech.com
     * @since 1.0
     */
    public static class DecryptionException extends RuntimeException {
        
        private static final long serialVersionUID = -6955141476076611344L;

        /**
         * Constructor.
         * 
         * @param cause The cause of the exception.
         */
        public DecryptionException(Throwable cause) {
            super(cause);
        }
    } 
}
