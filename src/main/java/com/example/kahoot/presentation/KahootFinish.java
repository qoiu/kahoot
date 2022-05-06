package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.Game;
import com.example.kahoot.domain.model.User;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.List;

public class KahootFinish extends BaseController<Game.Full> {

    public Label question;
    public BarChart<String, Integer> barChart;
    private Game.Full game;

    @Override
    public void getData(Game.Full data) {
        game = data;
        thread().start();
    }

    Thread thread() {

        return new Thread(() -> {
            List<User> list = game.statistic().sortedScoreboardUser();
            int i = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (i < list.size()) {
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                User user = list.get(i);
                series.setName(user.getCurrentNick() + " :" + game.statistic().readUserStatistic(user).score());
                series.getData().add(new XYChart.Data<>("Total", game.statistic().readUserStatistic(user).score()));
                Platform.runLater(() -> barChart.getData().add(series));
                i++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Platform.runLater(() -> question.setText("Winner: " + list.get(list.size() - 1).getCurrentNick()));
        });
    }

    @Override
    public boolean usualClose() {
        return false;
    }

    @Override
    public void onClose() {
        nextScene(Scenes.MAIN);
    }
}
