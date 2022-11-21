package in.gov.abdm.nmr.api.ext.user_detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRefreshTokenIdRequestTO {

    private String username;
    private String refreshTokenId;
}
