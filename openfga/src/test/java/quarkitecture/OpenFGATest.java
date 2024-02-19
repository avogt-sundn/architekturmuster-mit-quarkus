package quarkitecture;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.OpenFGAClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkiverse.openfga.client.model.TypeDefinition;
import io.quarkiverse.openfga.client.model.TypeDefinitions;
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
        assertTrue(client.listAllStores().await().indefinitely().stream().anyMatch(s -> s.getName().equals(name)));

        // delete store
        delete(storeId, storeClient);
    }

    @SuppressWarnings("null")
    @Test
    void _Run() throws IOException {
        client.listAllStores().await().indefinitely().forEach(s -> {

            if (s.getName().equals("test")) {
                delete(s.getId(), client.store(s.getId()));
            }
        });
        // create store
        String storeId = client.createStore("test").await().indefinitely().getId();
        // access store via store ID
        StoreClient storeClient = client.store(storeId);
        List<TypeDefinition> td = new ArrayList<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        // read file from classpath
        String fileContents = Files.readString(Path.of("src/test/resources/auth-model.json"));
        // byte[] file =
        // this.getClass().getResourceAsStream("auth-model.json").readAllBytes();

        assertThat(fileContents).isNotNull();
        Log.info("file:" + fileContents);
        TypeDefinitions modelFromFile = objectMapper.readValue(
                fileContents,
                TypeDefinitions.class);
        // create a tuple
        String modelId = storeClient.authorizationModels().create(modelFromFile.getTypeDefinitions()).await()
                .indefinitely();
        AuthorizationModelClient model = storeClient.authorizationModels().model(modelId);

        model.write(
                TupleKey.of("thing:1",
                        "owner",
                        "user:me"))
                .await().indefinitely();

        Boolean check = model.check(TupleKey.of("test", "test", "test"), null).await().indefinitely();
        assertThat(check).isTrue();
        // delete store
        delete(storeId, storeClient);
    }

    private void delete(String storeId, StoreClient storeClient) {
        storeClient.delete().await().indefinitely();
        client.listAllStores().await().indefinitely().forEach(s -> {
            if (s.getId().equals(storeId)) {
                Assertions.fail("Store not deleted");
            }
        });
    }

}
