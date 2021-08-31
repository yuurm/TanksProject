package ru.inno.game.server.sockets;

import ru.inno.game.server.services.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class GameServer {
    // поле, которое позволяет реализовать здесь серверное сокет-соединение
    private ServerSocket serverSocket;
    // два игрока нашего сервера
    private ClientThread firstPlayer;
    private ClientThread secondPlayer;

    private static final Lock lock = new ReentrantLock();

    private String firstPlayerNickname;
    private String secondPlayerNickname;

    private GameService gameService;

    private boolean gameInProcess = true;
    private boolean gameInStarted = false;
    private Long gameId;


    public GameServer(GameService gameService) {
        this.gameService = gameService;
    }

    // запускаем наш серверный сокет на каком-то порту
    public void start(int port) {
        try {
            // создали объект серверного-сокета
            serverSocket = new ServerSocket(port);
            // он уводит текущий поток в ожидание, пока не подключится клиента
            // как только клиент подключится, он будет лежать в объектной переменной client
            System.out.println("SERVER: ожидаем подключения первого игрока");
            firstPlayer = connect();
            System.out.println("SERVER: первый игрок подключен");
            System.out.println("SERVER: ожидаем подключения второго игрока");
            secondPlayer = connect();
            System.out.println("SERVER: второй игрок подключен");
            // запустили побочный поток для работы со вторым клиентом
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private ClientThread connect() {
        try {
            Socket client = serverSocket.accept();
            ClientThread playerThread = new ClientThread(client);
            playerThread.start();
            return playerThread;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private class ClientThread extends Thread {

        private Socket client;
        // поток символов, которые мы отправляем клиенту
        private PrintWriter toClient;
        // поток символов, которые мы читаем от клиента
        private BufferedReader fromClient;

        public ClientThread(Socket client) {
            this.client = client;
            try {
                // обернули потоки байтов в потоки символов
                this.toClient = new PrintWriter(client.getOutputStream(), true);
                this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // мы в любое время можем получить сообщение от клиента
        // поэтому чтение сообщения от клиента должно происходить в побочном потоке
        @Override
        public void run() {
            while (gameInProcess) {
                String messageFromClient;
                try {
                    // прочитали сообщение от клиента
                    messageFromClient = fromClient.readLine();

                    if (messageFromClient != null) {
                        System.out.println("SERVER: получено сообщение <" + messageFromClient + ">");
                        if (isMessageForNickname(messageFromClient)) {
                            resolveNickname(messageFromClient);
                        } else if (isMessageForShot(messageFromClient)) {
                            resolveShot(messageFromClient);
                        } else if (isMessageForMove(messageFromClient)) {
                            resolveMove(messageFromClient);
                        } else if (isMessageForDamage(messageFromClient)) {
                            resolveDamage();
                        } else if (isMessageForStop(messageFromClient)) {
                            // TODO: реализовать поведение

                            // DONE! Если от клиента поступило сообщение STOP и игра запущена - флажок становится false
                            //lock.lock();

                            /*if (gameInProcess) {
                                gameInProcess = false;
                                gameService.stopGame(gameId);

                                //System.out.println("Game is stopped");
                            }**/
                            //lock.unlock();

                            lock.lock();
                            if(gameInProcess){
                                sendMessage(firstPlayer, "STOP");
                                sendMessage(secondPlayer, "STOP");
                                gameInProcess = false;
                                try {
                                    toClient.close();
                                    fromClient.close();
                                    serverSocket.close();
                                } catch (IOException e) {
                                    throw new IllegalStateException(e);
                                }
                                System.out.println("GAME IS OVER");
                                lock.unlock();

                            }

                        }
                    }

                        // игра должна начинаться только один раз и только в одном из потоков
                        // срабатывает, только если игра еще не началась
                        lock.lock();
                        if (isReadyForStart()) {
                            startGame();
                        }
                        lock.unlock();

                } catch (IOException e) {
                    throw new IllegalStateException();
                }
            }
        }

        private boolean isMessageForStop(String messageFromClient) {
            return messageFromClient.equals("STOP");
        }

        private void resolveDamage() {
            if (meFirst()) {
                gameService.shot(gameId, firstPlayerNickname, secondPlayerNickname);
            } else {
                gameService.shot(gameId, secondPlayerNickname, firstPlayerNickname);
            }
        }



        private boolean isMessageForDamage(String messageFromClient) {
            return messageFromClient.equals("damage");
        }

        private void resolveMove(String messageFromClient) {
            // Отправить его другому клиенту
            // если вы - первый клиент
            if (meFirst()) {
                // отправляем сообщение второму
                sendMessage(secondPlayer, messageFromClient);
            } else {
                sendMessage(firstPlayer, messageFromClient);
            }
        }

        private boolean meFirst() {
            return this == firstPlayer;
        }

        private boolean isMessageForMove(String messageFromClient) {
            return messageFromClient.equals("left") || messageFromClient.equals("right");
        }

        private boolean isMessageForShot(String messageFromClient) {
            return messageFromClient.equals("shot");
        }

        private void resolveShot(String messageFromClient) {
            // Отправить его другому клиенту
            // если вы - первый клиент
            if (meFirst()) {
                // отправляем сообщение второму
                sendMessage(secondPlayer, messageFromClient);
            } else {
                sendMessage(firstPlayer, messageFromClient);
            }
        }

        private void resolveNickname(String messageFromClient) {
            String playerNickname = messageFromClient.split(" ")[1]; // playerNickname = Марсель
            // если это поток первого игрока
            if (meFirst()) {
                // запоминаем его никнейм
                firstPlayerNickname = playerNickname;
            } else {
                secondPlayerNickname = playerNickname;
            }
            // nickname Юрий
        }

        private boolean isMessageForNickname(String messageFromClient) {
            return messageFromClient.startsWith("nickname ");
        }

        private void startGame() {
            gameId = gameService.startGame(firstPlayer.client.getInetAddress().getHostAddress() + firstPlayer.client.getPort(),
                    secondPlayer.client.getInetAddress().getHostAddress() + secondPlayer.client.getPort(),
                    firstPlayerNickname, secondPlayerNickname);
            gameInStarted = true;
        }

        private boolean isReadyForStart() {
            return firstPlayerNickname != null && secondPlayerNickname != null && !gameInStarted;
        }

        public void sendMessage(ClientThread player, String message) {
            player.toClient.println(message);
        }
    }
}
