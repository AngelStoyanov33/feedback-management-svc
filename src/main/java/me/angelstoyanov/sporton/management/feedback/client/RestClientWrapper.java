package me.angelstoyanov.sporton.management.feedback.client;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import me.angelstoyanov.sporton.management.feedback.model.StorageEntity;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;

@ApplicationScoped
@RegisterForReflection
@Named("RestClientWrapper")
public class RestClientWrapper {

    @Inject
    @RestClient
    protected AzureStorageAdapterClient azureStorageAdapterClient;

    public String uploadBlob(@NotNull String entityId, @NotNull StorageEntity entityType, @NotNull File blob) {
        try {
            System.out.println("Uploading blob for entityId: " + entityId + " and entityType: " + entityType);
            Response svcResponse = azureStorageAdapterClient.upload(entityId, String.valueOf(entityType), blob);
            if (svcResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                return svcResponse.readEntity(String.class);
            }
        } catch (WebApplicationException e) {
            return null;
        }
        return null;
    }

}
