package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MarioClone extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() { // create jest gdy otwierasz grę pierwszy raz
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() { // render dzieje się caaaały czas wkółko dopóki nie zakończę gry
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }
	
    @Override
    public void dispose() { // chyba automatycznie jest wykonywany dipose gdy wyłączamy grę, ale głowy nie dam.
        batch.dispose();
        img.dispose();
    }
}
