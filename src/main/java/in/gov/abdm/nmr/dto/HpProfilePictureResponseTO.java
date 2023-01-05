package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class HpProfilePictureResponseTO {

	private String message;
	private Integer status;
	private byte[] profilePicture;
	private String picName;
}
