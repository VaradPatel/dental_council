package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NMRPagination {
    private Integer pageNo;
    private Integer offset;
    private String sortBy;
    private String sortType;
}
