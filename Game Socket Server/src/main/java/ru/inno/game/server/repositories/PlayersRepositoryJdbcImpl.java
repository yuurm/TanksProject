package ru.inno.game.server.repositories;

import ru.inno.game.server.models.Player;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class PlayersRepositoryJdbcImpl implements PlayersRepository {

    //language=SQL
    private static String SQL_FIND_BY_NICKNAME = "select * from player where name = ?";

    //language=SQL
    private static String SQL_INSERT = "insert into player(name, ip) values (?, ?)";

    //prepareStatement для update
    //language=SQL
    private static final String SQL_UPDATE_PLAYER_BY_ID = "update player set " +
            " ip = ?, name = ?, points = ?, max_loses_count = ?, max_wins_count = ? where id = ?";




    private DataSource dataSource;

    public PlayersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Player> findByNickname(String nickname) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NICKNAME)) {
            statement.setString(1, nickname);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(Player.builder()
                            .id(result.getLong("id"))
                            .ip(result.getString("ip"))
                            .nickname(result.getString("name"))
                            .points(result.getInt("points"))
                            .losesCount(result.getInt("max_loses_count"))
                            .winsCount(result.getInt("max_wins_count"))
                            .build());
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Player player) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, player.getNickname());
            statement.setString(2, player.getIp());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert player");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                player.setId(generatedKeys.getLong("id"));
            } else {
                throw new SQLException("Can't obtain id");
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(Player player) {
        // TODO: доделать обновление IP-адреса, если такой игрок уже был (+ обновлять очки = количеству попаданий всего)
        //DONE!
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PLAYER_BY_ID)) {
            statement.setString(1, player.getIp());
            statement.setString(2, player.getNickname());
            statement.setInt(3, player.getPoints());
            statement.setInt(4, player.getLosesCount());
            statement.setInt(5, player.getWinsCount());
            statement.setLong(6, player.getId());

            // statement.executeUpdate();

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new IllegalStateException("Can't update player");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("ПРОИСХОДИТ ОБНОВЛЕНИЕ IP " + player.getNickname() + " " + player.getIp());
    }




}
