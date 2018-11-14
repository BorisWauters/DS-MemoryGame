package com.henri.client.GUI.GameScreen4X6;

public class GameScreen4X6Controller {

    private boolean viewOnly = false;
    private int gameId;
    private boolean join = false;


    public boolean isViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

}
