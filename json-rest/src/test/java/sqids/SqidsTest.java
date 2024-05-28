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
        assertThat(id).hasSize(lengthOfUUID * 2 / 3);
        List<Long> numbers = sqids.decode(id); // [1, 2, 3]
        assertThat(numbers).isEqualTo(
                List.of(Math.abs(input.getLeastSignificantBits()), Math.abs(input.getMostSignificantBits())));
    }

    interface IntMath {
        int operation(int lengthOfUUID);
    }

    @Test
    void sqID_ist_kuerzer_als_UUID() {
        // links assoziative Multiplikation rundet wenn ausgeführt auf waehrend Faktor
        // 2/3 abrundet
        final IntMath factor = (int n) -> n * 2 / 3;

        final UUID input = UUID.randomUUID();

        Sqids sqids = Sqids.builder().build();
        String id = sqids
                .encode(List.of(Math.abs(input.getLeastSignificantBits()), Math.abs(input.getMostSignificantBits())));
        //
        final int lengthOfUUID = input.toString().length();
        assertThat(id).hasSize(factor.operation(lengthOfUUID));

        List<Long> numbers = sqids.decode(id);
        assertThat(numbers).isEqualTo(
                List.of(Math.abs(input.getLeastSignificantBits()), Math.abs(input.getMostSignificantBits())));
    }

    @Test
    void longZuSqid() {

        Sqids sqids = Sqids.builder().build();
        assertThat(sqids
                .encode(List.of(1L))).hasSize(2);

        assertThat(sqids
                .encode(List.of(Long.MAX_VALUE))).hasSize(12);

        assertThat(sqids
                .encode(List.of(Long.valueOf(0b1111111111111111)))).hasSize(4);

        assertThat(sqids
                .encode(List.of(Long.valueOf(0b1000000000000000)))).hasSize(4);

    }
}
