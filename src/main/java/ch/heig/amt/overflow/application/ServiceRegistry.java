/*
 * AMT : Project 1 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 29.10.2020
 */

package ch.heig.amt.overflow.application;

import ch.heig.amt.overflow.application.answer.AnswerFacade;
import ch.heig.amt.overflow.application.auth.AuthFacade;
import ch.heig.amt.overflow.application.comment.CommentFacade;
import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.application.gamification.IGamificationEngine;
import ch.heig.amt.overflow.application.question.QuestionFacade;
import ch.heig.amt.overflow.application.vote.VoteFacade;
import ch.heig.amt.overflow.domain.answer.IAnswerRepository;
import ch.heig.amt.overflow.domain.comment.ICommentRepository;
import ch.heig.amt.overflow.domain.question.IQuestionRepository;
import ch.heig.amt.overflow.domain.user.IUserRepository;
import ch.heig.amt.overflow.domain.vote.IVoteRepository;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class ServiceRegistry {

    @Inject
    @Named("JdbcQuestionRepository")
    private IQuestionRepository questionRepository;

    @Inject
    @Named("JdbcUserRepository")
    private IUserRepository userRepository;

    @Inject
    @Named("JdbcAnswerRepository")
    private IAnswerRepository answerRepository;

    @Inject
    @Named("JdbcCommentRepository")
    private ICommentRepository commentRepository;

    @Inject
    @Named("JdbcVoteRepository")
    private IVoteRepository voteRepository;

    @Inject
    private IGamificationEngine gamificationEngine;

    private QuestionFacade questionFacade;
    private AuthFacade authFacade;
    private AnswerFacade answerFacade;
    private CommentFacade commentFacade;
    private VoteFacade voteFacade;
    private GamificationFacade gamificationFacade;

    @PostConstruct
    private void init() {
        gamificationFacade = new GamificationFacade();
        authFacade = new AuthFacade(userRepository);
        questionFacade = new QuestionFacade(questionRepository, gamificationEngine);
        answerFacade = new AnswerFacade(answerRepository, gamificationEngine);
        commentFacade = new CommentFacade(commentRepository, gamificationEngine);
        voteFacade = new VoteFacade(voteRepository, gamificationEngine);
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public AnswerFacade getAnswerFacade() {
        return answerFacade;
    }

    public CommentFacade getCommentFacade() {
        return commentFacade;
    }

    public VoteFacade getVoteFacade() {
        return voteFacade;
    }

    public AuthFacade getAuthFacade() {
        return authFacade;
    }

    public GamificationFacade getGamificationFacade() {
        return gamificationFacade;
    }

}
