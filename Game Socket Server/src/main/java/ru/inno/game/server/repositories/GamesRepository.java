package ru.inno.game.server.repositories;

import ru.inno.game.server.models.Game;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public interface GamesRepository {
    void save(Game game);

    Game getById(Long gameId);

    void update(Game game);
}
