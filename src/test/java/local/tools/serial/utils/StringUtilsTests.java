package local.tools.serial.utils;

import local.tools.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTests {

    @Test
    void testJoinMethod() {
        assertEquals(StringUtils.join("One", ":", "'", "Two", ".", "'", "Three"), "One:'Two.'Three");
    }
}
