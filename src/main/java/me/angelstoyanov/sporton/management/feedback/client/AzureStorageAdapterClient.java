package me.angelstoyanov.sporton.management.feedback.client;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;

@RegisterForReflection
@RegisterRestClient
@ApplicationScoped
public interface AzureStorageAdapterClient {

    @POST
    @Consumes("image/png")
    @Path("/upload")
    Response upload(@QueryParam("entityId") String entityId,
                    @QueryParam("entityType") String entityType,
                    File image);

    @GET
    @Produces("image/jpeg")
    @Path("/fetch")
    Response download(@QueryParam("entityId") String entityId,
                      @QueryParam("entityType") String entityType);
}
