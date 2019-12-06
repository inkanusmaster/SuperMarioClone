package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MarioClone extends ApplicationAdapter {
    private SpriteBatch batch; // private porobiłem jak coś. Batch tutaj obsługuje sprite'y
    private Texture background; // texture dodaje generalnie obrazek do naszej apki
    private Texture[] man; // tablica textur będzie zawierać png'i biegającego ziomka
    private int manState = 0;
    int pause = 0; // ziomek strasznie zapierdala. Trzeba go spowolnić

    @Override
    public void create() { // create jest gdy otwierasz grę pierwszy raz
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4]; // tablica 4 elementów bo są 4 png'i biegającego ziomka
        man[0] = new Texture("frame-1.png"); // przypisujemy pod kolejne indeksy kolejne obrazki
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
    }

    @Override
    public void render() { // render dzieje się caaaały czas wkółko dopóki nie zakończę gry
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // rysujemy: co, pozycja startowa, pozycja koncowa

        if (pause < 6) { // spowalnia ziomka. co 8 pętli renderuje się obrazek.
            pause++;
        } else {
            pause = 0;
            if (manState < 3) { // do odczytywania kolejnych indeksów w tabeli, czyli kolejnych obrazków animacji
                manState++;
            } else {
                manState = 0;
            }
        }

        batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, Gdx.graphics.getHeight() / 2); // ustawiamy ziomka na środku ekranu i ruszamy go
        batch.end();
    }

    @Override
    public void dispose() { // chyba automatycznie jest wykonywany dipose gdy wyłączamy grę, ale głowy nie dam.
        batch.dispose();
    }
}