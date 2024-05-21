package sqids;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.sqids.Sqids;

class SqidsTest {

    @Test
    void GeneriereMalEineId() {

        Sqids sqids = Sqids.builder().build();
        String id = sqids.encode(Arrays.asList(1L, 2L, 3L)); // "86Rf07"
        List<Long> numbers = sqids.decode(id); // [1, 2, 3]
        assertThat(id).isEqualTo("86Rf07");
        assertThat(numbers).isEqualTo(List.of(1L, 2L, 3L));
    }

    @Test
    void UUIDzuSQID_V1() {
        final UUID input = UUID.randomUUID();

        Sqids sqids = Sqids.builder().build();
        String id = sqids
                .encode(List.of(Math.abs(input.getLeastSignificantBits()), Math.abs(input.getMostSignificantBits())));
        //
        final int lengthOfUUID = input.toString().length();
        assertThat(id.length()).isEqualTo(lengthOfUUID*2/3);
        List<Long> numbers = sqids.decode(id); // [1, 2, 3]
        assertThat(numbers).isEqualTo(
                List.of(Math.abs(input.getLeastSignificantBits()), Math.abs(input.getMostSignificantBits())));
    }

}
