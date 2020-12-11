/*
 * AMT : Project 1 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 29.10.2020
 */

package ch.heig.amt.overflow.application.comment;

import ch.heig.amt.overflow.application.auth.UserDTO;
import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.domain.MainContentId;
import ch.heig.amt.overflow.domain.comment.Comment;
import ch.heig.amt.overflow.domain.comment.ICommentRepository;
import ch.heig.amt.overflow.application.gamification.EventDTO;
import ch.heig.amt.overflow.domain.user.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentFacade {

    @Inject
    private ICommentRepository commentRepository;

    @Inject
    private GamificationFacade gamificationFacade;

    // add comment to the repository throw exception if incomplete
    public void addNewComment(NewCommentCommand command) {
        if (!command.getContent().isEmpty()) {
            Comment submittedComment = Comment.builder()
                    .content(command.getContent())
                    .author(User.builder().id(command.getAuthorId()).build())
                    .mainContentId(command.getMainContentId())
                    .build();
            commentRepository.save(submittedComment);

            // Send event to gamification engine
            gamificationFacade.sendEvent(EventDTO.builder()
                    .userId(submittedComment.getAuthor().getId())
                    .type("comment")
                    .properties(Map.of("type", "add", "quantity", "1"))
                    .build()
            );
        } else {
            throw new IllegalArgumentException("Le contenu est obligatoire");
        }
    }

    // return list of commentsDTO  with the corresponding ID
    public CommentsDTO getCommentFromMainContentId(MainContentId mainContentId) {
        Collection<Comment> comments = commentRepository.findByMainContentId(mainContentId);

        List<CommentsDTO.CommentDTO> mapper = comments.stream().map(comment ->
                CommentsDTO.CommentDTO.builder()
                        .commentId(comment.getId())
                        .mainContentId(comment.getMainContentId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .nbVotes(comment.getNbVotes())
                        .author(UserDTO.builder()
                                .id(comment.getAuthor().getId())
                                .username(comment.getAuthor().getUsername())
                                .firstName(comment.getAuthor().getFirstName())
                                .lastName(comment.getAuthor().getLastName())
                                .email(comment.getAuthor().getEmail())
                                .build())
                        .build())
                .collect(Collectors.toList());

        return CommentsDTO.builder()
                .comments(mapper)
                .build();
    }

}
