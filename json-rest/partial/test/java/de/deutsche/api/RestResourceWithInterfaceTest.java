package de.deutsche.api;

import de.deutsche.model.Organisation;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class RestResourceWithInterfaceTest {

    private RestResourceWithInterface restResource;

    @BeforeEach
    void setUp() {
        restResource = new RestResourceWithInterface();
    }

    @Test
    void testJsonPayloadOhneBeanBindingsBodiesIdGet() {
        assertThrows(UnsupportedOperationException.class, () -> {
            restResource.jsonPayloadOhneBeanBindingsBodiesIdGet(1L);
        });
    }

    @Test
    void testJsonPayloadOhneBeanBindingsBodiesPost() {
        assertThrows(UnsupportedOperationException.class, () -> {
            restResource.jsonPayloadOhneBeanBindingsBodiesPost("test body");
        });
    }

    @Test
    void testJsonPayloadOhneBeanBindingsFieldsIdGet() {
        assertThrows(UnsupportedOperationException.class, () -> {
            restResource.jsonPayloadOhneBeanBindingsFieldsIdGet(1L);
        });
    }

    @Test
    void testJsonPayloadOhneBeanBindingsFieldsPost() {
        Organisation organisation = new Organisation();
        assertThrows(UnsupportedOperationException.class, () -> {
            restResource.jsonPayloadOhneBeanBindingsFieldsPost(organisation);
        });
    }
}