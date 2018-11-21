package com.henri.client.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class tableItem {

    private SimpleStringProperty score;
    private SimpleStringProperty username;

    public tableItem(String username, String score){
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleStringProperty(score);
    }

    public String getScore() {
        return score.get();
    }

    public StringProperty scoreProperty() {
        return score;
    }

    public void setScore(String score) {
        this.score.set(score);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}
