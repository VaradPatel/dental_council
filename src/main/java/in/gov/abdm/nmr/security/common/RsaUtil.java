package in.gov.abdm.nmr.security.common;

import in.gov.abdm.nmr.common.ApplicationProfileEnum;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class RsaUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private KeyUtil keyUtil;

    private String activeProfile;

    public RsaUtil(KeyUtil keyUtil, @Value("${spring.profiles.active}") String activeProfile) {
        this.keyUtil = keyUtil;
        this.activeProfile = activeProfile;
    }

    public String decrypt(String encrypted) throws GeneralSecurityException {
        if (ApplicationProfileEnum.LOCAL.getCode().equals(activeProfile)) {
            return encrypted;
        }
        try {
            Cipher decryptCipher = Cipher.getInstance(NMRConstants.RSA_PADDING);
            decryptCipher.init(Cipher.DECRYPT_MODE, keyUtil.getPrivateKey());
            return new String(decryptCipher.doFinal(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            LOGGER.error("Exception occured while decrypting encrypted string: ", e);
            throw e;
        }
    }
}
