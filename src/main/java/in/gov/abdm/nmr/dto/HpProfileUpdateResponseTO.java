package in.gov.abdm.nmr.dto;

import lombok.Value;

import java.math.BigInteger;

@Value
public class HpProfileUpdateResponseTO {

	private Integer status;
	private String message;
	private BigInteger hpProfileId;
}
