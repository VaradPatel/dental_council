package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * Data Transfer Object - Structure to transfer data between layers and
 * represent Data to the external world [while restricting access to entity classes]
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextGroupTO {

    private BigInteger assignTo;

    private BigInteger workFlowStatusId;

}