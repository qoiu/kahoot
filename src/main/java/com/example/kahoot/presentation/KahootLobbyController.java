package com.example.kahoot.presentation;

import com.example.kahoot.domain.MsgReceiver;
import com.example.kahoot.domain.model.Kahoot;
import com.example.kahoot.domain.model.KahootGame;
import com.example.kahoot.domain.model.User;
import com.example.kahoot.domain.model.UserState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KahootLobbyController extends BaseController<Kahoot> implements MsgReceiver {

    public Label pinLabel;
    public ListView list;
    private Kahoot kahoot;
    private final Set<User> users = new HashSet<>();

    @Override
    public void getData(Kahoot data) {
        kahoot = data;
    }

    private int pin;

    @Override
    public void init() {
        super.init();
        pin = new Random().nextInt(8999) + 1000;
        pinLabel.setText("PIN: " + pin);
        interactor.subscribe(this);
        stage.widthProperty().addListener((observableValue, number, t1) ->
                pinLabel.setFont(new Font("System", stage.getWidth() / 16)));
    }

    @Override
    public boolean usualClose() {
        return false;
    }

    @Override
    public void onClose() {
        nextScene(Scenes.LIST);
    }

    @Override
    public void receiveMsg(@NotNull User user, @NotNull String message) {
        if (message.equals(pin + "")) {
            users.add(user);
            user.setCurrentState(new UserState.Ready());
            user.getCurrentState().execute();
        }
        updateList();
    }

    private void updateList() {
        Platform.runLater(() -> list.setItems(FXCollections.observableList(Arrays.asList(users.stream().map(User::getCurrentNick).toArray()))));
    }

    public void onStartKahoot(ActionEvent actionEvent) {
        nextScene(Scenes.QUESTION_PREPARE, new KahootGame(kahoot, users.stream().toList()));
    }
}
