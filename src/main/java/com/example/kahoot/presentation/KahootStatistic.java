package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.Game;
import com.example.kahoot.presentation.model.ChartGraphic;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KahootStatistic extends BaseController<Game.Full> {

    public Label question;
    public BarChart<String, Integer> chart;
    public Button nextQuestion;
    public BarChart<String, Double> timeChart;
    public Label statistic_label;
    public BarChart<String, Integer> score_chart;
    public CategoryAxis chartAxis;
    public NumberAxis chartYAxis;
    public NumberAxis timeChartYAxis;

    private Game.Full game;

    @Override
    public void getData(Game.Full data) {
        game = data;
        Platform.runLater(() -> question.setText(game.getQuestion().getQuestion()));
        if (game.isLastQuestion())
            Platform.runLater(() -> nextQuestion.setText("Finish"));
        setCorrectAnswerChart();
        setTimeChart();
        setScoreChart();
    }

    private void setCorrectAnswerChart() {
        ChartGraphic.Add<String, Integer> chartGraphic = new ChartGraphic.Base<>();
        for (int i = 0; i < game.getQuestion().answersCount(); i++) {
            String answer = game.getQuestion().getAnswers().get(i);
            if (game.getQuestion().getCorrect().equals(answer))
                answer += " ✔";
            chartGraphic.addGroup(answer);
            chartGraphic.addObj(game.getQuestion().getQuestion(), game.statistic().getCorrectAnswers(i));
        }
        Platform.runLater(() -> {
            chartYAxis.setUpperBound(chartGraphic.getMaxValue());
            chart.getData().clear();
            chart.layout();
            chart.getData().addAll(chartGraphic.toSeries());
        });
    }

    private void setTimeChart() {
        ChartGraphic.Add<String, Double> timeGraphic = new ChartGraphic.Base<>();
        game.forEachUser(user -> {
            timeGraphic.addGroup(user.getCurrentNick());
            game.forEachAnsweredQuestion((id, question) ->
                    timeGraphic.addObj(question.getQuestion(), game.statistic(user).getTime(id) / 10.0));
            timeGraphic.addObj("Total", game.statistic(user).getTotalTime() / 10.0);
        });
        Platform.runLater(() -> timeChart.getData().addAll(timeGraphic.toSeries()));
    }

    private void setScoreChart() {
        ChartGraphic.Add<String, Integer> scoreChart = new ChartGraphic.Base<>();
        game.forEachUser(user -> {
            scoreChart.addGroup(user.getCurrentNick());
            int score = game.statistic(user).score();
            scoreChart.addObj(user.getCurrentNick(), score);
        });
        Platform.runLater(() -> score_chart.getData().addAll(scoreChart.toSeries()));
    }

    public void onNextQuestion() {
        if (game.isLastQuestion()) {
            nextScene(Scenes.FINISH, game);
        } else {
            game.nextQuestion();
            nextScene(Scenes.QUESTION_PREPARE, game);
        }
    }
}
