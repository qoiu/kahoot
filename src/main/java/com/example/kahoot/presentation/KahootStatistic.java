package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.KahootGame;
import com.example.kahoot.domain.model.statistic.SingleChartGraphic;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.List;

public class KahootStatistic extends BaseController<KahootGame> {

    public Label question;
    public BarChart chart;
    public Button nextQuestion;
    public BarChart timeChart;
    public Label statistic_label;
    public BarChart score_chart;

    private KahootGame game;
    private List<Label> answers;
    private final Background background = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));

    @Override
    public void getData(KahootGame data) {
        super.getData(data);
        game = data;
        SingleChartGraphic.Add chartGraphic = new SingleChartGraphic.Base();
        for (int i = 0; i < game.getQuestion().getAnswers().size() ; i++) {
            String answer = game.getQuestion().getAnswers().get(i);
            if(game.getQuestion().getCorrect().equals(answer))
                answer+=" ✔";
            chartGraphic.addObj(answer+"\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15",game.statistic().getCorrectAnswers(i)+1);
        }
        SingleChartGraphic.Add scoreChart = new SingleChartGraphic.Base();
        game.forEachUser((user)->{
            scoreChart.addObj(user.getCurrentNick(),game.statistic().userStatistic(user).score());
            return null;
        });
        Platform.runLater(() -> {
//            answers = Arrays.asList(answ1, answ2, answ3, answ4);
            question.setText(game.getQuestion().getQuestion());
//            for (int i = 0; i < 4; i++) {
////                answers.get(i).setText(game.getQuestion().getAnswers().get(i));
//                if (game.getQuestion().getAnswers().get(i).equals(game.getQuestion().getCorrect())) {
//                    answers.get(i).setBackground(background);
//                }
//            }
//            game.forEachUser((user) -> {
//                chartGraphic.addObj("", game.statistic().correctAnswers(user));
//                return null;
//            });
            chart.setBarGap(1);
            chart.setCategoryGap(20);
            chart.getData().addAll(chartGraphic.toSeries());
            chart.impl_updatePeer();
            timeChart.setBarGap(3);
            timeChart.setCategoryGap(20);
            timeChart.getData().addAll(game.statistic().userTimeChart(game.getQuestions()).toSeries());
            score_chart.getData().addAll(scoreChart);
        });
    }

    public void onNextQuestion(ActionEvent actionEvent) {
        game.nextQuestion();
        nextScene(Scenes.QUESTION_PREPARE, game);
    }
}
