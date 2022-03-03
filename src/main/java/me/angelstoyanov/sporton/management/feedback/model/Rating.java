package me.angelstoyanov.sporton.management.feedback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonRootName("rating")
@JsonPropertyOrder({"id", "created_at", "user_id", "pitch_id", "rating"})
@MongoEntity(collection = "Rating", database = "sporton-dev-db")
@RegisterForReflection
public class Rating {
    @JsonProperty("id")
    private ObjectId id;

    @JsonProperty("created_at")
    @BsonProperty("created_at")
    private String createdAt = ZonedDateTime.now(ZoneId.of("Europe/Sofia")).toInstant().toString();

    @JsonProperty("user_id")
    @BsonProperty("user_id")
    private ObjectId userId;

    @JsonProperty("pitch_id")
    @BsonProperty("pitch_id")
    private ObjectId pitchId;

    @JsonProperty("grade")
    @BsonProperty("grade")
    private Double rating;

    public Rating(ObjectId id, String createdAt, ObjectId userId, ObjectId pitchId, Double rating) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.pitchId = pitchId;
        this.rating = rating;
    }

    public Rating(ObjectId id, ObjectId userId, ObjectId pitchId, Double rating) {
        this.id = id;
        this.userId = userId;
        this.pitchId = pitchId;
        this.rating = rating;
    }

    public Rating() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getPitchId() {
        return pitchId;
    }

    public void setPitchId(ObjectId pitchId) {
        this.pitchId = pitchId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
