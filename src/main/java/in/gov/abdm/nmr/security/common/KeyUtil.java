package in.gov.abdm.nmr.security.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class KeyUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private KeyStore keyStore;

    public KeyUtil(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public RSAPrivateKey getPrivateKey(String keyAlias, String privateKeyPass) {
        try {
            if (keyStore.getKey(keyAlias, privateKeyPass.toCharArray()) instanceof RSAPrivateKey privateKey) {
                return privateKey;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("Exception occured while loading private key from keystore: ", e);
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
            LOGGER.error("Exception occured while loading public key from keystore: ", e);
        }
        throw new IllegalArgumentException("Unable to load RSA public key");
    }
}
