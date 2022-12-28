package in.gov.abdm.nmr.db.sql.domain.user.to;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateRefreshTokenIdRequestTO {

    private String username;

    @NotBlank
    private String refreshTokenId;
}
