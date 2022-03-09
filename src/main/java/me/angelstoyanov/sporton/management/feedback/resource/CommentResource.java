package me.angelstoyanov.sporton.management.feedback.resource;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import me.angelstoyanov.sporton.management.feedback.client.RestClientWrapper;
import me.angelstoyanov.sporton.management.feedback.exception.CommentNotExistsException;
import me.angelstoyanov.sporton.management.feedback.model.Comment;
import me.angelstoyanov.sporton.management.feedback.model.StorageEntity;
import me.angelstoyanov.sporton.management.feedback.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterForReflection
public class CommentResource {

    @Inject
    protected CommentRepository commentRepository;

    @Inject
    protected RestClientWrapper restClient;

    @POST
    @ResponseStatus(201)
    @Consumes({"image/jpeg,image/png,image/bmp"})
    @Path("/comment/")
    public RestResponse<Comment> createCommentNew(File attachment,
                                                  @NotNull @QueryParam("user_id") String userId,
                                                  @NotNull @QueryParam("pitch_id") String pitchId,
                                                  @NotNull @QueryParam("content") String content) {
        try {
            UUID.fromString(userId);
        } catch (IllegalArgumentException exception) {
            RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
        Comment comment = new Comment(userId, new ObjectId(pitchId), content, null);
        comment = commentRepository.addComment(comment);
        String attachmentName = uploadAttachment(attachment, comment.getId().toString());
        if (attachmentName != null) {
            comment = commentRepository.updateAttachment(comment.getId(), attachmentName);
        }
        return RestResponse.ResponseBuilder.ok(comment).build();

    }

    @GET
    @ResponseStatus(200)
    @Path("/comment/{id}")
    public RestResponse<Comment> getComment(@PathParam("id") String id) {
        Comment comment = commentRepository.findById(new ObjectId(id));
        if (comment == null) {
            return RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.NOT_FOUND).build();
        }
        return RestResponse.ResponseBuilder.ok(comment).build();

    }

    @PATCH
    @ResponseStatus(200)
    @Consumes({"image/jpeg,image/png,image/bmp"})
    @Path("/comment/{id}")
    public RestResponse<Comment> updateComment(@PathParam("id") String id,
                                               File inputAttachment,
                                               @QueryParam("content") String content) {
        Comment comment = commentRepository.findById(new ObjectId(id));
        if (comment == null) {
            return RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.NOT_FOUND).build();
        }
        if (inputAttachment != null) {
            String attachment = uploadAttachment(inputAttachment, comment.getId().toString());
            comment = commentRepository.updateAttachment(new ObjectId(id), attachment);
        }
        if (content != null) {
            comment = commentRepository.updateContent(new ObjectId(id), content);
        }
        return RestResponse.ResponseBuilder.ok(comment).build();

    }

    @DELETE
    @ResponseStatus(204)
    @Path("/comment/{id}")
    public RestResponse<Comment> deleteComment(@PathParam("id") String id) {
        try {
            commentRepository.deleteCommentById(new ObjectId(id));
            return RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.NO_CONTENT).build();
        } catch (CommentNotExistsException e) {
            return RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.NOT_FOUND).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/comment/all")
    public RestResponse<List<Comment>> getCommentsByPitchId(@QueryParam("pitchId") String id) {
        List<Comment> comments = commentRepository.findCommentsByPitchId(new ObjectId(id));
        return RestResponse.ResponseBuilder.ok(comments).build();
    }

    private String uploadAttachment(File inputAttachment, String commentId) {
        String attachment = null;
        if (inputAttachment != null && commentId != null) {
            attachment = restClient.uploadBlob(commentId, StorageEntity.IMAGE_COMMENT, inputAttachment);
            if (attachment.contains("successfully")) {
                attachment = "/blob/c/" + commentId;
            }
        }
        return attachment;
    }

}
