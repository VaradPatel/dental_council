package in.gov.abdm.nmr.api.db.user_detail.to;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRefreshTokenIdRequestTO {

    private String username;
    private String refreshTokenId;
}
