package me.angelstoyanov.sporton.management.feedback.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.runtime.annotations.RegisterForReflection;
import me.angelstoyanov.sporton.management.feedback.exception.CommentNotExistsException;
import me.angelstoyanov.sporton.management.feedback.model.Comment;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Locale;

@Named("CommentRepository")
@ApplicationScoped
@RegisterForReflection
public class CommentRepository implements PanacheMongoRepository<Comment> {
    public Comment addComment(Comment comment) {
        persist(comment);
        return comment;
    }

    public void deleteComment(Comment comment) throws CommentNotExistsException {
        if (findById(comment.getId()) == null) {
            throw new CommentNotExistsException("Comment with id " + comment.getId() + " does not exist");
        }
        delete(comment);
    }

    public void deleteCommentById(ObjectId id) throws CommentNotExistsException {
        if (findById(id) == null) {
            throw new CommentNotExistsException("Comment with id " + id + " does not exist");
        }
        deleteById(id);
    }

    public Comment updateAttachment(ObjectId id, String attachment) {
        Comment comment = findById(id);
        if (comment == null) {
            throw new CommentNotExistsException("Comment with id " + id + " does not exist");
        }
        comment.setAttachment(attachment);
        update(comment);
        return comment;
    }

    public Comment updateContent(ObjectId id, String content) {
        Comment comment = findById(id);
        if (comment == null) {
            throw new CommentNotExistsException("Comment with id " + id + " does not exist");
        }
        comment.setContent(content);
        update(comment);
        return comment;
    }

    public List<Comment> findCommentsByPitchId(ObjectId pitchId) {
        return find(String.format(Locale.US, "{\"pitch_id\":ObjectId(\"%s\")}", pitchId)).list();
    }


}
