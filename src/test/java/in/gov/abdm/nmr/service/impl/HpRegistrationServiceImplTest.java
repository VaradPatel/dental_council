package in.gov.abdm.nmr.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HpRegistrationServiceImplTest {

    @InjectMocks
    HpRegistrationServiceImpl hpRegistrationService;

    @Test
    public void testFuzzyScore(){

        double fuzzyScore = hpRegistrationService.getFuzzyScore("Govind Kedia", "Govind Kedia");
//        System.out.println(fuzzyScore);
        Assertions.assertTrue(fuzzyScore> 80);
    }

}
