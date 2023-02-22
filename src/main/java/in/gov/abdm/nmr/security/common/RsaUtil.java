package in.gov.abdm.nmr.security.common;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.gov.abdm.nmr.common.ApplicationProfileEnum;

@Component
public class RsaUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String KEY_ALIAS = "api";

    private KeyUtil keyUtil;

    private String privateKeyPass;

    private String activeProfile;

    public RsaUtil(KeyUtil keyUtil, @Value("${nmr.api.private.key.pass}") String privateKeyPass, @Value("${spring.profiles.active}") String activeProfile) {
        this.keyUtil = keyUtil;
        this.privateKeyPass = privateKeyPass;
        this.activeProfile = activeProfile;
    }

    public String decrypt(String encrypted) throws GeneralSecurityException {
        if (ApplicationProfileEnum.LOCAL.getCode().equals(activeProfile)) {
            return encrypted;
        }
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, keyUtil.getPrivateKey(KEY_ALIAS, privateKeyPass));
            return new String(decryptCipher.doFinal(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            LOGGER.error("Exception occured while decrypting encrypted string: ", e);
            throw e;
        }
    }
}
