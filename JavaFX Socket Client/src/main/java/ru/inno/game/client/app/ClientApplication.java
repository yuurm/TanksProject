package ru.inno.game.client.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.inno.game.client.controllers.MainController;

/**
 *
 * @author Yuri Urmatsky
 * @version v1.0
 */
public class ClientApplication extends Application {

    public static void main(String[] args) {
        // входная точка приложения
        // в которой мы вызываем стандартный метод для запуска JavaFx-приложения
        // launch(args)
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        // старт приложения
        String fxmlFileName = "/fxml/Main.fxml";
        // загрузчик FXML-файлов
        FXMLLoader fxmlLoader = new FXMLLoader();
        // получили доступ к fxml-файлу через папку resources
        Parent root = fxmlLoader.load(getClass().getResourceAsStream(fxmlFileName));

        primaryStage.setTitle("Game Client");
        // создали сцену и к ней прикрепили fxml-файл
        Scene scene = new Scene(root);
        // привязать сцену к stage
        primaryStage.setScene(scene);
        // чтобы нельзя было менять размер сцены
        primaryStage.setResizable(false);

        // получить доступ к перехватчику нажатия на клавиатуру
        MainController controller = fxmlLoader.getController();

        // говорим сцене, какой перехватчик использовать
        scene.setOnKeyPressed(controller.getPlayerControlEventHandler());

        // показываем нашу панель
        primaryStage.show();
    }
}
