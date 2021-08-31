package ru.inno.game.client.sockets;

import javafx.application.Platform;
import javafx.scene.shape.Circle;
import ru.inno.game.client.controllers.MainController;
import ru.inno.game.client.utils.GameUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 21.08.2021
 * 42. JavaFX Socket Client
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class SocketClient extends Thread {
    // канал для подключения
    private Socket socket;

    private BufferedReader fromServer;
    private PrintWriter toServer;

    private GameUtil gameUtil;
    private Circle player;
    private Circle enemy;

    public SocketClient(MainController controller, String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.toServer = new PrintWriter(socket.getOutputStream(), true);
            this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.player = controller.getPlayer();
            this.enemy = controller.getEnemy();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public void sendMessage(String message) {
        toServer.println(message);
    }

    @Override
    public void run() {
        while (true) {
            String messageFromServer;
            try {
                messageFromServer = fromServer.readLine();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

            if (messageFromServer != null) {
                switch (messageFromServer) {
                    case "left":
                        gameUtil.goLeft(enemy);
                        break;
                    case "right":
                        gameUtil.goRight(enemy);
                        break;
                    case "shot":
                        Platform.runLater(() -> gameUtil.createBullet(enemy, player, true));
                        break;
                }
            }
        }
    }

    public void setGameUtil(GameUtil gameUtil) {
        this.gameUtil = gameUtil;
    }
}
