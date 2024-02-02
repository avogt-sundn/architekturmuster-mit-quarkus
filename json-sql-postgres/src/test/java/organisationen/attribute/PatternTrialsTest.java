package organisationen.attribute;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

class PatternTrialsTest {

    @Test
    void understandRegex() {

        Matcher matcher = JsonResourceTest.locationHeaderMatchingPattern.matcher("http://localhost:1234/grumpy/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        Long id = Long.valueOf(matcher.group(1));
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: ", id).isEqualTo(77);
    }

    @Test
    void greedyMatchDoesNotRequireUrlPathPrefix() {

        Matcher matcher = JsonResourceTest.locationHeaderMatchingPattern.matcher("http://localhost:8081/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        String group = matcher.group(1);
        Long id = Long.valueOf(group);
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: " + id).isEqualTo(77);
    }
}
