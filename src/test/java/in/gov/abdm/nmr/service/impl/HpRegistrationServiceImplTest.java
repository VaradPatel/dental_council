package in.gov.abdm.nmr.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@ExtendWith(MockitoExtension.class)
public class HpRegistrationServiceImplTest {

    @InjectMocks
    HpRegistrationServiceImpl hpRegistrationService;

    @Test
    public void testFuzzyScore() throws IOException {

        double fuzzyScore = hpRegistrationService.getFuzzyScore("Satish Chonde", "Satish Udhavrao chonde");
//        System.out.println(fuzzyScore);
//        Assertions.assertTrue(fuzzyScore> 80);

        File file = new File("C:/Users/10717747/Downloads/e-sign_NMR.pdf");
        byte[] input_file = Files.readAllBytes(Paths.get("C:/Users/10717747/Downloads/e-sign_NMR.pdf"));
//        Base64.getEncoder().encodeToString(input_file);
        String encodeToString = Base64.getEncoder().encodeToString(input_file);
        Base64.getDecoder().decode(encodeToString)
    }

}
