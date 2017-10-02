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

import java.util.Properties;

/**
 * Interface for a secrets locker. Lockers retrieve secrets by name. The exact
 * details of adding encrypted secrets to the locker and retrieving their
 * decrypted value is implementation-specific.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public interface SecretsLocker {

    /**
     * Adds a secret to the locker.
     * 
     * @param name The name of the secret. E.g., MySecret
     * @param fileName The name of the encrypted file. E.g., 
     * MySecret.txt.encrypted. Do not use the absolute path.
     */
    public void add(final String name, final String fileName);

    /**
     * Checks if a secret is in the locker.
     * 
     * @param name The name of the secret.
     * @return {@code true} if the named secret exists otherwise {@code false}.
     */
    public boolean contains(String name);

    /**
     * Gets a decrypted secret from the locker.
     * 
     * @param name The name of the secret to get.
     * @return The secret's plain-text.
     */
    public String get(String name);

    /**
     * Gets a decrypted secret as a Java {@link Properties} object.
     * @param name The name of the secret.
     * @return The secret's plain-text as Java {@link Properties}.
     */
    public Properties getAsProperties(String name);

    /**
     * A class representing a locker exception. How this exception is used will
     * vary. It is available to implementations to use as needed.
     * 
     * @author Michael Andrews | Michael.Andrews@eoniantech.com
     * @since 1.0
     */
    public static class SecretsLockerException extends RuntimeException {

        private static final long serialVersionUID = -1094055630821861458L;

        /**
         * Constructor.
         * 
         * @param message The message describing the exception.
         */
        public SecretsLockerException(String message) {
            super(message);
        }

        /**
         * Constructor.
         * 
         * @param message The message describing the exception.
         * @param cause  The cause of the exception.
         */
        public SecretsLockerException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructor.  
         * 
         * @param cause The cause of the exception.
         */
        public SecretsLockerException(Throwable cause) {
            super(cause);
        }
    }
}
