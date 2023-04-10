package in.gov.abdm.nmr.dto.image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto for image apis access token
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageTokenTo {

    private String accessToken;
    private String expiresIn;
    private String refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String notBeforePolicy;
    private String sessionState;
}
