package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;
import static in.gov.abdm.nmr.util.NMRUtil.validateWorkProfileDetails;
import static org.junit.jupiter.api.Assertions.*;

class NMRUtilTest {

    private String name;

    @BeforeEach
    public void init() {
        name = "John Doe";
    }

    @AfterEach
    void tearDown() {
        name = null;
    }
    @Test
    void testCoalescingWithString(){
        assertEquals(name, coalesce(name, null));
    }

    @Test
    void testCoalescingWithStringWithDefaultValue(){
        assertEquals(name, coalesce(null, name));
    }

    @Test
    void testCoalescingWithStringWithNullValues(){
        assertNull(coalesce(null, null));
    }

    @Test
    void testValidateWorkProfileDetailsThrowsInvalidRequestExceptionForNullCollection(){
        assertThrows(InvalidRequestException.class, () -> validateWorkProfileDetails(null));
    }
    @Test
    void testValidateWorkProfileDetailsThrowsInvalidRequestExceptionForEmptyCollection(){
        assertThrows(InvalidRequestException.class, () -> validateWorkProfileDetails(Collections.emptyList()));
    }

    @Test
    void testValidateWorkProfileDetailsDoNotThrowsInvalidRequestExceptionForValidCollection(){
        assertDoesNotThrow(()-> validateWorkProfileDetails(List.of(new CurrentWorkDetailsTO())));

    }

}
