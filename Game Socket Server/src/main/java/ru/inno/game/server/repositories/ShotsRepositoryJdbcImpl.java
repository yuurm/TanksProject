package ru.inno.game.server.repositories;

import ru.inno.game.server.models.Shot;

import javax.sql.DataSource;
import java.sql.*;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class ShotsRepositoryJdbcImpl implements ShotsRepository {

    private DataSource dataSource;

    public ShotsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //language=SQL
    private static final String SQL_INSERT = "insert into shot(datetime, shooter, target, game_id) values (?, ?, ?, ?)";

    @Override
    public void save(Shot shot) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, shot.getDateTime().toString());
            statement.setLong(2, shot.getShooter().getId());
            statement.setLong(3, shot.getTarget().getId());
            statement.setLong(4, shot.getGame().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert player");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
