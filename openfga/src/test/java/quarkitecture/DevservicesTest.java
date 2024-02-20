package quarkitecture;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.OpenFGAClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
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
    void _Access() {
        List<Store> stores = client.listAllStores().await().indefinitely();
        assertTrue(stores.size() > 0);
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("dev")));

    }

}
