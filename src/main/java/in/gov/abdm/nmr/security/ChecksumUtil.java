package in.gov.abdm.nmr.security;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChecksumUtil {

    @Value("classpath:key/checksum-key.txt")
    private Resource checksumKey;

    @SuppressWarnings("unchecked")
    public String generateChecksum(String jsonString) throws IOException {

        String key = new String(checksumKey.getInputStream().readAllBytes()) + jsonString;
        return getSHA256Hash(key);
    }

    public boolean validateChecksum(String jsonString, String expectedChecksum) {
        try {
            jsonString = jsonString.replaceAll(NMRConstants.REGEX_FOR_EXTRA_SPACES_AND_NEW_LINES,"");
            String generatedChecksum = getSHA256Hash(new String(checksumKey.getInputStream().readAllBytes()) + jsonString);
            return generatedChecksum.equalsIgnoreCase(expectedChecksum);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            log.error("Exception occurred while generating hash. Exception details are  {}", exception);
        }
        return result;
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array to a
     * hexadecimal string. Note that this generates hexadecimal in lower case.
     *
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }
}
