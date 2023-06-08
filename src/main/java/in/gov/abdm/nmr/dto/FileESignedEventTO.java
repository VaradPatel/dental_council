package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileESignedEventTO {
    String fileName;
    String transactionId;
    Boolean signed;
    String name;
    String pincode;
    String yob;
    String lastAadharDigit;

}