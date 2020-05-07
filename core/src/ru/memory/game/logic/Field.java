package ru.memory.game.logic;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.memory.game.MemoryGame;

public class Field {

    MemoryGame game;
    String difficult;
    Stage stage;

    public int width, height;
    public float x, y, cardSize;
    public final float SPACE = 10f;

    public float start_x, start_y;
    public float cardOff_x_2, cardOff_y_2;
    public float cardOff_x_1, cardOff_y_1;

    public Card[][] field;

    public Card open = null;
    public boolean isWaiting = false, isGameStopped = false, isFirstPlayersTurn = true;
    public int score_1 = 0;
    public int score_2 = 0;
    public int turns = 0;
    public int playersCount;

    public Field(String difficult, MemoryGame game, Stage stage, int playersCount) {
        this.game = game;
        this.difficult = difficult;
        this.stage = stage;
        this.playersCount = playersCount;

        init();
    }

    private void init() {
        if (difficult.equals("Easy")) {
            cardSize = DifficultControl.EASY_S;
            width = DifficultControl.EASY_W;
            height = DifficultControl.EASY_H;
        }
        else if (difficult.equals("Normal")) {
            cardSize = DifficultControl.NORMAL_S;
            width = DifficultControl.NORMAL_W;
            height = DifficultControl.NORMAL_H;
        }
        else if (difficult.equals("Hard")) {
            cardSize = DifficultControl.HARD_S;
            width = DifficultControl.HARD_W;
            height = DifficultControl.HARD_H;
        }
        else {
            cardSize = DifficultControl.IMPOSSIBLE_S;
            width = DifficultControl.IMPOSSIBLE_W;
            height = DifficultControl.IMPOSSIBLE_H;
        }

        float size_x = width * cardSize + SPACE * (width - 1);
        float size_y = height * cardSize + SPACE * (height - 1);

        x = game.WIDTH / 2f - size_x / 2f;
        y = game.HEIGHT / 2f - size_y / 2f;

        start_x = game.WIDTH / 2f - cardSize / 2f;
        start_y = game.HEIGHT - cardSize;

        cardOff_x_2 = game.WIDTH - cardSize;
        cardOff_x_1 = 0f;
        cardOff_y_2 = game.HEIGHT / 2f - cardSize / 2f - (width * height / 4f * 5f);
        cardOff_y_1 = game.HEIGHT / 2f - cardSize / 2f - (width * height / 4f * 5f);

        field = new Card[height][width];
        generateCards();
    }

    private void generateCards() {
        int cardsCount = width * height / 2;

        List<String> cards = Arrays.asList(DifficultControl.CARDS);
        Collections.shuffle(cards);
        ArrayList<String> cardsSet = new ArrayList<>();
        for (int i = 0; i < cardsCount; ++i) {
            cardsSet.add(cards.get(i));
            cardsSet.add(cards.get(i));
        }
        Collections.shuffle(cardsSet);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (field[i][j] != null) field[i][j].remove();
            }
        }

        isGameStopped = false;

        for (int i = 0; i < cardsCount * 2; ++i) {
            String type = cardsSet.get(i);
            int x = i / width;
            int y = i % width;

            float to_x = this.x + y * (cardSize + SPACE), to_y = this.y + x * (cardSize + SPACE);
            field[x][y] = new Card(start_x, start_y, cardSize, type, game, this, x, y);

            stage.addActor(field[x][y]);
            float delay = 1f;
            if (difficult.equals("Normal"))
                delay = 0.8f;
            else if (difficult.equals("Hard"))
                delay = 0.7f;
            else if (difficult.equals("Impossible")) {
                delay = 0.45f;
            }
            field[x][y].addAction(new SequenceAction(Actions.delay(i * 1f / 6f), Actions.moveTo(to_x, to_y, delay, Interpolation.fastSlow)));
        }
    }

    public void touch(final Card card) {
        if (card.status != -1 || !card.isAlive || isWaiting || isGameStopped)
            return;
        card.startAnim();

        if (open == null) {
            open = card;
            return;
        }

        turns++;

        if (open.type.equals(card.type)) {
            open.isAlive = false;
            card.isAlive = false;
            float to_x = isFirstPlayersTurn ? cardOff_x_1 : cardOff_x_2;
            float to_y = isFirstPlayersTurn ? cardOff_y_1 : cardOff_y_2;
            int playerScore = isFirstPlayersTurn ? score_1 : score_2;
            card.addAction(new SequenceAction(Actions.delay((Card.ROTATE_TIME + 400f) / 1000f),
                    Actions.moveTo(to_x, to_y + playerScore * 5f, 1f, Interpolation.fastSlow)));
            card.setZIndex(playerScore * 2);
            open.addAction(new SequenceAction(Actions.delay((Card.ROTATE_TIME + 300f) / 1000f),
                    Actions.moveTo(to_x, to_y + playerScore * 5f + 5f, 1f, Interpolation.fastSlow)));
            open.setZIndex(playerScore * 2 + 1);
            if (isFirstPlayersTurn)
                score_1++;
            else
                score_2++;
            open = null;
        }
        else {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    open.startAnim();
                    card.startAnim();
                    open = null;
                    isWaiting = false;
                    if (playersCount > 1) isFirstPlayersTurn = !isFirstPlayersTurn;
                }
            }, (Card.ROTATE_TIME + 600f)  / 1000f);
            isWaiting = true;
        }
    }

    public void restartGame() {
        score_1 = 0;
        score_2 = 0;
        turns = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                field[i][j].setZIndex(0);
                field[i][j].addAction(Actions.moveTo(start_x, start_y, 0.8f, Interpolation.smoother));
            }
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                generateCards();
            }
        }, 0.8f);
    }

}
