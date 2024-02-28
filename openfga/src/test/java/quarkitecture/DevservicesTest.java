package quarkitecture;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.OpenFGAClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Disabled("in dev mode the classpath files fail to load yet")
class DevservicesTest {

    OpenFGAClient client;
    StoreClient storeClient;
    AuthorizationModelClient authModelClient;

    public DevservicesTest(OpenFGAClient client, StoreClient storeClient, AuthorizationModelClient authModelClient) {
        this.client = client;
        this.storeClient = storeClient;
        this.authModelClient = authModelClient;
    }

    @Test
    void _AccessStore() {
        List<Store> stores = client.listAllStores().await().indefinitely();
        assertTrue(stores.size() > 0);
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("dev")));
    }

    @Test
    void _AccessTuples() {
        List<Tuple> t = authModelClient.readAllTuples().await().indefinitely();
        assertThat(t.size()).isGreaterThan(0);
        t.stream().forEach(System.out::println);
        assertTrue(t.stream().anyMatch(s -> s.getKey().getUser().equals("user:anne")));

    }

}
