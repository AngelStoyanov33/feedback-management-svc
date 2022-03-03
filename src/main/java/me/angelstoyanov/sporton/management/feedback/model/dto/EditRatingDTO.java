package me.angelstoyanov.sporton.management.feedback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

@JsonRootName("rating")
@JsonPropertyOrder({"id", "rating"})
@RegisterForReflection
public class EditRatingDTO {
    @JsonProperty("id")
    private ObjectId id;

    @JsonProperty("grade")
    private Double rating;


    public EditRatingDTO(ObjectId id, Double rating) {
        this.id = id;
        this.rating = rating;
    }

    public EditRatingDTO() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
