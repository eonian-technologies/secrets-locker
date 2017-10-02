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

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;

/**
 * Abstract Stopwatch implementation that provides no-op methods
 * and convenience methods to sub-classes.
 *
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public abstract class AbstractStopwatch implements TestRule {

    private final Clock clock;
    private volatile long startNanos;
    private volatile long endNanos;

    public AbstractStopwatch() {
        this(new Clock());
    }

    AbstractStopwatch(Clock clock) {
        this.clock = clock;
    }

    /**
     * Gets the runtime for the test.
     *
     * @param unit time unit for returned runtime
     * @return runtime measured during the test
     */
    public long runtime(TimeUnit unit) {
        return unit.convert(getNanos(), TimeUnit.NANOSECONDS);
    }

    /**
     * Invoked when a test starts
     *
     * @param description
     */
    protected void started(Description description) {
    }

    /**
     * Invoked when a test succeeds
     *
     * @param nanos
     * @param description
     */
    protected void succeeded(long nanos, Description description) {
    }

    /**
     * Invoked when a test fails
     *
     * @param nanos
     * @param e
     * @param description
     */
    protected void failed(long nanos, Throwable e, Description description) {
    }

    /**
     * Invoked when a test is skipped due to a failed assumption.
     *
     * @param nanos
     * @param e
     * @param description
     */
    protected void skipped(
            long nanos,
            AssumptionViolatedException e,
            Description description) {
    }

    /**
     * Invoked when a test method finishes (whether passing or failing)
     *
     * @param nanos
     * @param description
     */
    protected void finished(long nanos, Description description) {
    }

    protected long getNanos() {
        if (startNanos == 0) {
            throw new IllegalStateException("Test has not started");
        }
        long currentEndNanos = endNanos; // volatile read happens here
        if (currentEndNanos == 0) {
            currentEndNanos = clock.nanoTime();
        }

        return currentEndNanos - startNanos;
    }

    private void starting() {
        startNanos = clock.nanoTime();
        endNanos = 0;
    }

    private void stopping() {
        endNanos = clock.nanoTime();
    }

    @Override
    public final Statement apply(Statement base, Description description) {
        return new InternalWatcher().apply(base, description);
    }

    private class InternalWatcher extends TestWatcher {

        @Override
        protected void starting(Description description) {
            AbstractStopwatch.this.starting();
            AbstractStopwatch.this.started(description);
        }

        @Override
        protected void finished(Description description) {
            AbstractStopwatch.this.finished(getNanos(), description);
        }

        @Override
        protected void succeeded(Description description) {
            AbstractStopwatch.this.stopping();
            AbstractStopwatch.this.succeeded(getNanos(), description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            AbstractStopwatch.this.stopping();
            AbstractStopwatch.this.failed(getNanos(), e, description);
        }

        @Override
        protected void skipped(
                AssumptionViolatedException e,
                Description description) {

            AbstractStopwatch.this.stopping();
            AbstractStopwatch.this.skipped(getNanos(), e, description);
        }
    }

    static class Clock {

        public long nanoTime() {
            return System.nanoTime();
        }
    }
}
