package in.gov.abdm.nmr.nosql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDetails {
    private boolean is_currently_working;
    private String purpose_of_work;
    private boolean governmentEmployee;
}
