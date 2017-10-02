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

import static com.eoniantech.secretslocker.aws.Constants.CAN_NOT_BE_EMPTY_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.IS_REQUIRED_PATTERN;

/**
 * Class containing methods for argument assertions.
 *
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
abstract class Assertions {

    private Assertions() {
    }

    /**
     * Asserts that an argument is not {@code null}.
     * @param parameter The parameter.
     * @param argument The argument (value).
     */
    static void assertArgumentNotNull(
            final String parameter,
            final Object argument) {

        if (argument == null) {
            throw new IllegalArgumentException(
                    String.format(
                            IS_REQUIRED_PATTERN,
                            parameter));
        }
    }

    /**
     * Asserts that an argument is not {@code null} or empty.
     * @param parameter The parameter.
     * @param argument The argument (value).
     */
    static void assertArgumentNotEmpty(
            final String parameter,
            final String argument) {

        assertArgumentNotNull(
                parameter, 
                argument);

        if (argument.trim().isEmpty())
            throw new IllegalArgumentException(
                    String.format(
                            CAN_NOT_BE_EMPTY_PATTERN,
                            parameter));
    }

    /**
     * Asserts that an argument is not {@code null} or empty.
     * @param parameter The parameter.
     * @param argument The argument (value).
     */
    static void assertArgumentNotEmpty(
            final String parameter,
            final Object[] argument) {

        assertArgumentNotNull(
                parameter, 
                argument);

        if (argument.length == 0) {
            throw new IllegalArgumentException(
                    String.format(
                            CAN_NOT_BE_EMPTY_PATTERN,
                            parameter));
        }
    }
}
