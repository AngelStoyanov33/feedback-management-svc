package me.angelstoyanov.sporton.management.feedback.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.runtime.annotations.RegisterForReflection;
import me.angelstoyanov.sporton.management.feedback.exception.RatingAlreadyExistsException;
import me.angelstoyanov.sporton.management.feedback.exception.RatingNotExistsException;
import me.angelstoyanov.sporton.management.feedback.model.Rating;
import me.angelstoyanov.sporton.management.feedback.model.dto.EditRatingDTO;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Named("RatingRepository")
@ApplicationScoped
@RegisterForReflection
public class RatingRepository implements PanacheMongoRepository<Rating> {

    public Rating addRating(Rating rating) throws RatingAlreadyExistsException {
        if (findRatingByUserIdAndPitchId(rating.getUserId().toString(), rating.getPitchId().toString()) != null) {
            throw new RatingAlreadyExistsException("Rating from user with ID" + rating.getUserId()
                    + "for pitch with ID " + rating.getPitchId() + " already exists.");
        }
        persist(rating);
        return rating;
    }

    public Rating findRatingByUserIdAndPitchId(String userId, String pitchId) {
        return find(String.format(Locale.US, "{\"pitch_id\":ObjectId(\"%s\"),\"user_id\":ObjectId(\"%s\")}", pitchId, userId)).firstResult();
    }

    public List<Rating> getRatingsByPitchId(String pitchId) {
        return find(String.format(Locale.US, "{\"pitch_id\":ObjectId(\"%s\")}", pitchId)).list();
    }

    //TODO: Optimize this method to use a single query (aggregation)
    public double getRatingByPitchId(String pitchId) {
        long ratings = count(String.format(Locale.US, "{\"pitch_id\":ObjectId(\"%s\")}", pitchId));
        if (ratings != 0) {
            AtomicReference<Double> sumBuffer = new AtomicReference<>(0d);
            stream(String.format(Locale.US, "{\"pitch_id\":ObjectId(\"%s\")}", pitchId)).forEach(rating -> sumBuffer.updateAndGet(v -> v + rating.getRating()));
            return sumBuffer.get() / ratings;
        } else {
            return ratings;
        }
    }

    public Rating updateRating(ObjectId ratingId, EditRatingDTO rating) throws RatingNotExistsException {
        Rating ratingToUpdate = findById(ratingId);
        if (ratingToUpdate != null && rating.getRating() != 0) {
            ratingToUpdate.setRating(rating.getRating());
            update(ratingToUpdate);
        } else {
            throw new RatingNotExistsException("Rating with ID " + ratingId + " does not exist.");
        }
        return ratingToUpdate;
    }

    public void deleteRatingById(ObjectId ratingId) throws RatingNotExistsException {
        if (findById(ratingId) == null) {
            throw new RatingNotExistsException("Rating with ID " + ratingId + " does not exist.");
        }
        deleteById(ratingId);
    }

    public void deleteRating(Rating rating) throws RatingNotExistsException {
        Rating ratingToDelete = findById(rating.getId());
        if (ratingToDelete == null) {
            throw new RatingNotExistsException("Rating with ID " + rating.getId() + " does not exist.");
        }
        delete(ratingToDelete);
    }

}
