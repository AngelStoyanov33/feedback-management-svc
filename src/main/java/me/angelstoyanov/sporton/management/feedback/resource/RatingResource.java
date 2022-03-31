package me.angelstoyanov.sporton.management.feedback.resource;

import io.quarkus.runtime.annotations.RegisterForReflection;
import me.angelstoyanov.sporton.management.feedback.exception.InvalidUserIdException;
import me.angelstoyanov.sporton.management.feedback.exception.RatingAlreadyExistsException;
import me.angelstoyanov.sporton.management.feedback.exception.RatingNotExistsException;
import me.angelstoyanov.sporton.management.feedback.model.Rating;
import me.angelstoyanov.sporton.management.feedback.model.dto.AverageRatingDTO;
import me.angelstoyanov.sporton.management.feedback.model.dto.EditRatingDTO;
import me.angelstoyanov.sporton.management.feedback.repository.RatingRepository;
import org.bson.types.ObjectId;
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
public class RatingResource {

    @Inject
    protected RatingRepository ratingRepository;

    @POST
    @ResponseStatus(201)
    @Path("/rating")
    public RestResponse<Rating> createRating(Rating rating,
                                             @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            if (!rating.getUserId().equals(userId)) {
                return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            ratingRepository.addRating(rating);
            return RestResponse.ResponseBuilder.ok(rating).build();
        } catch (RatingAlreadyExistsException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.CONFLICT).build();
        } catch (InvalidUserIdException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/rating")
    public RestResponse<Rating> getRatingByUserAndPitchId(@QueryParam("user_id") String userId,
                                                          @QueryParam("pitch_id") String pitchId) {
        try {

            Rating rating = ratingRepository.findRatingByUserIdAndPitchId(userId, pitchId);
            return RestResponse.ResponseBuilder.ok(rating).build();
        } catch (RatingNotExistsException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.NOT_FOUND).build();
        } catch (InvalidUserIdException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/rating/{id}")
    public RestResponse<Rating> getRating(@PathParam("id") String id) {
        Rating rating = ratingRepository.findById(new ObjectId(id));
        if (rating == null) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.NOT_FOUND).build();
        }
        return RestResponse.ResponseBuilder.ok(rating).build();
    }

    @GET
    @ResponseStatus(200)
    @Path("/rating/average")
    public RestResponse<AverageRatingDTO> getAverageRating(@QueryParam("pitch_id") String pitchId) {
        if (pitchId == null) {
            return RestResponse.ResponseBuilder.ok((AverageRatingDTO) null).build();
        }
        AverageRatingDTO averageRatingDTO = new AverageRatingDTO(new ObjectId(pitchId));
        Double averageRating = ratingRepository.getRatingByPitchId(pitchId);
        averageRatingDTO.setAverageRating(averageRating);
        return RestResponse.ResponseBuilder.ok(averageRatingDTO).build();
    }

    @GET
    @ResponseStatus(200)
    @Path("/rating/all")
    public RestResponse<List<Rating>> getAllRatings(@QueryParam("pitch_id") String pitchId) {
        List<Rating> ratings = ratingRepository.getRatingsByPitchId(pitchId);
        return RestResponse.ResponseBuilder.ok(ratings).build();
    }

    @DELETE
    @ResponseStatus(204)
    @Path("/rating/{id}")
    public RestResponse<Rating> deleteRating(@PathParam("id") String id,
                                             @HeaderParam("X-Requesting-User-Role") String userRole,
                                             @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            Rating rating = ratingRepository.findById(new ObjectId(id));
            if (!rating.getUserId().equals(userId) && !userRole.equals("ADMIN")) {
                return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            ratingRepository.deleteRatingById(new ObjectId(id));
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.NO_CONTENT).build();
        } catch (RatingNotExistsException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.NOT_FOUND).build();
        }
    }

    @PATCH
    @ResponseStatus(200)
    @Path("/rating/{id}")
    public RestResponse<Rating> updateRatingGrade(@PathParam("id") String id,
                                                  EditRatingDTO editRatingDTO,
                                                  @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            Rating rating = ratingRepository.findById(new ObjectId(id));
            if (!rating.getUserId().equals(userId)) {
                return RestResponse.ResponseBuilder.ok((Rating) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            rating = ratingRepository.updateRating(new ObjectId(id), editRatingDTO);
            return RestResponse.ResponseBuilder.ok(rating).build();
        } catch (RatingNotExistsException e) {
            return RestResponse.ResponseBuilder.ok((Rating) null).build();
        }
    }

}



