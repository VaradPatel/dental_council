package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class DashboardResponseTO {
    private BigInteger totalNoOfRecords;
    private List<DashboardTO> dashboardTOList;
}
