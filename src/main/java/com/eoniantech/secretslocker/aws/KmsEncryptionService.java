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
import com.amazonaws.encryptionsdk.CryptoOutputStream;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.encryptionsdk.multi.MultipleProviderFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityResult;
import com.amazonaws.util.IOUtils;
import com.eoniantech.secretslocker.EncryptionService;
import com.eoniantech.secretslocker.EncryptionService.EncryptionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotEmpty;
import static com.eoniantech.secretslocker.aws.Assertions.assertArgumentNotNull;
import static com.eoniantech.secretslocker.aws.Constants.ALIAS;
import static com.eoniantech.secretslocker.aws.Constants.DIRECTORY_IS_NOT_READABLE;
import static com.eoniantech.secretslocker.aws.Constants.FILE_DOES_NOT_EXIST_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.FILE_IS_NOT_A_NORMAL_FILE_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.FILE_IS_NOT_READABLE_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.INVALID_REGION_PATTERN;
import static com.eoniantech.secretslocker.aws.Constants.REGION;
import static com.eoniantech.secretslocker.aws.Constants.REGIONS;
import static com.eoniantech.secretslocker.aws.Constants.SUFFIX;

/**
 * Implementation of {@link EncryptionService} that uses AWS KMS Multi-region
 * envelope encryption. AWS credentials are required. KMS keys with the same
 * alias must be created in each of the desired regions.
 *
 * @author Michael.Andrews@eoniantech.com
 * @since 1.0
 * @see DefaultAWSCredentialsProviderChain
 */
public final class KmsEncryptionService implements EncryptionService {

    private static final String ALIAS_ARN_FORMAT 
            = "arn:aws:kms:%s:%s:%s"; 

    private String alias;
    private String[] regions;
    private String account;

    /**
     * Constructor.
     * *
     * @param alias The alias that exists in all the regions. E.g., alias/myKey
     * @param regions The regions where the alias exists.
     */
    public KmsEncryptionService(
            final String alias,
            final String... regions) {

        setAlias(alias);
        setRegions(regions);
        setAccountId();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String encryptFile(final String filename) {
        return encryptFile(new File(filename)).getAbsolutePath();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public File encryptFile(
            final File file) {
        
        if (!file.exists())
            throw new IllegalArgumentException(
                    String.format(
                            FILE_DOES_NOT_EXIST_PATTERN,
                            file.getAbsolutePath()));

        if (!file.isFile())
            throw new IllegalArgumentException(
                    String.format(
                            FILE_IS_NOT_A_NORMAL_FILE_PATTERN,
                            file.getAbsolutePath()));

        if (!file.canRead())
            throw new IllegalArgumentException(
                    String.format(
                            FILE_IS_NOT_READABLE_PATTERN,
                            file.getAbsolutePath()));

        if (!file.getParentFile().canWrite()) 
            throw new IllegalArgumentException(
                    String.format(
                            DIRECTORY_IS_NOT_READABLE,
                            file.getParentFile().getAbsolutePath()));

        File encryptedFile
                = new File(
                        new StringBuilder()
                                .append(file.getAbsoluteFile())
                                .append(SUFFIX)
                                .toString());

        final AwsCrypto awsCrypto
                = new AwsCrypto();

        try (final FileInputStream fileInputStream
                = new FileInputStream(file);

                final FileOutputStream fileOutputStram 
                    = new FileOutputStream(
                        encryptedFile);

                final CryptoOutputStream<?> encryptingStream
                        = awsCrypto
                                .createEncryptingStream(
                                        masterKeyProvider(),
                                       fileOutputStram)) {

            IOUtils.copy(
                    fileInputStream,
                    encryptingStream);

        } catch (Exception exception) {
            throw new EncryptionException(exception);
        }

        return encryptedFile;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String encryptValue(
            final String value) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private MasterKeyProvider<?> masterKeyProvider() {

        List<KmsMasterKeyProvider> kmsMasterKeyProviders 
                = new LinkedList<>();

        for (String region : regions()) { 
            kmsMasterKeyProviders.add(
                    KmsMasterKeyProvider
                            .builder() 
                            .withKeysForEncryption(
                                    String.format(
                                            ALIAS_ARN_FORMAT, 
                                            region,
                                            account(),
                                            alias()))
                            .build());
        } 

        return MultipleProviderFactory
                .buildMultiProvider(
                        kmsMasterKeyProviders);
    }

    private void setAlias(
            final String alias) {

        assertArgumentNotEmpty(
                ALIAS,
                alias);

        this.alias 
                = alias;
    }

    private void setRegions(
            final String... regions) {

        assertArgumentNotEmpty(
                REGIONS,
                regions);

        for (String region : regions)
            validateRegion(region);

        this.regions 
                = regions;
    }

    private void validateRegion(
            final String region) {

        assertArgumentNotNull(
                REGION,
                region);

        try {
            Regions.fromName(region);

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    String.format(
                            INVALID_REGION_PATTERN,
                            region),
                    exception);
        }
    }

    private void setAccountId() {
        AWSSecurityTokenService stsService 
                = AWSSecurityTokenServiceClientBuilder
                        .standard()
                        .withCredentials(
                                new DefaultAWSCredentialsProviderChain())
                        .build();

        GetCallerIdentityResult callerIdentity 
                = stsService
                        .getCallerIdentity(
                                new GetCallerIdentityRequest());

        this.account 
                = callerIdentity
                        .getAccount();
    }

    private String alias() {
        return alias;
    }

    private String[] regions() {
        return regions;
    }

    private String account() {
        return account;
    }
}
