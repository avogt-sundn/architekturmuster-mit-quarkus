package quarkitecture;

import static java.time.Duration.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.OpenFGAClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class OpenFGATest {

    OpenFGAClient client;

    StoreClient storeClient;
    AuthorizationModelClient authModelClient;

    public OpenFGATest(OpenFGAClient client, StoreClient storeClient, AuthorizationModelClient authModelClient) {
        this.client = client;
        this.storeClient = storeClient;
        this.authModelClient = authModelClient;
    }


    @SuppressWarnings("null")
    @Test
    void _StoresCreateDelete() {

        String randomAlphabetic = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        Log.info("randomAlphabetic:" + randomAlphabetic);
        final String name = randomAlphabetic;

        // create store
        String storeId = client.createStore(name).await().indefinitely().getId();
        // access store via store ID
        StoreClient storeClient = client.store(storeId);
        List<Store> listAllStores = client.listAllStores().await().indefinitely();
        Log.info("listAllStores:" + listAllStores);
        assertThat(listAllStores).isNotNull();
        // at least one store matches our name
        client.listAllStores().await().indefinitely().stream().anyMatch(s -> s.getName().equals(name));

        // delete store
        storeClient.delete().await().atMost(ofSeconds(10));

        client.listAllStores().await().indefinitely().forEach(s -> {
            if (s.getName().equals(name) || s.getId().equals(storeId)) {
                Assertions.fail("Store not deleted");
            }
        });
    }

}
