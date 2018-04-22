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

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoInputStream;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.util.IOUtils;
import com.eoniantech.secretslocker.DecryptionService;
import com.eoniantech.secretslocker.DecryptionService.DecryptionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Implementation of {@link DecryptionService} that uses AWS KMS Multi-region
 * envelope decryption. AWS credentials are required. KMS keys with the same
 * alias must be created in each of the desired regions.
 *
 * @author Michael.Andrews@eoniantech.com
 * @since 1.0
 * @see DefaultAWSCredentialsProviderChain
 */
public final class KmsDecryptionService implements DecryptionService {

    /**
     * Singleton holder.
     */
    private static class Holder {
        public static final KmsDecryptionService INSTANCE
                = new KmsDecryptionService();
    }

    /**
     * Static factory method.
     *
     * @return The KmsDecryptionService singleton.
     */
    public static KmsDecryptionService instance() {
        return Holder.INSTANCE;
    }
  
    private AwsCrypto awsCrypto;
    private KmsMasterKeyProvider kmsMasterKeyProvider;

    /**
     * Private constructor.
     */
    private KmsDecryptionService() {
        setAwsCrypto();
        setKmsMasterKeyProvider();
    }

    private AwsCrypto awsCrypto() {
        return this.awsCrypto;
    }

    private KmsMasterKeyProvider kmsMasterKeyProvider() {
        return this.kmsMasterKeyProvider;
    }

    private void setAwsCrypto() {
        this.awsCrypto 
                = new AwsCrypto();
    }

    private void setKmsMasterKeyProvider() {
        Region region 
                = Regions.getCurrentRegion(); 

        String regionName 
                = (region == null) 
                        ? null 
                        : region.getName(); 

        this.kmsMasterKeyProvider 
                = KmsMasterKeyProvider
                        .builder()
                        .withDefaultRegion(
                                regionName)
                        .build();
    } 

    /**
     * {@inheritDoc }
     */
    @Override
    public void decryptFile(
            final String encryptedFilename, 
            final String decryptedFilename) {

        try (final FileInputStream fileInputStream
                = new FileInputStream(
                        encryptedFilename); 
                
                final FileOutputStream fileOutputStream
                        = new FileOutputStream(
                                decryptedFilename);

                final CryptoInputStream<?> decryptingStream
                        = awsCrypto()
                                .createDecryptingStream(
                                        kmsMasterKeyProvider(),
                                        fileInputStream)) {

            IOUtils.copy(
                    decryptingStream,
                    fileOutputStream);

        } catch (IOException exception) {
            throw new DecryptionException(exception);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void decryptFile(
            final File encryptedFile,
            final String decryptedFilename) {

        decryptFile(encryptedFile.getAbsolutePath(), decryptedFilename);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void decryptFile(
            final File encryptedFile,
            final File decryptedFile) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String decryptFile(
            final String encryptedFilename) {

        try (final FileInputStream fileInputStream
                = new FileInputStream(
                        encryptedFilename);

                final CryptoInputStream<?> decryptingStream
                        = awsCrypto()
                                .createDecryptingStream(
                                        kmsMasterKeyProvider(),
                                        fileInputStream)) {

            return IOUtils.toString(
                    decryptingStream);

        } catch (IOException exception) {
            throw new DecryptionException(exception);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String decryptFile(
            final File encryptedFile) {

        return decryptFile(encryptedFile.getAbsolutePath());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String decryptValue(
            final String encryptedValue) {

        throw new UnsupportedOperationException("Not supported yet.");
    }
}
