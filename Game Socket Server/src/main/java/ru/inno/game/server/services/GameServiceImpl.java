package ru.inno.game.server.services;

import ru.inno.game.server.models.Game;
import ru.inno.game.server.models.Player;
import ru.inno.game.server.models.Shot;
import ru.inno.game.server.repositories.GamesRepository;
import ru.inno.game.server.repositories.PlayersRepository;
import ru.inno.game.server.repositories.ShotsRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class GameServiceImpl implements GameService {

    private PlayersRepository playersRepository;

    private GamesRepository gamesRepository;

    private ShotsRepository shotsRepository;

    private LocalDateTime startGameTime = now();

    private LocalDateTime start = startGameTime;

    public GameServiceImpl(PlayersRepository playersRepository, GamesRepository gamesRepository, ShotsRepository shotsRepository) {
        this.playersRepository = playersRepository;
        this.gamesRepository = gamesRepository;
        this.shotsRepository = shotsRepository;
    }

    @Override
    public Long startGame(String firstIp, String secondIp, String firstPlayerNickname, String secondPlayerNickname) {
        // получили первого игрока
        Player first = checkIxExists(firstIp, firstPlayerNickname);
        // получили второго игрока
        Player second = checkIxExists(secondIp, secondPlayerNickname);
        // создаем игру
        Game game = Game.builder()
                .playerFirst(first)
                .playerSecond(second)
                .secondsGameTimeAmount(0L)
                .playerFirstShotsCount(0)
                .playerSecondShotsCount(0)
                .dateTime(LocalDateTime.now())
                .build();
        gamesRepository.save(game);
        return game.getId();
    }

    @Override
    public void stopGame(Long gameId) {
        // получили игру с указанным Id
        Game game = gamesRepository.getById(gameId);

        // выясняем время окончания игры
        LocalDateTime end = now();

        /*Player firstPlayer = game.getPlayerFirst();
        System.out.println(firstPlayer.getNickname());
        Player secondPlayer = game.getPlayerSecond();
        System.out.println(secondPlayer.getNickname());
        **/

        // выясняем победителя
        if (game.getPlayerFirstShotsCount() > game.getPlayerSecondShotsCount()) {
            //System.out.println("проверка");
            game.getPlayerFirst().setWinsCount(game.getPlayerFirst().getWinsCount() + 1);
            game.getPlayerSecond().setLosesCount(game.getPlayerSecond().getLosesCount() + 1);
            //System.out.println("победитель - " + game.getPlayerFirst().getNickname());
        }

        if (game.getPlayerFirstShotsCount() < game.getPlayerSecondShotsCount()) {
            //System.out.println("проверка на победитель - второй игрок?");
            game.getPlayerFirst().setLosesCount(game.getPlayerFirst().getLosesCount() + 1);
            game.getPlayerSecond().setWinsCount(game.getPlayerSecond().getWinsCount() + 1);
            //System.out.println("победитель - " + game.getPlayerSecond().getNickname());
        }





        // Длительность игры
        Long gameDuration = Duration.between(start, end).getSeconds();
        game.setSecondsGameTimeAmount(Math.abs(gameDuration));


        playersRepository.update(game.getPlayerFirst());
        playersRepository.update(game.getPlayerSecond());

        // обновляем данные по игре
        gamesRepository.update(game);


    }

    @Override
    public void shot(Long gameId, String shooterNickname, String targetNickname) {
        // получаем информацию об игроках
        Player shooter = playersRepository.findByNickname(shooterNickname).get();
        Player target = playersRepository.findByNickname(targetNickname).get();

        // TODO: сделать проверку на isPresent
        // DONE! Проверяется ниже при проверке игрока на существование


        // находим игру
        Game game = gamesRepository.getById(gameId);
        // создаем выстрел
        Shot shot = Shot.builder()
                .shooter(shooter)
                .target(target)
                .dateTime(now())
                .game(game)
                .build();
        // увеличили очки у того, кто стрелял
        shooter.setPoints(shooter.getPoints() + 1);
        // если стрелял первый игрок
        // TODO: обновление информации по игре в сервисе
        // DONE!
        if (game.getPlayerFirst().getId().equals(shooter.getId())) {
            // увеличиваем количество попаданий в этой игре
            game.setPlayerFirstShotsCount(game.getPlayerFirstShotsCount() + 1);
        }
        // если стрелял второй игрок
        if (game.getPlayerSecond().getId().equals(shooter.getId())) {
           game.setPlayerSecondShotsCount(game.getPlayerSecondShotsCount() + 1);
        }




//        if (game.getPlayerFirst().getId().equals(shooter.getId())) {
//            // увеличиваем количество попаданий в этой игре
//            game.setPlayerFirstShotsCount(game.getPlayerFirstShotsCount() + 1);
//        }
//        // если стрелял второй игрок
//        if (game.getPlayerSecond().getId().equals(shooter.getId())) {
//            game.setPlayerSecondShotsCount(game.getPlayerSecondShotsCount() + 1);
//        }
        // обновляем данные по стреляющему
        playersRepository.update(shooter);
        // обновляем данные по игре
        gamesRepository.update(game);
        // сохраняем выстрел
        shotsRepository.save(shot);
    }



    // ищет игрока по никнейму, если такой игрок был - она меняет его IP
    // если игрока не было, она создает нового и сохраняет его
    private Player checkIxExists(String ip, String nickname) {
        Player result;

        Optional<Player> playerOptional = playersRepository.findByNickname(nickname);
        // если игрока под таким именем нет
        if (!playerOptional.isPresent()) {
            // создаем игрока
             Player player = Player.builder()
                    .ip(ip)
                    .nickname(nickname)
                    .losesCount(0)
                    .winsCount(0)
                    .points(0)
                    .build();
            // сохраняем его в репозитории
            playersRepository.save(player);
            result = player;
        } else {
            Player player = playerOptional.get();
            player.setIp(ip);
            playersRepository.update(player);
            result = player;
        }

        return result;
    }
}
