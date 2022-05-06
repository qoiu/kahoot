package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.Kahoot;
import com.example.kahoot.domain.model.KahootGame;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KahootListController extends BaseController<List<Kahoot>> {
    public ListView<String> list;
    private final List<Kahoot> kahoots = new ArrayList<>();
    public Button editBtn;
    public Button startBtn;
    public HBox cheatPane;

    public void onNewClick() {
        showInputTextDialog();
    }

    @Override
    public boolean usualClose() {
        return false;
    }

    @Override
    public void onClose() {
        nextScene(Scenes.MAIN);
    }

    private void showInputTextDialog() {
        showInputTextDialog("Please enter new kahoot title:");
    }

    private void showInputTextDialog(String title) {
        TextInputDialog dialog = new TextInputDialog("New kahoot");
        dialog.setTitle("Kahoot");
        dialog.setHeaderText(title);
        dialog.setContentText("Title:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Kahoot kahoot = new Kahoot(-1, name, new ArrayList<>(Collections.emptyList()));
            if (kahoots.stream().noneMatch(kahoot1 -> kahoot1.getTitle().equals(name)))
                nextScene(Scenes.CREATE, kahoot);
            else
                showInputTextDialog("Kahoot " + name + " already exist. Choose another name");
        });
    }

    private void updateList() {
        List<String> result = new ArrayList<>(Collections.emptyList());
        for (Kahoot kahoot : kahoots) {
            result.add(kahoot.getTitle());
        }
        list.setItems(FXCollections.observableArrayList(result));
    }

    @Override
    public void getData(List<Kahoot> data) {
        kahoots.addAll(data);
        updateList();
    }

    @Override
    public void init() {
        super.init();
        cheatPane.setVisible(interactor.isDebugVersion());
        kahoots.addAll(interactor.getAllKahoot());
        updateList();
        list.getSelectionModel().selectedItemProperty().addListener(getListener());
    }

    private ChangeListener<String> getListener() {
        return (observableValue, s, t1) -> {
            if (t1 == null) return;
            editBtn.setDisable(list.getSelectionModel().getSelectedIndex() > kahoots.size());
            if (list.getSelectionModel().getSelectedIndex() > kahoots.size())
                changeTitle(getKahoot().getTitle());
        };
    }

    public void onEditKahootAction() {
        nextScene(Scenes.CREATE, getKahoot());
    }

    public void onBackClick() {
        usualClose();
    }

    public void onStartKahoot() {
        nextScene(Scenes.LOBBY, getKahoot());
    }

    private Kahoot getKahoot() {
        return kahoots.get(list.getSelectionModel().getSelectedIndex());
    }

    public void onCheatClick() {
        nextScene(Scenes.QUESTION_PREPARE, new KahootGame(getKahoot(), interactor.cheatConnection()));
    }

    public void onCheatFinishClick() {
        nextScene(Scenes.FINISH, new KahootGame(getKahoot(), interactor.cheatConnection()));
    }
}