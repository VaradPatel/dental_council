package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object - Structure to transfer data between layers and
 * represent Data to the external world [while restricting access to entity classes]
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusWiseCountTO {

    /**
     * Application status at that point in time
     */
    private String name;

    /**
     * Count of applications according to their status at that point in time
     */
    private BigInteger count;

}