package alamsyahhutama.database.repository;

import alamsyahhutama.database.ConnectionUtil;
import alamsyahhutama.database.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements ComentRepository {


    @Override
    public void insert(Comment comment) {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            String sql = "INSERT INTO comments (email, comment) values (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, comment.getEmail());
                statement.setString(2, comment.getComment());
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()){
                    if (resultSet.next()){
                        comment.setId(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException exception){
            throw new RuntimeException();
        }
    }

    @Override
    public Comment findById(Integer id) {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            String sql = "SELECT * FROM comments where id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()){
                        return new Comment(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("comment")
                        );
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException exception){
            throw new RuntimeException();
        }
    }

    @Override
    public List<Comment> findAll() {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            String sql = "SELECT * FROM comments";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    List<Comment> comments = new ArrayList<>();
                    while (resultSet.next()){
                        comments.add(new Comment(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("comment")
                        ));
                    }

                    return comments;
                }
            }
        } catch (SQLException exception){
            throw new RuntimeException();
        }
    }

    @Override
    public List<Comment> findAllByEmail(String email) {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            String sql = "SELECT * FROM comments where email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()){
                    List<Comment> comments = new ArrayList<>();
                    while (resultSet.next()){
                        comments.add(new Comment(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("comment")
                        ));
                    }
                    return comments;
                }
            }

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }
}
