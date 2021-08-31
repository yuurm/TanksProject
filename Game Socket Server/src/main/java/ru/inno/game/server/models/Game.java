package ru.inno.game.server.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Game {
    private Long id;
    private LocalDateTime dateTime;
    private Player playerFirst;
    private Player playerSecond;
    private Integer playerFirstShotsCount;
    private Integer playerSecondShotsCount;
    private Long secondsGameTimeAmount;



}
