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
import org.junit.Test;

/** 
 * Unit tests for the {@link S3SecretsLocker} constructor.
 * 
 * @author Michael Andrews <Michael.Andrews@eoniantech.com>
 * @since 1.0
 */
public class S3SecretsLockerTest_constructor extends AbstractTest {

    @SuppressWarnings("unused")
    private SecretsLocker secretsLocker;

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_nullBucketName() {
         secretsLocker
                = new S3SecretsLocker(
                        null, 
                        "path",
                        LOCAL_LOCKER,
                        false);
    } 

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyBucketName() {
         secretsLocker
                = new S3SecretsLocker(
                        "", 
                        "path",
                        LOCAL_LOCKER,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyBucketNameWithSpaces() {
        secretsLocker
                = new S3SecretsLocker(
                        "    ", 
                        "path",
                        LOCAL_LOCKER,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_nullBucketPath() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        null,
                        LOCAL_LOCKER,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyBucketPath() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "",
                        LOCAL_LOCKER,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyBucketPathWithSpaces() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "    ",
                        LOCAL_LOCKER,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_nullLocalLockerPath() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "path",
                        null,
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyLocalLockerPath() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "path",
                        "",
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_emptyLocalLockerPathWithSpaces() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "path",
                        "    ",
                        false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testS3SecretsLocker_invalidLocalLocker() {
        secretsLocker
                = new S3SecretsLocker(
                        "bucket",
                        "path",
                        "/foo/bar",
                        false);
    }
}    
