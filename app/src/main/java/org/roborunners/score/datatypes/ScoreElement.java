package org.roborunners.score.datatypes;

import org.parceler.Parcel;


@Parcel
public class ScoreElement {
    private int description;
    private int state;
    private int value;
    private int layout;

    @SuppressWarnings("unused")
    public ScoreElement() { }

    public ScoreElement(int description, int value, int layout) {
        this.description = description;
        this.state = 0;
        this.value = value;
        this.layout = layout;
    }

    public int getDescription() {
        return description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getValue() {
        return value;
    }

    public int getLayout() {
        return layout;
    }

    public int score() {
        return state * value;
    }
}
