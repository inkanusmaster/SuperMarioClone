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
    private int pause = 0; // ziomek strasznie zapierdala. Trzeba go spowolnić
    private final float gravity = 1.2f; // robimy fizykę do skakania. Tutaj jakaś wartość gravity
    private float velocity = 0; // robimy fizykę. tutaj prędkość czegoś
    private int manY = 0; // pozycja ziomka. On będzie skakał - więc zmienna Y
    private boolean canJump; // żeby nie dało się skskać w locie, tylko jak ziomek opadnie na podłogę


    @Override
    public void create() { // create jest gdy otwierasz grę pierwszy raz
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4]; // tablica 4 elementów bo są 4 png'i biegającego ziomka
        man[0] = new Texture("frame-1.png"); // przypisujemy pod kolejne indeksy kolejne obrazki
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
        manY = Gdx.graphics.getHeight() / 2; // ustawiamy ziomka na środku ekranu (wysokość)
    }

    @Override
    public void render() { // render dzieje się caaaały czas wkółko dopóki nie zakończę gry
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // rysujemy: co, pozycja startowa, pozycja koncowa

        if (Gdx.input.justTouched() && canJump) { // skakanie tutaj zaimplementowane. Na dotyk ekranu będzie.
            velocity = -35; // podbijamy ziomka do góry. Z każdą iteracją będzie leciał do góry, a potem spadał zgodnie z tym co jest w sekcji "nakurwiamy fizykę"
            canJump = false;
        }

        if (pause < 6) { // spowalnia ziomka. co 8 pętli renderuje się obrazek z biegnącym ziomkiem.
            pause++;
        } else {
            pause = 0;
            if (manState < 3) { // do odczytywania kolejnych indeksów w tabeli, czyli kolejnych obrazków animacji
                manState++;
            } else {
                manState = 0;
            }
        }
        // nakurwiamy fizykę
        velocity += gravity; // prędkość to prędkość plus grawitacja
        manY -= velocity; // opada ziomek

        if (manY <= 0) {  // robimy if żeby jak ziomek wyląduje na ziemi, nie spadał pod nią (manY spada cały czas więc dlatego warunek <=)
            manY = 0;
            canJump = true;
        }
        batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY); // ustawiamy ziomka na środku ekranu, pozycja manY i ruszamy go.
        batch.end();
    }

    @Override
    public void dispose() { // chyba automatycznie jest wykonywany dipose gdy wyłączamy grę, ale głowy nie dam.
        batch.dispose();
    }
}