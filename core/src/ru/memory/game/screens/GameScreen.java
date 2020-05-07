package ru.memory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import ru.memory.game.MemoryGame;
import ru.memory.game.logic.Button;
import ru.memory.game.logic.Card;
import ru.memory.game.logic.DifficultControl;
import ru.memory.game.logic.Field;

public class GameScreen implements Screen {

    MemoryGame game;

    Stage stage;
    StretchViewport viewport;
    SpriteBatch batch;

    String difficult;
    Field field;
    int playersCount;
    int finalScore;

    BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/font_big.fnt"));
    BitmapFont big_font = new BitmapFont(Gdx.files.internal("fonts/font_big.fnt"));
    Button restart;
    boolean isGameEnded = false;

    public GameScreen(MemoryGame game, String difficult, int playersCount) {
        this.game = game;
        this.difficult = difficult;
        this.playersCount = playersCount;

        batch = new SpriteBatch();
        viewport = new StretchViewport(game.WIDTH, game.HEIGHT);
        stage = new Stage(viewport, batch);
        //stage.setDebugAll(true);

        font.getData().setScale(0.3f);
        big_font.getData().setScale(0.7f);

        initUI();
    }

    private void initUI() {
        Button back = new Button("back1", "back2", "back3", 0f,
                game.HEIGHT - 50f, 50f, 50f, game);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });
        stage.addActor(back);

        restart = new Button("restart1", "restart2", "restart3", game.WIDTH - 50f,
                0f, 50f, 50f, game);
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restart.addAction(Actions.scaleTo(50f / restart.getWidth(),
                        50f / restart.getHeight(), 0.5f));
                restart.addAction(Actions.moveTo(game.WIDTH - 50f, 0f, 0.5f));
                field.score_1 = 0;
                field.score_2 = 0;
                isGameEnded = false;
                if (field.isGameStopped)
                    return;
                field.isGameStopped = true;
                for (int i = 0; i < field.height; ++i) {
                    for (int j = 0; j < field.width; ++j) {
                        field.field[i][j].animated = 1;
                    }
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        for (int i = 0; i < field.height; ++i) {
                            for (int j = 0; j < field.width; ++j) {
                                if (field.field[i][j].status != -1) field.field[i][j].startAnim();
                            }
                        }
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                field.restartGame();
                            }
                        }, (Card.ROTATE_TIME + 100f) / 1000f);
                    }
                }, (Card.ROTATE_TIME + 1200f) / 1000f);
            }
        });
        restart.setTransform(true);
        stage.addActor(restart);

        field = new Field(difficult, game, stage, playersCount);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0.5f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        batch.begin();
        font.draw(batch, "Turns - " + field.turns, 630f, game.HEIGHT - 20f);
        if (playersCount > 1) font.draw(batch, "" + field.score_2,
                game.WIDTH - field.cardSize / 2f - (difficult.equals("Easy") ? 5:10), field.cardOff_y_2 - 10f);
        font.draw(batch, "" + field.score_1,
                field.cardSize / 2f - (difficult.equals("Easy") ? 5:10), field.cardOff_y_2 - 10f);
        if (playersCount > 1) font.draw(batch, (field.isFirstPlayersTurn ? "First " : "Second ") +
                    "players turn",250f, 20f);

        if ((field.score_1 + field.score_2) * 2 == field.width * field.height && !isGameEnded) {
            isGameEnded = true;
            restart.addAction(Actions.scaleTo(1.8f, 1.8f, 0.5f));
            restart.addAction(Actions.moveTo(game.WIDTH / 2f - 45f, 30f, 0.5f));

            if (playersCount == 1) {
                float c = DifficultControl.DIFFICULT_COEFFICIENT_EASY;
                if (difficult.charAt(0) == 'H')
                    c = DifficultControl.DIFFICULT_COEFFICIENT_HARD;
                if (difficult.charAt(0) == 'I')
                    c = DifficultControl.DIFFICULT_COEFFICIENT_IMPOSSIBLE;
                if (difficult.charAt(0) == 'N')
                    c = DifficultControl.DIFFICULT_COEFFICIENT_NORMAL;
                finalScore = (int) Math.floor(100f * field.width * field.height / field.turns * c / 2f);

                Preferences pref = Gdx.app.getPreferences("Memory Game");
                int best = pref.getInteger("bestScore", 0);
                if (finalScore > best)
                    pref.putInteger("bestScore", finalScore);
                pref.flush();
            }
        }

        if(isGameEnded) {
            if (playersCount == 1) {
                big_font.draw(batch, "Your score is " + finalScore,140f, 250f);
            }
            else {
                big_font.draw(batch, (field.score_1 > field.score_2 ? "1-st " : "2-nd ") +
                                "player won", 110f, 270f);
            }
        }

        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
        batch.dispose();
    }

}
