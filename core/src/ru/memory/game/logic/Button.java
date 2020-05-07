package ru.memory.game.logic;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ru.memory.game.MemoryGame;

public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button {

    public Button(String up, String checked, String down, float x, float y, float width, float height, MemoryGame game) {
        super(new ButtonStyle());
        setTransform(true);

        ButtonStyle style = new ButtonStyle();
        style.up = new TextureRegionDrawable(game.atlas.findRegion(up));
        style.down = new TextureRegionDrawable(game.atlas.findRegion(down));
        style.focused = new TextureRegionDrawable(game.atlas.findRegion(checked));
        style.over = new TextureRegionDrawable(game.atlas.findRegion(checked));

        setStyle(style);

        setSize(width, height);
        setPosition(x, y);
    }
}
