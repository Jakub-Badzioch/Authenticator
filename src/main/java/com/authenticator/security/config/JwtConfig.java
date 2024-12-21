package com.authenticator.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("D:\\D-Downloads\\authenticator\\src\\main\\resources\\app.pub")
    private String publicKeyPath;

    @Value("D:\\D-Downloads\\authenticator\\src\\main\\resources\\app.key")
    private String privateKeyPath;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    private void loadKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (publicKey == null || privateKey == null) {
            // Load and decode the public key
            Resource publicKeyResource = new FileSystemResource(publicKeyPath);
            String publicKeyPem = new String(publicKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            byte[] publicKeyBytes = decodePem(publicKeyPem, "PUBLIC KEY");
            publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Load and decode the private key
            Resource privateKeyResource = new FileSystemResource(privateKeyPath);
            String privateKeyPem = new String(privateKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            byte[] privateKeyBytes = decodePem(privateKeyPem, "PRIVATE KEY");
            privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        }
    }

    private byte[] decodePem(String pem, String type) {
        String header = "-----BEGIN " + type + "-----";
        String footer = "-----END " + type + "-----";
        pem = pem.replace(header, "").replace(footer, "").replaceAll("\\s", ""); // Remove headers, footers, and whitespace
        return Base64.getDecoder().decode(pem);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        loadKeys(); // Ensure keys are loaded
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        loadKeys(); // Ensure keys are loaded
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(
                new JWKSet(new RSAKey.Builder(publicKey)
                        .privateKey(privateKey)
                        .build())));
    }
}
