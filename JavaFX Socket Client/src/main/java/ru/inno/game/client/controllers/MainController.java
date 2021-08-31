package ru.inno.game.client.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import ru.inno.game.client.sockets.SocketClient;
import ru.inno.game.client.utils.GameUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
// контроллер осуществляет взаимодействие со сценой
public class MainController implements Initializable {

    private GameUtil gameUtil;


    @FXML
    private Button hiButton;


    @FXML
    private Label messageLabel;


    @FXML
    private TextField nameTextField;


    @FXML
    private Circle player;



    @FXML
    private Circle enemy;


    @FXML
    private Label hpPlayer;


    @FXML
    private Label hpEnemy;


    @FXML
    private AnchorPane pane;

    private SocketClient client;

    // не давать игроку выходить на пределы площадки
    private final EventHandler<KeyEvent> playerControlEventHandler = event -> {
      if (event.getCode() == KeyCode.RIGHT) {
          gameUtil.goRight(player);
          client.sendMessage("right");
      } else if (event.getCode() == KeyCode.LEFT) {
          gameUtil.goLeft(player);
          client.sendMessage("left");
      } else if (event.getCode() == KeyCode.SPACE) {
          gameUtil.createBullet(player, enemy, false);
          client.sendMessage("shot");
      }
    };

    public EventHandler<KeyEvent> getPlayerControlEventHandler() {
        return playerControlEventHandler;
    }

    // инициализирует контроллер
    public void initialize(URL location, ResourceBundle resources) {
        // что должно происходить, когда мы нажимаем на кнопку?
        hiButton.setOnAction(event -> {
            // получаем текст, который ввел пользователь
            String name = nameTextField.getText();
            // формируем текст сообщения
            String message = "Привет, " + name + "!";
            // кладем текст в label
            messageLabel.setText(message);
            // сместить фокус с поля для ввода на всю сцену
            hiButton.getScene().getRoot().requestFocus();
            this.client = new SocketClient(this, "localhost", 7777);
            this.gameUtil = new GameUtil(this);
            this.client.setGameUtil(gameUtil);
            this.gameUtil.setSocketClient(this.client);
            this.client.sendMessage("nickname " + name  );
            this.client.start();
        });

    }

    public Label getHpPlayer() {
        return hpPlayer;
    }

    public Label getHpEnemy() {
        return hpEnemy;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public SocketClient getClient() {
        return client;
    }

    public GameUtil getGameUtil() {
        return gameUtil;
    }

    public Circle getPlayer() {
        return player;
    }

    public Circle getEnemy() {
        return enemy;
    }
}
