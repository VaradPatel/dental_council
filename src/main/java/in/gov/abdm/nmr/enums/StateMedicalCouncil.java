package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

public enum StateMedicalCouncil {

    ANDHRA_PRADESH_MEDICAL_COUNCIL(BigInteger.valueOf(1), "Andhra Pradesh Medical Council"),
    ARUNACHAL_PRADESH_MEDICAL_COUNCIL(BigInteger.valueOf(2), "Arunachal Pradesh Medical Council"),
    ASSAM_MEDICAL_COUNCIL(BigInteger.valueOf(3), "Assam Medical Council"),
    BIHAR_MEDICAL_COUNCIL(BigInteger.valueOf(4), "Bihar Medical Council"),
    CHHATTISGARH_MEDICAL_COUNCIL(BigInteger.valueOf(5), "Chhattisgarh Medical Council"),
    DELHI_MEDICAL_COUNCIL(BigInteger.valueOf(6), "Delhi Medical Council"),
    GOA_MEDICAL_COUNCIL(BigInteger.valueOf(7), "Goa Medical Council"),

    GUJARAT_MEDICAL_COUNCIL(BigInteger.valueOf(8), "Gujarat Medical Council"),

    HARYANA_MEDICAL_COUNCIL(BigInteger.valueOf(9), "Haryana Medical Council"),

    HIMANCHAL_PRADESH_MEDICAL_COUNCIL(BigInteger.valueOf(10), "Himanchal Pradesh Medical Council"),
    JAMMU_AND_KASHMIR_MEDICAL_COUNCIL(BigInteger.valueOf(11), "Jammu and  Kashmir Medical Council"),
    JHARKHAND_MEDICAL_COUNCIL(BigInteger.valueOf(12), "Jharkhand Medical Council"),
    KARNATAKA_MEDICAL_COUNCIL(BigInteger.valueOf(13), "Karnataka Medical Council"),
    MAHARASHTRA_MEDICAL_COUNCIL(BigInteger.valueOf(14), "Maharashtra Medical Council"),
    MADHYA_PRADESH_MEDICAL_COUNCIL(BigInteger.valueOf(15), "Madhya Pradesh Medical Council"),

    MANIPUR_MEDICAL_COUNCIL(BigInteger.valueOf(16), "Manipur Medical Council"),

    MIZORAM_MEDICAL_COUNCIL(BigInteger.valueOf(17), "Mizoram Medical Council"),
    NAGALAND_MEDICAL_COUNCIL(BigInteger.valueOf(18), "Nagaland Medical Council"),
    ORISSA_COUNCIL_OF_MEDICAL_REGISTRATION(BigInteger.valueOf(19), "Orissa Council of Medical Registration"),
    PUNJAB_MEDICAL_COUNCIL(BigInteger.valueOf(20), "Punjab Medical Council"),

    RAJASTHAN_MEDICAL_COUNCIL(BigInteger.valueOf(21), "Rajasthan Medical Council"),
    SIKKIM_MEDICAL_COUNCIL(BigInteger.valueOf(22), "Sikkim Medical Council"),
    TAMIL_NADU_MEDICAL_COUNCIL(BigInteger.valueOf(23), "Tamil Nadu Medical Council"),
    TELANGANA_MEDICAL_COUNCIL(BigInteger.valueOf(24), "Telangana State Medical Council"),
    TRIPURA_MEDICAL_COUNCIL(BigInteger.valueOf(25), "Tripura State Medical Council"),
    UTTARAKHAND_MEDICAL_COUNCIL(BigInteger.valueOf(26), "Uttarakhand Medical Council"),
    UTTAR_PRADESH_MEDICAL_COUNCIL(BigInteger.valueOf(27), "Uttar Pradesh Medical Council"),
    WEST_BENGAL_MEDICAL_COUNCIL(BigInteger.valueOf(28), "West Bengal Medical Council"),
    NATIONAL_MEDICAL_COMMISSION(BigInteger.valueOf(29), "National Medical Commission (erstwhile Medical Council of India)"),
    KERALA_MEDICAL_COUNCIL(BigInteger.valueOf(30), "Travancore Cochin Medical Council, Kerala");

//    JAMMU_AND_KASHMIR_MEDICAL_COUNCIL(BigInteger.valueOf(1), "Jammu and  Kashmir Medical Council");
    private final BigInteger id;
    private final String description;

    StateMedicalCouncil(BigInteger id, String description) {
        this.id = id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
