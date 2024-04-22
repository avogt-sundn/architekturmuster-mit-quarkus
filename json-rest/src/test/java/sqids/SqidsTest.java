package sqids;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sqids.Sqids;

public class SqidsTest {

    @Test
    void GeneriereMalEineId() {

        Sqids sqids = Sqids.builder().build();
        String id = sqids.encode(Arrays.asList(1L, 2L, 3L)); // "86Rf07"
        List<Long> numbers = sqids.decode(id); // [1, 2, 3]
    }
}
