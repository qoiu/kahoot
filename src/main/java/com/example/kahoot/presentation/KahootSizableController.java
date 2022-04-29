package com.example.kahoot.presentation;

import com.example.kahoot.domain.model.Kahoot;
import com.example.kahoot.domain.model.KahootQuestion;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KahootSizableController extends BaseController<Kahoot> {
    public TextField header;
    public ListView<String> list;
    public TextField answ1;
    public TextField answ2;
    public TextField answ3;
    public TextField answ4;
    public CheckBox trueFalse;
    public ToggleGroup group;

    private Kahoot kahoot;
    private int currentId = 0;
    private boolean writableState = true;

    @FXML
    private RadioButton answRbtn1;
    @FXML
    private RadioButton answRbtn2;
    @FXML
    private RadioButton answRbtn3;
    @FXML
    private RadioButton answRbtn4;

    private String getCorrect() {
        if (answRbtn1.isSelected()) return answ1.getText();
        if (answRbtn2.isSelected()) return answ2.getText();
        if (answRbtn3.isSelected()) return answ3.getText();
        return answ4.getText();
    }

    private KahootQuestion newQuestion() {
        return new KahootQuestion("New question " + kahoot.getQuestions().size(), Arrays.asList("", "", "", ""), "");
    }

    private void save() {
        KahootQuestion q = getQuestion();
        q.setQuestion(header.getText());
        List<String> answers = Arrays.asList(
                answ1.getText(),
                answ2.getText(),
                answ3.getText(),
                answ4.getText()
        );
        q.setAnswers(answers);
        q.setCorrect(getCorrect());
    }

    private void updateScreen() {
        writableState = false;
        KahootQuestion question = getQuestion();
        header.setText(question.getQuestion());
        try {
            trueFalse.setSelected(
                    question.getAnswers().get(0).equals("True") && question.getAnswers().get(1).equals("False")
            );
            changeTrueFalse();
            answ1.setText(question.getAnswers().get(0));
            answ2.setText(question.getAnswers().get(1));
            answ3.setText(question.getAnswers().get(2));
            answ4.setText(question.getAnswers().get(3));
            if (question.getAnswers().get(0).equals(question.getCorrect())) answRbtn1.setSelected(true);
            if (question.getAnswers().get(1).equals(question.getCorrect())) answRbtn2.setSelected(true);
            if (question.getAnswers().get(2).equals(question.getCorrect())) answRbtn3.setSelected(true);
            if (question.getAnswers().get(3).equals(question.getCorrect())) answRbtn4.setSelected(true);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        writableState = true;
    }

    private void updateList() {
        ArrayList<String> questions = new ArrayList<>();
        for (KahootQuestion question : kahoot.getQuestions()) {
            questions.add(question.getQuestion());
        }
        questions.add("New");
        list.getSelectionModel().clearSelection();
        list.setItems(FXCollections.observableArrayList(questions));
    }

    public void changeTrueFalse() {
        if (trueFalse.isSelected()) {
            answ1.setText("True");
            answ2.setText("False");
            answRbtn1.setSelected(true);
            answ3.setVisible(false);
            answ4.setVisible(false);
            answRbtn3.setVisible(false);
            answRbtn4.setVisible(false);
        } else {
            answ1.setText("");
            answ2.setText("");
            answ3.setVisible(true);
            answ4.setVisible(true);
            answRbtn3.setVisible(true);
            answRbtn4.setVisible(true);
        }
        saveOnInput();
    }

    @Override
    public void getData(Kahoot data) {
        kahoot = data;
        if (kahoot.getQuestions().size() == 0) {
            kahoot.getQuestions().add(newQuestion());
        }
        updateList();
        updateScreen();
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
    public void init() {
        super.init();
        list.getSelectionModel().selectedItemProperty().addListener(getListener());
        header.textProperty().addListener((obs) -> saveOnInput());
        answ1.textProperty().addListener((obs) -> saveOnInput());
        answ2.textProperty().addListener((obs) -> saveOnInput());
        answ3.textProperty().addListener((obs) -> saveOnInput());
        answ4.textProperty().addListener((obs) -> saveOnInput());
    }

    @NotNull
    private ChangeListener<String> getListener() {
        return (observableValue, s, t1) -> {
            if (t1 == null) return;
            if (t1.equals("New")) {
                KahootQuestion nq = newQuestion();
                kahoot.getQuestions().add(nq);
                currentId = kahoot.getQuestions().size();
                updateList();
                list.getSelectionModel().selectIndices(currentId - 1);
            } else {
                currentId = list.getSelectionModel().getSelectedIndex();
                updateList();
                updateScreen();
            }
        };
    }


    public void onSaveKahootAction(ActionEvent actionEvent) {
        saveOnInput();
        interactor.saveKahoot(kahoot);
        usualClose();
    }

    private void saveOnInput() {
        if (writableState) {
            save();
            updateList();
        }
    }

    public void changeValue(ActionEvent actionEvent) {
        saveOnInput();
    }

    private KahootQuestion getQuestion() {
        return kahoot.getQuestions().get(currentId);
    }
}
