package in.gov.abdm.nmr.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response Data Transfer Object to transfer State data
 * between the different layers of this spring-boot application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LGDSearchTO {

    private Integer pinCode;

    private Integer townVillageCode;

    private String townVillageName;

    private Integer districtCode;

    private String districtName;

    private Integer stateCode;

    private String stateName;

}
