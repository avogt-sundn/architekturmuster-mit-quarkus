# api-specs

Dieses Projekt zeigt den Umgang mit dem openapi Generator für Rest-APIS

````bash
quarkus extension add openapi
quarkus extension add openapi-generator
quarkus extension add smallrye-openapi
npm install @openapitools/openapi-generator-cli -g
openapi-generator-cli generate -i /workspaces/architekturmuster-mit-quarkus/api-specs/src/main/resources/META-INF/openapi.yaml -g jaxrs-spec -o /workspaces/architekturmuster-mit-quarkus/api-specs/generated
````

### Der geschriebene Code

````java
package quarkitecture;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }
}

````

### Der generierte Code

So sieht das generierte Server-Implementierung aus:

````java
package org.openapitools.api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/hello")
@Api(description = "the hello API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-19T11:53:56.762836878Z[Etc/UTC]", comments = "Generator version: 7.11.0")
public class HelloApi {

    @GET
    @Produces({ "text/plain" })
    @ApiOperation(value = "Hello", notes = "", response = String.class, tags={ "Greeting Resource" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = String.class)
    })
    public Response helloGet() {
        return Response.ok().entity("magic!").build();
    }
}
````