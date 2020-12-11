/*
 * AMT : Project 1 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 29.10.2020
 */

package ch.heig.amt.overflow.application.answer;

import ch.heig.amt.overflow.application.auth.UserDTO;
import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.domain.answer.Answer;
import ch.heig.amt.overflow.domain.answer.IAnswerRepository;
import ch.heig.amt.overflow.application.gamification.EventDTO;
import ch.heig.amt.overflow.domain.question.QuestionId;
import ch.heig.amt.overflow.domain.user.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnswerFacade {

    @Inject
    private IAnswerRepository answerRepository;

    @Inject
    private GamificationFacade gamificationFacade;

    // add answer to the answerRepository throw exception if incomplete
    public void addNewAnswer(NewAnswerCommand command) {
        if (!command.getContent().isEmpty()) {
            Answer submittedAnswer = Answer.builder()
                    .content(command.getContent())
                    .author(User.builder().id(command.getAuthorId()).build())
                    .questionId(command.getQuestionId())
                    .build();
            answerRepository.save(submittedAnswer);

            // Send event to gamification engine
            gamificationFacade.sendEvent(EventDTO.builder()
                    .userId(submittedAnswer.getAuthor().getId())
                    .type("answer")
                    .properties(Map.of("type", "add", "quantity", "1"))
                    .build()
            );

        } else {
            throw new IllegalArgumentException("Le contenu est obligatoire");
        }
    }

    // search answer with corresponding ID and return answerDTO
    public AnswersDTO getAnswerFromQuestionId(QuestionId questionId) {
        Collection<Answer> answers = answerRepository.findByQuestionId(questionId);

        List<AnswersDTO.AnswerDTO> mapper = answers.stream().map(answer ->
                AnswersDTO.AnswerDTO.builder()
                        .answerId(answer.getId())
                        .content(answer.getContent())
                        .createdAt(answer.getCreatedAt())
                        .nbVotes(answer.getNbVotes())
                        .author(UserDTO.builder()
                                .id(answer.getAuthor().getId())
                                .username(answer.getAuthor().getUsername())
                                .firstName(answer.getAuthor().getFirstName())
                                .lastName(answer.getAuthor().getLastName())
                                .email(answer.getAuthor().getEmail())
                                .build())
                        .build())
                .collect(Collectors.toList());

        return AnswersDTO.builder()
                .answers(mapper)
                .build();
    }

}
