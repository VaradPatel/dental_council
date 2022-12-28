package in.gov.abdm.nmr.api.controller.hp.to;

import java.math.BigInteger;

import lombok.Value;

@Value
public class HpProfileAddResponseTO {

	private Integer status;
	private String message;
	private BigInteger hpProfileId;
}
