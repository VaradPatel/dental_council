package in.gov.abdm.nmr.dto.dsc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DscResponseTo {

	String espRequest;
	String aspTxnId;
	String contentType;
	String espUrl;
}
