package me.angelstoyanov.sporton.management.feedback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

@JsonRootName("rating")
@JsonPropertyOrder({"id", "user_id", "average_rating"})
@RegisterForReflection
public class AverageRatingDTO {
    @JsonProperty("pitch_id")
    private ObjectId pitchId;

    @JsonProperty("average_rating")
    private Double averageRating;

    public AverageRatingDTO(ObjectId pitchId) {
        this.pitchId = pitchId;
    }

    public AverageRatingDTO(ObjectId pitchId, Double averageRating) {
        this.pitchId = pitchId;
        this.averageRating = averageRating;
    }

    public AverageRatingDTO() {
    }

    public ObjectId getPitchId() {
        return pitchId;
    }

    public void setPitchId(ObjectId pitchId) {
        this.pitchId = pitchId;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
