package com.example.kahoot;

import com.example.kahoot.bot.Bot;
import com.example.kahoot.data.DatabaseBase;
import com.example.kahoot.domain.FxInteractor;
import com.example.kahoot.domain.presesnter.MainPresenter;
import com.example.kahoot.presentation.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        BaseController<Object> controller = fxmlLoader.getController();
        boolean admin = new File("admin").exists();
        controller.init(new FxInteractor.Base(db, presenter,admin), stage);
    }

    private static Bot.Base bot;
    private static BotSession session;
    private static MainPresenter.Full presenter;
    private static DatabaseBase db;

    public static void main(String[] args) {
        db = new DatabaseBase("jdbc:sqlite:kahoot.db");
        presenter = new MainPresenter.Base(db);
        startBot();
        new Thread(Main::reconnected).start();
        launch();
    }

    private static void startBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            bot = new Bot.Base(new TokenReader().read("TokenEasy.txt"), new DefaultBotOptions());
            if (session != null && session.isRunning()) session.stop();
            session = api.registerBot(bot);
            presenter.setBot(bot);
            System.out.println("Bot: started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            bot = null;
        }
    }


    private static void reconnected() {
        while (true) {
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bot == null) {
                startBot();
                System.out.println("Bot: trying to reconnect");
            } else {
                try {
                    bot.getMe();
                } catch (NullPointerException | TelegramApiException ignored) {
                    bot = null;
                    presenter.lostConnection();
                }
            }
        }
    }
}