package in.gov.abdm.nmr.domain.user_detail;

import lombok.Data;

@Data
public class UpdateRefreshTokenIdRequestTO {

    private String username;
    private String refreshTokenId;
}
