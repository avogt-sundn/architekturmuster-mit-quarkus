package quarkitecture;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.OpenFGAClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.Metadata;
import io.quarkiverse.openfga.client.model.RelationMetadata;
import io.quarkiverse.openfga.client.model.RelationReference;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkiverse.openfga.client.model.TypeDefinition;
import io.quarkiverse.openfga.client.model.TypeDefinitions;
import io.quarkiverse.openfga.client.model.Userset;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

@QuarkusTest
class OpenFGATest {

    OpenFGAClient client;
    private ObjectMapper objectMapper;

    // constructor injection
    public OpenFGATest(OpenFGAClient client) {
        this.client = client;
        this.objectMapper = new JsonMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                // MUST have this ParameterNamesModule registered
                .registerModule(new ParameterNamesModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL).enable(SerializationFeature.INDENT_OUTPUT);
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
    @Disabled("failing")
    void _Run() throws IOException {

        clean();
        // create store
        String storeId = client.createStore("test").await().indefinitely().getId();
        // access store via store ID
        StoreClient storeClient = client.store(storeId);
        // read file from classpath
        String fileContents = Files.readString(Path.of("auth-model.json"));

        assertThat(fileContents).isNotNull();
        Log.info("file:" + fileContents);
        TypeDefinitions modelFromFile = objectMapper.readValue(fileContents, TypeDefinitions.class);
        // create a tuple
        String modelId = storeClient.authorizationModels().create(modelFromFile).await()
                .indefinitely();
        AuthorizationModelClient model = storeClient.authorizationModels().model(modelId);

        model.write(TupleKey.of("thing:1", "owner", "user:me")).await().indefinitely();

        Boolean check = model.check(TupleKey.of("test", "test", "test"), null).await().indefinitely();
        assertThat(check).isTrue();
        // delete store
        delete(storeId, storeClient);
    }

    @SuppressWarnings("null")
    @Test
    void _CreateViaAPI() throws IOException {

        clean();
        // create store
        String storeId = client.createStore("test").await().indefinitely().getId();
        // access store via store ID
        StoreClient storeClient = client.store(storeId);

        // ensure it has an auth model
        var useTypeDef = new TypeDefinition("user", Map.of());

        var documentTypeDef = new TypeDefinition("document", Map.of(
                "reader", Userset.direct("a", 1),
                "writer", Userset.direct("b", 2)),
                new Metadata(
                        Map.of("reader", new RelationMetadata(List.of(new RelationReference("user"))),
                                "writer", new RelationMetadata(List.of(new RelationReference("user"))))));

        var typeDefs = new TypeDefinitions("1.1", List.of(useTypeDef, documentTypeDef));

        var json = objectMapper.writeValueAsString(typeDefs);
        Log.info("json:" + json);
        assertThat(json).contains("a");

        var readback = objectMapper.readValue(json, TypeDefinitions.class);
        assertThat(readback.getSchemaVersion()).isEqualTo("1.1");

        var authModelId = storeClient.authorizationModels().create(typeDefs)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        AuthorizationModelClient authorizationModelClient = storeClient.authorizationModels().model(authModelId);
        // write tuples
        var tuples = List.of(
                TupleKey.of("document:123", "reader", "user:me"),
                TupleKey.of("document:123", "reader", "user:a"));
        var writes = authorizationModelClient.write(tuples, Collections.emptyList())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();
        assertThat(writes.entrySet()).isEmpty();

        Boolean a = authorizationModelClient.check(TupleKey.of("document:123", "reader", "user:a"), null).await()
                .indefinitely();
        assertThat(a).isTrue();
        Boolean check = authorizationModelClient.check(TupleKey.of("document:123", "reader", "user:me"), null).await()
                .indefinitely();
        assertThat(check).isTrue();

        Boolean failure = authorizationModelClient.check(TupleKey.of("document:123", "writer", "user:me"), null)
                .await()
                .indefinitely();
        assertThat(failure).isFalse();
        // delete store
        delete(storeId, storeClient);
    }

    @SuppressWarnings("null")
    @Test
    void _CreateFromJson() throws IOException {
        var schema = """
                        {
                            "schema_version": "1.1",
                            "type_definitions": [
                                {
                                    "type": "user",
                                    "relations": {},
                                    "metadata": null
                                },
                                {
                                    "type": "document",
                                    "relations": {
                                        "writer": {
                                            "this": {
                                                "b": 2
                                            },
                                            "computedUserset": null,
                                            "tupleToUserset": null,
                                            "union": null,
                                            "intersection": null,
                                            "difference": null
                                        },
                                        "reader": {
                                            "this": {
                                                "a": 1
                                            },
                                            "computedUserset": null,
                                            "tupleToUserset": null,
                                            "union": null,
                                            "intersection": null,
                                            "difference": null
                                        }
                                    },
                                    "metadata": {
                                        "relations": {
                                            "writer": {
                                                "directly_related_user_types": [
                                                    {
                                                        "type": "user",
                                                        "relation": null,
                                                        "wildcard": null,
                                                        "condition": null
                                                    }
                                                ]
                                            },
                                            "reader": {
                                                "directly_related_user_types": [
                                                    {
                                                        "type": "user",
                                                        "relation": null,
                                                        "wildcard": null,
                                                        "condition": null
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                """;
        clean();
        // create store
        String storeId = client.createStore("test").await().indefinitely().getId();
        // access store via store ID
        StoreClient storeClient = client.store(storeId);

        // ensure it has an auth model

        var typeDefs = objectMapper.readValue(schema, TypeDefinitions.class);
        assertThat(typeDefs.getSchemaVersion()).isEqualTo("1.1");

        var authModelId = storeClient.authorizationModels().create(typeDefs)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        AuthorizationModelClient authorizationModelClient = storeClient.authorizationModels().model(authModelId);
        // write tuples
        var tuples = List.of(
                TupleKey.of("document:123", "reader", "user:der_leser"),
                TupleKey.of("document:123", "writer", "user:der_autor")

        );
        var writes = authorizationModelClient.write(tuples, Collections.emptyList())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();
        assertThat(writes.entrySet()).isEmpty();

        Boolean success = authorizationModelClient.check(TupleKey.of("document:123", "reader", "user:der_leser"), null)
                .await()
                .indefinitely();
        assertThat(success).isTrue();

        Boolean failure = authorizationModelClient.check(TupleKey.of("document:123", "writer", "user:der_leser"), null)
                .await()
                .indefinitely();

        assertThat(failure).isFalse();
        // delete store
        delete(storeId, storeClient);
    }

    private void clean() {
        client.listAllStores().await().indefinitely().forEach(s -> {

            if (s.getName().equals("test")) {
                delete(s.getId(), client.store(s.getId()));
            }
        });
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
