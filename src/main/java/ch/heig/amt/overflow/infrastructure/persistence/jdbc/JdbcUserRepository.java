/*
 * AMT : Project 1 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 29.10.2020
 */

package ch.heig.amt.overflow.infrastructure.persistence.jdbc;

import ch.heig.amt.overflow.domain.user.IUserRepository;
import ch.heig.amt.overflow.domain.user.User;
import ch.heig.amt.overflow.domain.user.UserId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Named("JdbcUserRepository")
public class JdbcUserRepository implements IUserRepository {

    @Resource(lookup = "jdbc/OverflowDS")
    private DataSource dataSource;

    @Override
    public Optional<User> findByUsername(String username) {
        User user;
        try {
            String sqlQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            user = getUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problème lié à la base de données");
        }

        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void save(User entity) {
        try {
            PreparedStatement select = dataSource.getConnection().prepareStatement("SELECT COUNT(*) FROM users WHERE id = ?");
            select.setString(1, entity.getId().toString());
            ResultSet rs = select.executeQuery();
            int size = 0;
            if (rs.next()) {
                size = rs.getInt(1);
            }
            if (size == 0) {
                // Create user
                PreparedStatement create = dataSource
                        .getConnection()
                        .prepareStatement("INSERT INTO users (id, username, password, email, first_name, last_name, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?)");
                int i = 1;
                create.setString(i++, entity.getId().toString());
                create.setString(i++, entity.getUsername());
                create.setString(i++, entity.getEncryptedPassword());
                create.setString(i++, entity.getEmail());
                create.setString(i++, entity.getFirstName());
                create.setString(i++, entity.getLastName());
                create.setBoolean(i, entity.getIsAdmin());
                int rows = create.executeUpdate();
                if (rows == 0) {
                    throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur dans la base de données");
                }
            } else {
                // Update user
                PreparedStatement create = dataSource
                        .getConnection()
                        .prepareStatement("UPDATE users SET username = ?, password = ?, email = ?, first_name = ?, last_name = ?, is_admin = ? WHERE id = ?");
                int i = 1;
                create.setString(i++, entity.getUsername());
                create.setString(i++, entity.getEncryptedPassword());
                create.setString(i++, entity.getEmail());
                create.setString(i++, entity.getFirstName());
                create.setString(i++, entity.getLastName());
                create.setBoolean(i++, entity.getIsAdmin());
                create.setString(i, entity.getId().toString());
                int rows = create.executeUpdate();
                if (rows == 0) {
                    throw new RuntimeException("Erreur lors de le mise à jour de l'utilisateur dans la base de données");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problème lié à la base de données");
        }
    }

    @Override
    public void remove(UserId id) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("DELETE FROM users WHERE id = ?");
            preparedStatement.setString(1, id.toString());
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Aucun utilisateur supprimé, l'utilisateur avec l'ID '" + id.toString() + "' n'a pas été trouvé");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problème lié à la base de données");
        }
    }

    @Override
    public Optional<User> findById(UserId id) {
        User user = null;

        try {
            String sqlQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();

            user = getUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problème lié à la base de données");
        }

        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> findAll() {
        ArrayList<User> users = new ArrayList<>();

        try {
            String sqlQuery = "SELECT * FROM users";
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                users.add(getUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problème lié à la base de données");
        }

        return users;
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()) {
            user = User.builder()
                    .id(new UserId(rs.getString("id")))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .email(rs.getString("email"))
                    .username(rs.getString("username"))
                    .encryptedPassword(rs.getString("password"))
                    .isAdmin(rs.getBoolean("is_admin"))
                    .build();
        }
        return user;
    }

}
