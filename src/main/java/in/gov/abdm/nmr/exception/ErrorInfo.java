package in.gov.abdm.nmr.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * A class to represent error information.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {

    /**
     * Code field representing error code.
     */
    private String code;

    /**
     * Message field representing error message.
     */
    private String message;

    /**
     * Details field representing error details.
     */
    private List<DetailsTO> details;
}
