package ru.inno.game.client.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.inno.game.client.controllers.MainController;
import ru.inno.game.client.sockets.SocketClient;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class GameUtil {

    private final static int DAMAGE = 5;
    private final static int PLAYER_STEP = 5;

    private AnchorPane pane;
    private Label hpPlayer;
    private Label hpEnemy;
    private SocketClient socketClient;

    public GameUtil(MainController controller) {
        this.pane = controller.getPane();
        this.hpPlayer = controller.getHpPlayer();
        this.hpEnemy = controller.getHpEnemy();
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    // shooterIsEnemy - true - враг стреляет в нас
    public void createBullet(Circle shooter, Circle target, boolean shooterIsEnemy) {
        Circle bullet = new Circle();
        bullet.setRadius(5);
        // ставим пулю, там же, где и находится игрок в данный момент
        bullet.setCenterX(shooter.getCenterX() + shooter.getLayoutX());
        bullet.setCenterY(shooter.getCenterY() + shooter.getLayoutY());
        bullet.setFill(Color.ORANGE);
        // добавляю ее в сцену как вложенный объект относительно панели
        pane.getChildren().add(bullet);

        int value;
        Label hpLabel;

        if (shooterIsEnemy) {
            hpLabel = hpPlayer;
            value = 1;
        } else {
            hpLabel = hpEnemy;
            value = -1;
        }

        // сделали анимацию выстрела
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.005), animation -> {
            bullet.setCenterY(bullet.getCenterY() + value);

            if (bullet.getCenterY() < 0 && bullet.getCenterY() > 500) {
                pane.getChildren().remove(bullet);
                System.out.println("Удалили объект");
            }

            if (bullet.isVisible() && isIntersects(bullet, target)) {
                bullet.setVisible(false);
                createDamage(hpLabel);

                if (!shooterIsEnemy) {
                    socketClient.sendMessage("damage");
                }

                if (Integer.parseInt(getHpPlayer().getText()) == 0 ||
                        Integer.parseInt(getHpEnemy().getText()) == 0) {
                    socketClient.sendMessage("STOP");
                }

            }
        }));

        timeline.setCycleCount(450);
        timeline.play();
    }

    private boolean isIntersects(Circle bullet, Circle target) {
        return bullet.getBoundsInParent().intersects(target.getBoundsInParent());
    }

    private void createDamage(Label hpLabel) {
        int hpPerson = Integer.parseInt(hpLabel.getText());
        if (hpPerson > 0) {
            hpLabel.setText(String.valueOf(hpPerson - DAMAGE));
        }
    }

    public void goLeft(Circle person) {
        if(person.getCenterX() >= -219) {
            person.setCenterX(person.getCenterX() - PLAYER_STEP);
        }
    }

    public void goRight(Circle person) {
        if(person.getCenterX() <= 219) {
            person.setCenterX(person.getCenterX() + PLAYER_STEP);
        }
    }

    public Label getHpPlayer() {
        return hpPlayer;
    }

    public void setHpPlayer(Label hpPlayer) {
        this.hpPlayer = hpPlayer;
    }

    public Label getHpEnemy() {
        return hpEnemy;
    }

    public void setHpEnemy(Label hpEnemy) {
        this.hpEnemy = hpEnemy;
    }
}
