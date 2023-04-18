package in.gov.abdm.nmr.service.impl;


import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class HpRegistrationServiceImplTest {

    @InjectMocks
    HpRegistrationServiceImpl hpRegistrationService;

    @Test
    void testFuzzyScore() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Govind Kedia", "Govind Dwarkadasji Kedia");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreLogic() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Sathish Chonde", "Sathish Udhavroa Chonde");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreImplementation() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Vijay Anthony Chandrasaekar", "Vijay Chandrasaekar");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }
}
