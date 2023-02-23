package in.gov.abdm.nmr.dto;

import lombok.Value;

import java.math.BigInteger;

@Value
public class HpProfileAddResponseTO {

	private Integer status;
	private String message;
	private BigInteger hpProfileId;
	private String requestId;
}
