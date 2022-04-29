package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.Kahoot;
import javafx.event.ActionEvent;

public class HelloController extends BaseController<Kahoot> {

    public void onEditKahootClick(ActionEvent actionEvent) {
        nextScene(Scenes.LIST);
    }
}