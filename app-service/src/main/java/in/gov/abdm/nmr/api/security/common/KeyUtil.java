package in.gov.abdm.nmr.api.security.common;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private KeyStore keyStore;

    private String keyPass;

    public KeyUtil(KeyStore keyStore, @Value("${nmr.jwt.private.key.pass}") String keyPass) {
        this.keyStore = keyStore;
        this.keyPass = keyPass;
    }

    public RSAPrivateKey getPrivateKey(String keyAlias) {
        try {
            if (keyStore.getKey(keyAlias, keyPass.toCharArray()) instanceof RSAPrivateKey privateKey) {
                return privateKey;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("Unable to load private key from keystore: ", e);
        }
        throw new IllegalArgumentException("Unable to load private key");
    }

    public RSAPublicKey getPublicKey(String keyAlias) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            if (certificate.getPublicKey() instanceof RSAPublicKey publicKey) {
                return publicKey;
            }
        } catch (KeyStoreException e) {
            LOGGER.error("Unable to load private key from keystore: ", e);
        }
        throw new IllegalArgumentException("Unable to load RSA public key");
    }
}
