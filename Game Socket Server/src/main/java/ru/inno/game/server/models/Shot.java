package ru.inno.game.server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Shot {
    private Long id;
    private LocalDateTime dateTime;
    private Game game;
    private Player shooter;
    private Player target;
}
