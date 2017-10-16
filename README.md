## Eonian Secrets Locker
The Eonian Secrets Locker provides in-app decryption of encrypted secrets at runtime. Encrypted secrets can be on the class path, the file system, or in AWS S3. Secrets are decrypted into Java Strings or Properties. Their plaintext is not written to disk. The library provides interfaces which can be implemented by different encryption providers. Currently the only provider is AWS KMS via the AWS Encryption SDK.


### Maven Dependency
```
<dependency>
    <groupId>com.eoniantech</groupId>
    <artifactId>secrets-locker</artifactId>
    <version>1.1</version>
</dependency>
```

### Using The AWS KMS Provider
[The AWS Encryption SDK](https://github.com/awslabs/aws-encryption-sdk-java) provides a framework and [message format]( http://docs.aws.amazon.com/encryption-sdk/latest/developer-guide/message-format.html) for envelope encryption. Envelope encryption is used to encrypt a file using a KMS data key. That data key is then encrypted with regional KMS Customer Master Keys. Each regionally encrypted data key is then stored in the encrypted message. When decrypting, the appropriate regional CMK is used to decrypt the data key, and the data key is then used to decrypt the file. In other words, encrypt once - decrypt from anywhere.

#### Encrypting Secrets
The [mrcrypt](https://github.com/aol/mrcrypt) command-line tool encrypts secrets which conform to the AWS Encryption SDK's message format. In the following example, KMS CMKs with the alias `mykey` exist in `us-east-1`, `us-west-2` and `eu-west-1` - and the file called `secret.txt` contains the plaintext that needs to be encrypted. When executing this command, AWS credentials with permission to encrypt using each of the KMS CMKs must be found in the AWS credentials chain (environment variables, user home directory, instance profile, etc).
```
$ mrcrypt encrypt -r us-east-1 us-west-2 eu-west-1 -- alias/mykey secret.txt
```
Because `mrcrypt` follows the AWS Encryption SDK's message format, the resulting file called `secret.txt.encrypted` can be decrypted by the Eonian Secrets Locker from each of the regions specified. As when encrypting, AWS credentials with permission to decrypt using each of the KMS CMK must be found in the credentials chain. You can not encrypt for three regions, and then decrypt using credentials that only have permission for one of the regions. To decrypt the file, you have to have rights to decrypt from all three regions.

#### Decrypting Secrets
There are several types of in-app lockers. Choose the Secrets Locker that best fits your use case.

**Class Path Secrets Locker**</br>
Encrypted secrets must exist on the class path. Existence checks are made when secrets are added.
```
// Create the locker.
SecretsLocker secretsLocker = new ClassPathSecretsLocker();
  
// Add encrypted secrets.
secretsLocker.add("SecretText", "secret.txt.encrypted");
secretsLocker.add("SecretProperties", "secret.properties.encrypted");

// Decrypt secrets into Java objects.
String secretText = secretsLocker.get("SecretText");
Properties secretProperties = secretsLocker.getAsProperties("SecretProperties");

```

**File System Secrets Locker**</br>
Encrypted secrets must exist in the specified directory. Existence checks are made when secrets are added.
```
// Create the locker.
SecretsLocker secretsLocker = new FileSystemSecretsLocker(“/var/secrets/myapp");
  
// Add encrypted secrets.
secretsLocker.add("SecretText", "secret.txt.encrypted");
secretsLocker.add("SecretProperties", "secret.properties.encrypted");

// Decrypt secrets into Java objects.
String secretText = secretsLocker.get("SecretText");
Properties secretProperties = secretsLocker.getAsProperties("SecretProperties");
```

**S3 Secrets Locker**</br>
Encrypted secrets must exist in the specified S3 bucket at the specified path. When the locker is created, an AWS call is made to check the existence of the bucket. Existence checks are NOT made when secrets are added to the locker.
```
// Create the locker.
SecretsLocker secretsLocker = new S3SecretsLocker("MyBucket","path/to/secrets");
  
// Add encrypted secrets.
secretsLocker.add("SecretText", "secret.txt.encrypted");
secretsLocker.add("SecretProperties", "secret.properties.encrypted");

// Decrypt secrets into Java objects.
String secretText = secretsLocker.get("SecretText");
Properties secretProperties = secretsLocker.getAsProperties("SecretProperties");
```
When the `get` method is called, the encrypted secret is downloaded from S3 and written to the local locker.

### Spring Integration
You can use the Secrets Locker in your Spring Java Configuration to load secret properties into your `PropertySourcesPlaceholderConfigurer`. The following example loads secret properties from AWS S3, based on the environment the application is launched in. E.g., dev, stage, prod, etc.

```
@Configuration
public class MySpringConfigurationClass {

    @Bean
    public static SecretsLocker secretsLocker(
            Environment environment) {
            
        // Create the locker.
        SecretsLocker secretsLocker 
                = new S3SecretsLocker(
                        "MyBucket",
                        "path/to/secrets");
                
        // Get the current environment.
        String env
                = environment
                        .getRequiredProperty(
                                "com.myco.env");
                                
        // Build the secret's filename.
        String secretPropertiesFilename
                = new StringBuilder()
                        .append("secret-")
                        .append(env)
                        .append(".properties.encrypted")
                        .toString();
                        
        // Add the file to the locker.
        secretsLocker.add(
                "SecretProperties", 
                secretPropertiesFilename);
                
        return secretsLocker;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer pspc(
            SecretsLocker secretsLocker) {

        PropertySourcesPlaceholderConfigurer pspc
                = new PropertySourcesPlaceholderConfigurer();
        
        pspc.setProperties(
                secretsLocker
                        .getAsProperties(
                                "secretProperties"));

        pspc.setLocalOverride(true);

        return pspc;
    }
}
```

### Best Practices
* Do not call `get` on the same secret multiple times. Each call will result in decryption. Instead, call `get` once and keep a reference to the object.

### Snapshots Repository
```
<repository>
    <id>oss-snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```


