package me.angelstoyanov.sporton.management.feedback.resource;

import io.quarkus.runtime.annotations.RegisterForReflection;
import me.angelstoyanov.sporton.management.feedback.client.RestClientWrapper;
import me.angelstoyanov.sporton.management.feedback.exception.CommentNotExistsException;
import me.angelstoyanov.sporton.management.feedback.model.Comment;
import me.angelstoyanov.sporton.management.feedback.model.StorageEntity;
import me.angelstoyanov.sporton.management.feedback.model.dto.CommentMultipartFormDTO;
import me.angelstoyanov.sporton.management.feedback.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/comment")
    public RestResponse<Comment> createComment(@MultipartForm CommentMultipartFormDTO commentMultipartFormDTO) {
        String attachment = null;
        Comment comment = new Comment(new ObjectId(commentMultipartFormDTO.getUserId()),
                new ObjectId(commentMultipartFormDTO.getPitchId()), commentMultipartFormDTO.getContent(), null);
        comment = commentRepository.addComment(comment);
        attachment = uploadAttachment(commentMultipartFormDTO, comment.getId().toString());
        if (attachment != null) {
            comment = commentRepository.updateAttachment(comment.getId(), attachment);
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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/comment/{id}")
    public RestResponse<Comment> updateComment(@PathParam("id") String id, @MultipartForm CommentMultipartFormDTO commentMultipartFormDTO) {
        Comment comment = commentRepository.findById(new ObjectId(id));
        if (comment == null) {
            return RestResponse.ResponseBuilder.ok((Comment) null).status(RestResponse.Status.NOT_FOUND).build();
        }
        if (commentMultipartFormDTO.getAttachment() != null) {
            commentMultipartFormDTO.setPitchId(id);
            String attachment = uploadAttachment(commentMultipartFormDTO, comment.getId().toString());
            comment = commentRepository.updateAttachment(new ObjectId(id), attachment);
        }
        if (commentMultipartFormDTO.getContent() != null) {
            comment = commentRepository.updateContent(new ObjectId(id), commentMultipartFormDTO.getContent());
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

    private String uploadAttachment(CommentMultipartFormDTO commentMultipartFormDTO, String commentId) {
        String attachment = null;
        if (commentMultipartFormDTO.getAttachment() != null && commentId != null) {
            attachment = restClient.uploadBlob(commentId, StorageEntity.IMAGE_COMMENT, commentMultipartFormDTO.getAttachment());
            if (attachment.contains("successfully")) {
                attachment = "/blob/c/" + commentId;
            }
        }
        return attachment;
    }

}
