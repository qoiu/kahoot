package com.example.kahoot;

import com.example.kahoot.bot.BaseBotPresenter;
import com.example.kahoot.bot.Bot;
import com.example.kahoot.data.DatabaseBase;
import com.example.kahoot.data.repository.ChatRepository;
import com.example.kahoot.data.repository.KahootRepository;
import com.example.kahoot.data.repository.UserRepository;
import com.example.kahoot.domain.clean.*;
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
        controller.init(fxInteractor, stage);
    }

    private static Bot.Base bot;
    private static BotSession session;
    private static DatabaseBase db;
    private static Thread reconnection;
    private static FxInteractor fxInteractor;


    public static void main(String[] args) {
        db = new DatabaseBase("jdbc:sqlite:kahoot.db");
        initialize();
        reconnection = new Thread(Main::reconnected);
        reconnection.start();
        launch();
    }

    private static void initialize() {
        ChatRepository chatRepository = new ChatRepository(db);
        UserRepository userRepository = new UserRepository(db);
        KahootRepository kahootRepository = new KahootRepository(db);
        AllRepository repository = new AllRepository.Base(kahootRepository, userRepository, chatRepository);
        Bot bot = startBot();
        BotPresenter botPresenter = new BaseBotPresenter(bot);
        BotInteractor botInteractor = new BotInteractor.Base(botPresenter);
        boolean admin = new File("admin").exists();
        fxInteractor = new FxInteractor.Base(admin);
        MainInteractor main = new MainInteractor.Base(fxInteractor, botInteractor, repository);
        botInteractor.setMainInteractor(main);
        fxInteractor.setMainInteractor(main);
        botPresenter.setInteractor(botInteractor);
        bot.setPresenter(botPresenter);
    }

    private static Bot.Base startBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            bot = new Bot.Base(new TokenReader().read("TokenEasy.txt"), new DefaultBotOptions());
            if (session != null && session.isRunning()) session.stop();
            session = api.registerBot(bot);
            System.out.println("Bot: started");
            return bot;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return startBot();
        }
    }


    private static void reconnected() {
        boolean continuum = true;
        while (continuum) {
            if (bot == null) {
                startBot();
                System.out.println("Bot: trying to reconnect");
            } else {
                try {
                    bot.getMe();
                } catch (NullPointerException | TelegramApiException ignored) {
                    bot = null;
                }
            }
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continuum = false;
            }
        }
    }

    @Override
    public void stop() throws Exception {
        reconnection.interrupt();
        super.stop();
    }
}