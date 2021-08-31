package ru.inno.game.server.repositories;

import ru.inno.game.server.models.Shot;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public interface ShotsRepository {
    void save(Shot shot);
}
