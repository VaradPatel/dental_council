package in.gov.abdm.nmr.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AttributeTO is a data transfer object for storing attributes with key-value pairs.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeTO {

    /**
     * The key of the attribute.
     */
    private String key;

    /**
     * The value of the attribute.
     */
    private String value;
}
