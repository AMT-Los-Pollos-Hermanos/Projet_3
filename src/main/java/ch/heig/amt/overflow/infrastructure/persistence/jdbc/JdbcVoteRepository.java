package ch.heig.amt.overflow.infrastructure.persistence.jdbc;

import ch.heig.amt.overflow.domain.ContentId;
import ch.heig.amt.overflow.domain.question.QuestionId;
import ch.heig.amt.overflow.domain.user.UserId;
import ch.heig.amt.overflow.domain.vote.IVoteRepository;
import ch.heig.amt.overflow.domain.vote.Vote;
import ch.heig.amt.overflow.domain.vote.VoteId;
import ch.heig.amt.overflow.domain.vote.status.VoteDown;
import ch.heig.amt.overflow.domain.vote.status.VoteUp;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@ApplicationScoped
@Named("JdbcVoteRepository")
public class JdbcVoteRepository implements IVoteRepository {

    @Resource(lookup = "jdbc/OverflowDS")
    private DataSource dataSource;

    @Override
    public void save(Vote entity) {
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement;

            preparedStatement = con.prepareStatement("SELECT COUNT(*) FROM votes WHERE id = ?");
            preparedStatement.setString(1, entity.getId().toString());
            ResultSet rs = preparedStatement.executeQuery();

            int size = 0;
            if (rs.next()) {
                size = rs.getInt(1);
            }

            if (size == 0) {
                // Create comment
                preparedStatement = con.prepareStatement("INSERT INTO votes (id, content_id, user_id, state) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, entity.getId().toString());
                preparedStatement.setString(2, entity.getContentId().toString());
                preparedStatement.setString(3, entity.getUserId().toString());
                preparedStatement.setString(3, entity.getStatus().getVote());

                int rows = preparedStatement.executeUpdate();
                if (rows == 0) {
                    throw new RuntimeException("Error while adding new vote to the database");
                }
            } else {
                // Update comment
                preparedStatement = con.prepareStatement("UPDATE votes SET content_id = ?, user_id = ?, state = ? WHERE id = ?;");
                preparedStatement.setString(1, entity.getContentId().toString());
                preparedStatement.setString(2, entity.getUserId().toString());
                preparedStatement.setString(3, entity.getStatus().getVote());
                preparedStatement.setString(4, entity.getId().toString());

                int rows = preparedStatement.executeUpdate();
                if (rows == 0) {
                    throw new RuntimeException("Error while updating new vote to the database");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding/updating vote to the database");
        }
    }

    @Override
    public void remove(VoteId id) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("DELETE FROM votes WHERE id = ?");
            preparedStatement.setString(1, id.toString());
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No answer deleted, answer with id '" + id.toString() + "' not found in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL error");
        }
    }

    @Override
    public Optional<Vote> findById(VoteId id) {
        String sql = "SELECT id, content_id, user_id, state FROM votes WHERE id = ?";

        Collection<Vote> votes = resultToVotes(sql, Collections.singletonList(id.toString()));
        if (votes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(votes.iterator().next());
        }
    }

    @Override
    public Collection<Vote> findAll() {
        String sql = "SELECT id, content_id, user_id, state FROM votes";
        return resultToVotes(sql, Collections.emptyList());
    }

    @Override
    public Collection<Vote> findByUserId(UserId userId) {
        String sql = "SELECT id, content_id, user_id, state FROM votes WHERE user_id = ?";
        return resultToVotes(sql, Collections.singletonList(userId.toString()));
    }

    @Override
    public Collection<Vote> findByUserIdAndQuestionId(UserId userId, QuestionId questionId) {
        String sql = "SELECT votes.id, votes.content_id, votes.user_id, votes.state FROM comments " +
                "INNER JOIN main_contents on comments.main_content_id = main_contents.content_id " +
                "INNER JOIN votes ON votes.content_id = comments.content_id " +
                "WHERE main_content_id = ? " +
                "AND votes.user_id = ? " +
                "UNION " +
                "SELECT votes.id, votes.content_id, votes.user_id, votes.state FROM answers " +
                "INNER JOIN votes ON votes.content_id = answers.content_id " +
                "WHERE answers.question_id = ? " +
                "AND votes.user_id = ? " +
                "UNION " +
                "SELECT votes.id, votes.content_id, votes.user_id, votes.state FROM questions " +
                "INNER JOIN votes ON votes.content_id = questions.content_id " +
                "WHERE questions.content_id = ? " +
                "AND votes.user_id = ? " +
                "UNION " +
                "SELECT votes.id, votes.content_id, votes.user_id, votes.state FROM comments " +
                "INNER JOIN main_contents on comments.main_content_id = main_contents.content_id " +
                "INNER JOIN votes ON votes.content_id = comments.content_id " +
                "WHERE main_content_id IN (SELECT answers.content_id FROM answers " +
                "                          WHERE answers.question_id = ?) " +
                "AND votes.user_id = ?";

        return resultToVotes(sql, Arrays.asList(
                questionId.toString(),
                userId.toString(),
                questionId.toString(),
                userId.toString(),
                questionId.toString(),
                userId.toString(),
                questionId.toString(),
                userId.toString()
        ));
    }

    @Override
    public Optional<Vote> findByUserIdAndContentId(UserId userId, ContentId contentId) {
        String sql = "SELECT id, content_id, user_id, state FROM votes WHERE user_id = ? AND content = ?";

        Collection<Vote> votes = resultToVotes(sql, Arrays.asList(userId.toString(), contentId.toString()));
        if (votes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(votes.iterator().next());
        }
    }

    private Collection<Vote> resultToVotes(String query, List<String> parameters) {
        List<Vote> votes = new ArrayList<>();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                statement.setString(i + 1, parameters.get(i));
            }
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                votes.add(
                        Vote.builder()
                        .id(new VoteId(rs.getString("id")))
                        .contentId(new ContentId(rs.getString("content_id")))
                        .userId(new UserId(rs.getString("user_id")))
                        .status(rs.getString("state").equals(new VoteUp().getVote()) ? new VoteUp() : new VoteDown())
                        .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO handle SQL exception
        }
        return votes;
    }

}