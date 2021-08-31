package ru.inno.game.server.repositories;


import ru.inno.game.server.models.Player;

import java.util.Optional;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public interface PlayersRepository {
    Optional<Player> findByNickname(String nickname);

    void save(Player player);
    // обновляет данные объекта по ВСЕМ ПОЛЯМ
    // player - {5, a, b, c}, player в БД - {5, x, y, c}
    // update(player) -> player в БД {5, a, b, c}
    void update(Player player);


}
