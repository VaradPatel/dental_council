package in.gov.abdm.nmr.security;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import in.gov.abdm.nmr.common.ApplicationProfileEnum;
import in.gov.abdm.nmr.common.CustomHeaders;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

@Slf4j
@Component
public class ChecksumUtil {

    @Value("classpath:key/checksum-key.txt")
    private Resource checksumKey;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${feature.toggle.checksum.enable}")
    private boolean checksumEnable;

    @SuppressWarnings("unchecked")
    public String generateChecksum(String jsonString) throws IOException {

        String key = new String(checksumKey.getInputStream().readAllBytes()) + jsonString;
        return getSHA256Hash(key);
    }

    @SneakyThrows
    public void validateChecksum() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

        byte[] requestBody = wrapper.getContentAsByteArray();
        String expectedChecksum = request.getHeader(CustomHeaders.CHECKSUM_HEADER);

        String requestString = new String(requestBody, StandardCharsets.UTF_8);

        if(ApplicationProfileEnum.LOCAL.getCode().equals(activeProfile)) {

            if (checksumEnable) {


                if (!requestString.isEmpty()) {
                    requestString = requestString.replaceAll(NMRConstants.REGEX_FOR_EXTRA_SPACES_AND_NEW_LINES, "");

                    String generatedChecksum = getSHA256Hash(new String(checksumKey.getInputStream().readAllBytes()) + requestString);

                    if (!generatedChecksum.equalsIgnoreCase(expectedChecksum)) {
                        throw new InvalidRequestException(NMRError.DATA_TAMPERED.getCode(), NMRError.DATA_TAMPERED.getMessage());
                    }


                }
            }
        }
        System.out.println("No error in validate checksum ");
//        throw new Exception("Test");
    }

    private static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash);
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
