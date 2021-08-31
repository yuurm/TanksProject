package ru.inno.game.server.services;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public interface GameService {
    /**
     * Метод вызывается для обеспечения начала игры. Если игрок с таким никнеймом уже есть, то мы работаем с ним.
     * Если такого нет - то создаем нового
     * @param firstIp - IP первого первого игрока
     * @param secondIp - IP второго игрока
     * @param firstPlayerNickname никнейм первого игрока
     * @param secondPlayerNickname никнейм второго игрока
     * @return возвращает идентификатор игры
     */
    Long startGame(String firstIp, String secondIp, String firstPlayerNickname, String secondPlayerNickname);

    /**
     * Фиксирует попадание игрока в противника
     * @param gameId идентификатор игры
     * @param shooterNickname кто стрелял
     * @param targetNickname в кого попали
     */
    void shot(Long gameId, String shooterNickname, String targetNickname);


    void stopGame(Long gameId);


}
