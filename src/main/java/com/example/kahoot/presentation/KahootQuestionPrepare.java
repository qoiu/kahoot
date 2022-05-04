package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.KahootGame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import kotlin.Unit;

public class KahootQuestionPrepare extends BaseController<KahootGame> {

    public Label question;
    public Label timeLabel;
    public Label answ1;
    public Label answ2;
    public Label answ3;
    public Label answ4;
    public Button nextQuestion;

    private KahootGame game;

    private Thread createTimerThread(int time, TimerEnd finish) {
        return new Thread(() -> {
            int countdown = time;
            while (countdown > 0) {
                countdown -= 1;
                System.out.println(countdown);
                int finalCountdown = countdown;
                Platform.runLater(() -> timeLabel.setText(finalCountdown + ""));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            finish.finish();
        });
    }

    @Override
    public void getData(KahootGame data) {
        game = data;
        startNewQuestion();
    }

    private void startNewQuestion() {
        question.setText(game.getQuestion().getQuestion());
        nextQuestion.setDisable(true);
        timeLabel.setVisible(true);
        Platform.runLater(this::hideAnswers);
        createTimerThread(1, this::startAnswers).start();
    }

    private void startAnswers() {
        System.out.println("end");
        timeLabel.setVisible(true);
        Platform.runLater(this::showAnswers);
        Thread timerThread = createTimerThread(25, this::stopAnswers);
        game.startQuestion(() -> {
            timerThread.interrupt();
            Platform.runLater(() -> nextScene(Scenes.STATISTIC, game));
            return Unit.INSTANCE;
        });
        timerThread.start();
    }

    public void stopAnswers() {
        nextQuestion.setDisable(false);
        if (!game.isLastQuestion()) {
            nextQuestion.setDisable(true);
        }
    }

    private void showAnswers() {
        answ1.setText(game.getQuestion().getAnswers().get(0));
        answ2.setText(game.getQuestion().getAnswers().get(1));
        answ3.setText(game.getQuestion().getAnswers().get(2));
        answ4.setText(game.getQuestion().getAnswers().get(3));
        answ1.setVisible(true);
        answ2.setVisible(true);
        answ3.setVisible(true);
        answ4.setVisible(true);
    }

    private void hideAnswers() {
        answ1.setVisible(false);
        answ2.setVisible(false);
        answ3.setVisible(false);
        answ4.setVisible(false);
    }

    public void onNextQuestion(ActionEvent actionEvent) {
        game.nextQuestion();
        startNewQuestion();
    }

    interface TimerEnd {
        void finish();
    }
}
