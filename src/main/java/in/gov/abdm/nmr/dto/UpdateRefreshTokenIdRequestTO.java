package in.gov.abdm.nmr.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateRefreshTokenIdRequestTO {

    private String username;

    @NotBlank
    private String refreshTokenId;
}
