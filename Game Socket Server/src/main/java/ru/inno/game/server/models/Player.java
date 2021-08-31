package ru.inno.game.server.models;

import lombok.*;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Getter
//@Setter
public class Player {
    private Long id;
    private String ip;
    private String nickname;
    private Integer points;
    private Integer winsCount;
    private Integer losesCount;
}
