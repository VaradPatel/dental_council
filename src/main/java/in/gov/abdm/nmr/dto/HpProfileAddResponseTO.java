package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Value;

@Value
public class HpProfileAddResponseTO {

	private Integer status;
	private String message;
	private BigInteger hpProfileId;
	private String requestId;
}
