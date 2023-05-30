package de.iv.game.lasertag.game;

public class GameProperty<T> {

    private T value;

    public GameProperty(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
