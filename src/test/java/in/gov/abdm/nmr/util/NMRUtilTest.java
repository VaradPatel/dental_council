package in.gov.abdm.nmr.util;

import org.junit.jupiter.api.Test;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NMRUtilTest {

    @Test
    void testCoalescingWithString(){
        assertEquals("John Doe", coalesce("John Doe", null));
    }

    @Test
    void testCoalescingWithStringWithDefaultValue(){
        assertEquals("John Doe", coalesce(null, "John Doe"));
    }

    @Test
    void testCoalescingWithStringWithNullValues(){
        assertNull(coalesce(null, null));
    }

}
