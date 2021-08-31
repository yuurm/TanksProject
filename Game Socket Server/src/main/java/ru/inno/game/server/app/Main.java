package ru.inno.game.server.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.inno.game.server.repositories.*;
import ru.inno.game.server.services.GameService;
import ru.inno.game.server.services.GameServiceImpl;
import ru.inno.game.server.sockets.GameServer;

import javax.sql.DataSource;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class Main {
    public static void main(String[] args) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/game_db");
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("admin");
        hikariConfig.setMaximumPoolSize(10);

        DataSource dataSource = new HikariDataSource(hikariConfig);

        GamesRepository gamesRepository = new GamesRepositoryJdbcImpl(dataSource);
        PlayersRepository playersRepository = new PlayersRepositoryJdbcImpl(dataSource);
        ShotsRepository shotsRepository = new ShotsRepositoryJdbcImpl(dataSource);

        GameService gameService = new GameServiceImpl(playersRepository, gamesRepository, shotsRepository);
        GameServer gameServer = new GameServer(gameService);
        gameServer.start(7777);

    }
}
