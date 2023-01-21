package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;
import lombok.Value;

@Value
public class HpProfileUpdateResponseTO {

	private Integer status;
	private String message;
	private BigInteger hpProfileId;
}
