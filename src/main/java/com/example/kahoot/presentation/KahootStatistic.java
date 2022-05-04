package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.KahootGame;
import com.example.kahoot.domain.model.statistic.ChartGraphic;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class KahootStatistic extends BaseController<KahootGame> {

    public Label question;
    public BarChart chart;
    public Label answ1;
    public Label answ2;
    public Label answ3;
    public Label answ4;
    public ListView list1;
    public ListView list2;
    public ListView list3;
    public ListView list4;
    public Button nextQuestion;
    public BarChart timeChart;

    private KahootGame game;
    private List<Label> answers;
    private final Background background = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));

    @Override
    public void getData(KahootGame data) {
        super.getData(data);
        game = data;
        Platform.runLater(() -> {
            answers = Arrays.asList(answ1, answ2, answ3, answ4);
            question.setText(game.getQuestion().getQuestion());
            for (int i = 0; i < 4; i++) {
                answers.get(i).setText(game.getQuestion().getAnswers().get(i));
                if (game.getQuestion().getAnswers().get(i).equals(game.getQuestion().getCorrect())) {
                    answers.get(i).setBackground(background);
                }
            }
            chart.setBarGap(1);
            ChartGraphic.Add chartGraphic = new ChartGraphic.Base();
            game.forEachUser((user) -> {
                chartGraphic.addGroup(user.getCurrentNick());
                chartGraphic.addObj("", game.statistic().correctAnswers(user));
                return null;
            });
            chart.getData().addAll(chartGraphic.toSeries());
            timeChart.setBarGap(3);
            timeChart.setCategoryGap(20);
            timeChart.getData().addAll(game.statistic().userTime(game.getQuestions()).toSeries());
        });
    }

    public void onNextQuestion(ActionEvent actionEvent) {
        game.nextQuestion();
        nextScene(Scenes.QUESTION_PREPARE, game);
    }
}
