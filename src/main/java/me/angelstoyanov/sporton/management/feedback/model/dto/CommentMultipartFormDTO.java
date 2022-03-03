package me.angelstoyanov.sporton.management.feedback.model.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.resteasy.reactive.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;

@RegisterForReflection
public class CommentMultipartFormDTO {

    @FormParam("content")
    @PartType(MediaType.TEXT_PLAIN)
    private String content;

    @FormParam("pitch_id")
    @PartType(MediaType.TEXT_PLAIN)
    private String pitchId;

    @FormParam("user_id")
    @PartType(MediaType.TEXT_PLAIN)
    private String userId;

    @FormParam("attachment")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private File attachment;

    public CommentMultipartFormDTO(String content, String pitchId, String userId, File attachment) {
        this.content = content;
        this.pitchId = pitchId;
        this.userId = userId;
        this.attachment = attachment;
    }

    public CommentMultipartFormDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "CommentMultipartFormDTO{" +
                "content='" + content + '\'' +
                ", pitchId='" + pitchId + '\'' +
                ", userId='" + userId + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}
