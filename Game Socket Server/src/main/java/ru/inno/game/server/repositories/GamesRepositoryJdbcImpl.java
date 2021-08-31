package ru.inno.game.server.repositories;

import ru.inno.game.server.models.Game;
import ru.inno.game.server.models.Player;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class GamesRepositoryJdbcImpl implements GamesRepository {


    //language=SQL
    private static final String SQL_INSERT = "insert into game(datetime, player_first, player_second) values (?, ?, ?)";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from game where id = ?";

    //language=SQL
    private static final String SQL_UPDATE_GAME_BY_ID = "update game set player_first_shots_count = ?, " +
            "player_second_shots_count=?, second_game_time_amount = ? where id = ?";

    private DataSource dataSource;

    public GamesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Game game) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, game.getDateTime().toString());
            statement.setLong(2, game.getPlayerFirst().getId());
            statement.setLong(3, game.getPlayerSecond().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert game");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                game.setId(generatedKeys.getLong("id"));
            } else {
                throw new SQLException("Can't obtain id");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Game getById(Long gameId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, gameId);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()) {
                    return Game.builder()
                            .id(result.getLong("id"))
                            .dateTime(LocalDateTime.parse(result.getString("datetime")))
                            .playerFirst(Player.builder().id(result.getLong("player_first")).build())
                            .playerSecond(Player.builder().id(result.getLong("player_second")).build())
                            .playerFirstShotsCount(result.getInt("player_first_shots_count"))
                            .playerSecondShotsCount(result.getInt("player_second_shots_count"))
                            .secondsGameTimeAmount(result.getLong("second_game_time_amount"))
                            .build();

                } else {
                    throw new  SQLException("Empty result");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(Game game) {
        // TODO: реализовать обновление информации об игре
        // DONE!

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_GAME_BY_ID)) {
            statement.setInt(1,game.getPlayerFirstShotsCount());
            statement.setInt(2,game.getPlayerSecondShotsCount());
            statement.setLong(3, game.getSecondsGameTimeAmount());
            statement.setLong(4, game.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't update");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        //System.out.println("Game is updated " + game.getId());
    }
}
