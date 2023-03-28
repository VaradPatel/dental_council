package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDetailsTO {

	@NotBlank
	@JsonProperty("qualificationFrom")
	private String qualificationFrom;

	@NotBlank
	@JsonProperty("nameOfDegree")
	private String nameOfDegree;

	@NotBlank
	@JsonProperty("country")
	private String country;

	@NotBlank
	@JsonProperty("state")
	private String state;

	@NotBlank
	@JsonProperty("college")
	private String college;

	@NotBlank
	@JsonProperty("university")
	private String university;

	@NotBlank
	@JsonProperty("monthAndYearOfAwarding")
	private String monthAndYearOfAwarding;
}