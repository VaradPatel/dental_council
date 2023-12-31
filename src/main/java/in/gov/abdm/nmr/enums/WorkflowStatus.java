package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enums for all possible actions in NMR
 */
public enum WorkflowStatus {
    PENDING(BigInteger.valueOf(1),"Pending"),
    APPROVED(BigInteger.valueOf(2),"Approved"),
    QUERY_RAISED(BigInteger.valueOf(3),"Query Raised"),
    REJECTED(BigInteger.valueOf(4),"Rejected"),
    SUSPENDED(BigInteger.valueOf(5),"Suspended"),
    BLACKLISTED(BigInteger.valueOf(6),"Blacklisted");
    private final BigInteger id;
    private final String description;

    WorkflowStatus(BigInteger id, String description){
        this.id= id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static WorkflowStatus getWorkflowStatus(BigInteger id) {
        Optional<WorkflowStatus> optionalWorkflowStatus = Arrays.stream(WorkflowStatus.values()).filter(workflowStatus -> workflowStatus.getId().equals(id)).findFirst();
        return optionalWorkflowStatus.isPresent() ? optionalWorkflowStatus.get() : null;
    }
}