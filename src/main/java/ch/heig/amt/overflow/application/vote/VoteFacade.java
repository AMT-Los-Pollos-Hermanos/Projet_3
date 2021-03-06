/*
 * AMT : Project 1 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 29.10.2020
 */

package ch.heig.amt.overflow.application.vote;

import ch.heig.amt.overflow.application.gamification.IGamificationEngine;
import ch.heig.amt.overflow.domain.ContentId;
import ch.heig.amt.overflow.application.gamification.EventDTO;
import ch.heig.amt.overflow.domain.user.UserId;
import ch.heig.amt.overflow.domain.vote.IVoteRepository;
import ch.heig.amt.overflow.domain.vote.Vote;
import ch.heig.amt.overflow.domain.vote.VoteId;
import ch.heig.amt.overflow.domain.vote.VoteStatus;

import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoteFacade {

    private final IVoteRepository voteRepository;
    private final IGamificationEngine gamificationEngine;

    public VoteFacade(IVoteRepository voteRepository, IGamificationEngine gamificationEngine) {
        this.voteRepository = voteRepository;
        this.gamificationEngine = gamificationEngine;
    }

    public void addNewVote(NewVoteCommand command) {
        if (!isVoteCancelled(command.getUserId(), command.getContentId(), command.getStatus())) {
            Vote submittedVote = Vote.builder()
                    .status(command.getStatus())
                    .userId(command.getUserId())
                    .contentId(command.getContentId())
                    .build();
            voteRepository.save(submittedVote);

            // Send event to gamification engine
            gamificationEngine.sendEvent(EventDTO.builder()
                    .userId(submittedVote.getUserId().getId())
                    .type("vote")
                    .properties(Map.of("status", submittedVote.getStatus().name(), "quantity", "1"))
                    .build()
            );
        }
    }

    public void deleteVote(VoteId voteId) {
        voteRepository.remove(voteId);
    }

    public void deleteVote(UserId userId, ContentId contentId) {
        Optional<Vote> vote = voteRepository.findByUserIdAndContentId(userId, contentId);
        if (vote.isPresent()) {
            voteRepository.remove(vote.get().getId());
        } else {
            throw new RuntimeException("Vote non trouvé");
        }
    }

    public VotesDTO getUserVotesInQuestionList(UserId userId) {
        return mapVoteDTO(voteRepository.findByUserId(userId));
    }

    private boolean isVoteCancelled(UserId userId, ContentId contentId, VoteStatus status) {
        Optional<Vote> vote = voteRepository.findByUserIdAndContentId(userId, contentId);

        if (vote.isPresent()) {
            deleteVote(vote.get().getId());
            return vote.get().getStatus().equals(status);
        } else {
            return false;
        }
    }

    private VotesDTO mapVoteDTO(Collection<Vote> votes) {
        List<VotesDTO.VoteDTO> mapper = votes.stream().map(vote ->
                VotesDTO.VoteDTO.builder()
                        .contentId(vote.getContentId())
                        .status(vote.getStatus())
                        .build())
                .collect(Collectors.toList());

        return VotesDTO.builder()
                .votes(mapper)
                .build();
    }

}