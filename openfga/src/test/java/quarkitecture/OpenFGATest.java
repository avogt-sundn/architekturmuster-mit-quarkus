package quarkitecture;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class OpenFGATest {

    @Inject
    StoreClient storeClient;
    AuthorizationModelClient authModelClient;

    @Test
    void _ListAll() {
        Assertions.assertNotNull(storeClient.authorizationModels().listAll());
    }

    @Test
    void _ListTuples() {
        Assertions.assertNotNull(authModelClient.readAllTuples())
        ;
    }

    @SuppressWarnings("null")
    @Test
    void _StoreCheck() {

        authModelClient = storeClient.authorizationModels().defaultModel();
        authModelClient.write(TupleKey.of("thing:1", "reader", "user:me"));

        List<Tuple> tuples = this.storeClient.authorizationModels().defaultModel().readAllTuples().await()
                .indefinitely();
        Assertions.assertTrue(tuples.size() > 0);
    }
}
