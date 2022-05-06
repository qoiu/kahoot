package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.KahootGame;
import com.example.kahoot.presentation.model.ChartGraphic;
import com.example.kahoot.presentation.model.SingleChartGraphic;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import kotlin.Unit;

public class KahootStatistic extends BaseController<KahootGame> {

    public Label question;
    public BarChart<String,Integer> chart;
    public Button nextQuestion;
    public BarChart<String,Double> timeChart;
    public Label statistic_label;
    public BarChart score_chart;
    public CategoryAxis chartAxis;
    public NumberAxis chartYAxis;
    public NumberAxis timeChartYAxis;

    private KahootGame game;

    @Override
    public void getData(KahootGame data) {
        super.getData(data);
        game = data;
        Platform.runLater(()-> question.setText(game.getQuestion().getQuestion()));
        setCorrectAnswerChart();
        setTimeChart();
        SingleChartGraphic.Add scoreChart = new SingleChartGraphic.Base();
        game.forEachUser((user)->{
            scoreChart.addObj(user.getCurrentNick(),game.statistic().editUserStatistic(user).score());
            return null;
        });
        Platform.runLater(() -> {
//            timeChart.setCategoryGap(20);
            score_chart.getData().addAll(scoreChart);
//            thread().start();
        });
    }

    private void setCorrectAnswerChart(){
        ChartGraphic.Add<String, Integer> chartGraphic = new ChartGraphic.Base<>();
        for (int i = 0; i < game.getQuestion().answersCount(); i++) {
            String answer = game.getQuestion().getAnswers().get(i);
            if(game.getQuestion().getCorrect().equals(answer))
                answer+=" ✔";
            chartGraphic.addGroup(answer);
            chartGraphic.addObj(game.getQuestion().getQuestion(),game.statistic().getCorrectAnswers(i));
        }
        Platform.runLater(() -> {
            chartYAxis.setUpperBound(chartGraphic.getMaxValue());
            chart.getData().clear();
            chart.layout();
            chart.getData().addAll(chartGraphic.toSeries());
        });
    }

    private void setTimeChart(){
        ChartGraphic.Add<String,Double> timeGraphic = new ChartGraphic.Base<>();
        game.forEachUser((user)->{
            timeGraphic.addGroup(user.getCurrentNick());
            game.forEachAnsweredQuestion((id,question)-> {
                timeGraphic.addObj(question.getQuestion(), game.statistic(user).getTime(id) / 10.0);
                return Unit.INSTANCE;
            });
            timeGraphic.addObj("Total",game.statistic(user).getTotalTime() / 10.0);
            return Unit.INSTANCE;
        });
        Platform.runLater(()-> timeChart.getData().addAll(timeGraphic.toSeries()));
    }

    Thread thread() {
        return new Thread(() -> {
            int i =1;
            while (true) {
                chart.getData().remove(1);
                XYChart.Series series = new XYChart.Series();
                series.setName("new");
                series.getData().addAll(new XYChart.Data("2", i));
                Platform.runLater(()->{
                    chart.getData().add(1, series);
                });
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onNextQuestion(ActionEvent actionEvent) {
        game.nextQuestion();
        nextScene(Scenes.QUESTION_PREPARE, game);
    }
}
