package in.gov.abdm.nmr.dto.image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto for auth otp details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageCompareTo {

    private String image1;
    private String image2;
}
