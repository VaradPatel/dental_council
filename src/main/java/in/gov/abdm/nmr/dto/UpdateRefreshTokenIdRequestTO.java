package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateRefreshTokenIdRequestTO {

    private String username;

    @NotBlank
    private String refreshTokenId;
}
