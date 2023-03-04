package in.gov.abdm.nmr.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

}
