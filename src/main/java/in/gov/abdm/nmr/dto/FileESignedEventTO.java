package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Jacksonized
public class FileESignedEventTO {
    String fileName;
    String transactionId;
    Boolean isSigned;
    String name;
    String pincode;
    String yob;
}