package ru.memory.game.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import ru.memory.game.MemoryGame;

public class Card extends Actor {

    public float status = -1f;
    public int animated = 0;
    public final static float ROTATE_TIME = 500f;
    private float size;

    private Sprite sprite;
    private Sprite cardBack;
    public String type;

    public boolean isAlive = true;

    public Card(float x, float y, float size, String type, MemoryGame game, final Field field, int arr_x, int arr_y) {
        super();
        this.size = size;
        this.type = type;

        setPosition(x, y);
        setSize(size, size);
        sprite = game.getSprite(type);
        cardBack = game.getSprite("cardBack");

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                field.touch(Card.this);
                return true;
            }
        });
    }

    public void startAnim() {
        if (status == 1)
            animated = -1;
        if (status == -1)
            animated = 1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (status >= 0) {
            sprite.setBounds(getX() + size * (1 - status) / 2f, getY(), size * status, size);
            sprite.draw(batch, parentAlpha);
        }
        else {
            float status = -this.status;
            cardBack.setBounds(getX() + size * (1 - status) / 2f, getY(), size * status, size);
            cardBack.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animated == 0)
            return;
        if (status >= 1 && animated == 1) {
            animated = 0;
            status = 1;
            return;
        }
        if (status <= -1 && animated == -1) {
            animated = 0;
            status = -1;
            return;
        }
        status += 2 * animated * (delta * 1000f / ROTATE_TIME);
    }
}
