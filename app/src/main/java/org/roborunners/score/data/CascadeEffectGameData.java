package org.roborunners.score.data;


import org.roborunners.score.R;
import org.roborunners.score.datatypes.ScoreElement;

import java.util.ArrayList;

public class CascadeEffectGameData {
    public static ArrayList<ScoreElement> getAuto() {
        ArrayList<ScoreElement> scoreElements = new ArrayList<>();
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_auto_platform,
                20,
                R.layout.card_scoreelement_binary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_auto_kickstand,
                30,
                R.layout.card_scoreelement_binary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_auto_goal,
                30,
                R.layout.card_scoreelement_quaternary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_auto_center,
                60,
                R.layout.card_scoreelement_binary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_auto_park,
                30,
                R.layout.card_scoreelement_quaternary));

        return scoreElements;
    }

    public static ArrayList<ScoreElement> getTeleop() {
        ArrayList<ScoreElement> scoreElements = new ArrayList<>();
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_teleop_30cm,
                1,
                R.layout.card_scoreelement_numeric));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_teleop_60cm,
                2,
                R.layout.card_scoreelement_numeric));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_teleop_90cm,
                3,
                R.layout.card_scoreelement_numeric));

        return scoreElements;
    }

    public static ArrayList<ScoreElement> getEndgame() {
        ArrayList<ScoreElement> scoreElements = new ArrayList<>();
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_endgame_park,
                10,
                R.layout.card_scoreelement_quinary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_endgame_off_ground,
                30,
                R.layout.card_scoreelement_quinary));
        scoreElements.add(new ScoreElement(
                R.string.scoreelement_cascade_endgame_center,
                6,
                R.layout.card_scoreelement_numeric));

        return scoreElements;
    }
}
