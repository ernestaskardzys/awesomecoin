package info.ernestas.awesomecoin.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashTargetUtilTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1, 0",
            "2, 00",
            "3, 000",
            "4, 0000"
    })
    void getHashTarget(String input, String expected) {
        assertEquals(expected, HashTargetUtil.getHashTarget(Integer.valueOf(input)));
    }
}