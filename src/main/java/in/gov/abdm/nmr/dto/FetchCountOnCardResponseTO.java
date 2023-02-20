package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Final Response Structure of fetchCountOnCard API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchCountOnCardResponseTO {

    /**
     * This holds the list of count of New applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO hpRegistrationRequest;

    /**
     * This holds the list of count of Existing applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO hpModificationRequest;

    /**
     * This holds the list of count of Temporary Suspension applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO temporarySuspensionRequest;

    /**
     * This holds the list of count of Permanent Suspension applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO permanentSuspensionRequest;

    /**
     * This holds the list of count of Consolidated Suspension applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO consolidatedSuspensionRequest;

    /**
     * This holds the list of count of Activate License applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO activateLicenseRequest;

    /**
     * This holds the list of count of College Registration applications
     * according to their status at that point in time
     */
    private FetchCountOnCardInnerResponseTO collegeRegistrationRequest;



}
