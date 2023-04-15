package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.repository.LanguageRepository;
import in.gov.abdm.nmr.service.impl.LanguageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {

    @Mock
    LanguageRepository languageRepository;

    @InjectMocks
    LanguageServiceImpl languageService;

    @Test
    void testGetSpecialityDataShouldReturnValidResponse(){
        when(languageRepository.getLanguage()).thenReturn(List.of(getLanguage()));
        List<LanguageTO> languages = languageService.getLanguageData();
        assertTrue(languages.size() == 1);
        assertEquals(ID, languages.get(0).getId());
        assertEquals(LANGUAGE_NAME, languages.get(0).getName());
    }

}
