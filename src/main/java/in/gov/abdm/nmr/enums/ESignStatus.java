package in.gov.abdm.nmr.enums;

/**
 * Enums for all possible E sign status in NMR
 */
public enum ESignStatus {

    PROFILE_ESIGNED_WITH_SAME_AADHAR(1, "Profile esigned with same Aadhar"),
    PROFILE_ESIGNED_WITH_DIFFERENT_AADHAR(2, "Profile esigned with different aadhar"),
    PROFILE_NOT_ESIGNED(3, "Profile not esigned"),
    QUERY_RESOLVED_PROFILE_NOT_ESIGNED(4, "Query resolved, profile not esigned");

    private final Integer id;
    private final String status;

    ESignStatus(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

}
