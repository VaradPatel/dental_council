package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class IMRDetailsTO {
	private BigInteger registrationNumber;
	private String nmrId;
	private String yearOfInfo;
}
