package info.ernestas.awesomecoin.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashUtilTest {

    @ParameterizedTest
    @CsvSource(value = {
            "123, a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            "text, 982d9e3eb996f559e633f4d194def3761d909f5a3b647d1a851fead67c32c9d1",
            "blockchain, ef7797e13d3a75526946a3bcf00daec9fc9c9c4d51ddc7cc5df888f74dd434d1"
    })
    void calculateSha256Hash(String input, String expected) {
        assertEquals(expected, HashUtil.calculateSha256Hash(input));
    }
}