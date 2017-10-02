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

import org.junit.runner.Description;

/**
 * Stopwatch implementation that logs the start, status, and time of every test.
 *
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class Stopwatch extends AbstractStopwatch {

    @Override
    protected void started(Description description) {
        System.out.println(
                String.format(
                        "RUNNING: %s",
                        description.getMethodName()));

        System.out.println();
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        System.out.println(
                String.format("FAILURE: %s (%f seconds)",
                        description.getMethodName(),
                        time()));

        System.out.println();
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        System.out.println(
                String.format(
                        "SUCCESS: %s (%f seconds)",
                        description.getMethodName(),
                        time()));

        System.out.println();
    }

    private double time() {
        return (double) getNanos() / 1000000000.0;
    }
}
