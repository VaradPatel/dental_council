package in.gov.abdm.nmr.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The DetailsTO class is used to transfer the details data between various layers of the application.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsTO {
    /**
     * A message that provides additional information about the details.
     */
    private String message;

    /**
     * A code that identifies the details.
     */
    private String code;

    /**
     * An object of AttributeTO class that holds information about the attributes.
     */
    private AttributeTO attribute;


}
