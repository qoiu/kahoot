package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.KahootGame;

public class KahootGameController extends BaseController<KahootGame> {

    private KahootGame game;

    @Override
    public void getData(KahootGame data) {
        game = data;
    }
}