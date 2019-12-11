package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MarioClone extends ApplicationAdapter {
    private SpriteBatch batch; // private porobiłem jak coś. Batch tutaj obsługuje sprite'y
    private Texture background; // texture dodaje generalnie obrazek do naszej apki
    private Texture[] man; // tablica textur będzie zawierać png'i biegającego ziomka
    private int manState = 0;
    private int pause = 0; // ziomek strasznie zapierdala. Trzeba go spowolnić
    private final float gravity = 1.2f; // robimy fizykę do skakania. Tutaj jakaś wartość gravity
    private float velocity = 0; // robimy fizykę. tutaj prędkość czegoś
    private int manY = 0; // pozycja ziomka. On będzie skakał - więc zmienna Y
    private Rectangle manRectangle;
    private int score = 0;
    private BitmapFont font; // będziemy w tym pokazywać punkty
    private int gameState = 0;
    private Texture dizzy;

//    private boolean canJump; // żeby nie dało się skskać w locie, tylko jak ziomek opadnie na podłogę

    private ArrayList<Integer> coinX = new ArrayList<>(); // arraylist z pozycjami X dla coina
    private ArrayList<Integer> coinY = new ArrayList<>(); // arraylist z pozycjami Y dla coina
    private ArrayList<Rectangle> coinRectangle = new ArrayList<>(); // rectangle. Do kolizji. Nadaje kształt obiektom
    private int coinCount; // licznik co ile pętli będzie się coin pojawiał
    private Texture coin; // teksturka dla coina

    private ArrayList<Integer> bombX = new ArrayList<>(); // arraylist z pozycjami X dla bomby
    private ArrayList<Integer> bombY = new ArrayList<>(); // arraylist z pozycjami Y dla bomby
    private ArrayList<Rectangle> bombRectangle = new ArrayList<>(); // rectangle. Do kolizji. Nadaje kształt obiektom
    private int bombCount; // licznik co ile pętli będzie się bomb pojawiał
    private Texture bomb; // teksturka dla bomby

    private Random random; // zmienna random do losowania gdzie będzie się pojawiać element nowy

    @Override
    public void create() { // create jest gdy otwierasz grę pierwszy raz
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4]; // tablica 4 elementów bo są 4 png'i biegającego ziomka
        man[0] = new Texture("frame-1.png"); // przypisujemy pod kolejne indeksy kolejne obrazki
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
        dizzy = new Texture("dizzy-1.png");
        manY = Gdx.graphics.getHeight() / 2; // ustawiamy ziomka w pozycji początkowej na środku ekranu (wysokość)
        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        random = new Random();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
    }

    private void makeCoin() { // metoda zapisuje do arrayów pozycje coina
        float height = random.nextFloat() * Gdx.graphics.getHeight(); // ustawiamy losową wysokość dla coina
        coinY.add((int) height); // robimy Y
        coinX.add(Gdx.graphics.getWidth()); // robimy X na końcu ekranu z prawej
    }

    private void makeBomb() { // metoda zapisuje do arrayów pozycje coina
        float height = random.nextFloat() * Gdx.graphics.getHeight(); // ustawiamy losową wysokość dla coina
        bombY.add((int) height); // robimy Y
        bombX.add(Gdx.graphics.getWidth()); // robimy X na końcu ekranu z prawej
    }

    @Override
    public void render() { // render dzieje się caaaały czas wkółko dopóki nie zakończę gry
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // rysujemy: co, pozycja startowa, pozycja koncowa

        if (gameState == 1) { // sprawdzamy gamestate. Jeśli 1 to gra działa
            if (coinCount < 120) { // co 120 pętli zrób coina (tzn dodaje koordynaty do arraya)
                coinCount++;
            } else {
                coinCount = 0;
                makeCoin();
            }

            if (bombCount < 150) { // co 150 pętli zrób bomb (tzn dodaje koordynaty do arraya)
                bombCount++;
            } else {
                bombCount = 0;
                makeBomb();
            }

            coinRectangle.clear(); // musimy czyścić  za każdym razem
            for (int i = 0; i < coinX.size(); i++) { // loopujemy przez wszystkie coiny w arrayliście (x lub y).
                batch.draw(coin, coinX.get(i), coinY.get(i)); // rysuj coin pozycja element i z tablicy X i Y
                coinX.set(i, coinX.get(i) - 10); // musimy coina przesuwać w lewo. Wartość po minusie to prędkość. Zmieniamy wartość X w array
                coinRectangle.add(new Rectangle(coinX.get(i), coinY.get(i), coin.getWidth(), coin.getHeight())); // robimy rectangle na coinie
            }

            bombRectangle.clear(); // musimy czyścić  za każdym razem
            for (int i = 0; i < bombX.size(); i++) {  // j.w dla bomb
                batch.draw(bomb, bombX.get(i), bombY.get(i));
                bombX.set(i, bombX.get(i) - 15); // bomba szybciej zasuwa
                bombRectangle.add(new Rectangle(bombX.get(i), bombY.get(i), bomb.getWidth(), bomb.getHeight())); // robimy rectangle na bombie
            }

            if (Gdx.input.justTouched() /*&& canJump*/) { // skakanie tutaj zaimplementowane. Na dotyk ekranu będzie.
                velocity = -35; // podbijamy ziomka do góry. Z każdą iteracją będzie leciał do góry, a potem spadał zgodnie z tym co jest w sekcji "nakurwiamy fizykę"
//            canJump = false; // żeby nie dało się wcisnąć skakania jak ziomek leci
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

            velocity += gravity; // prędkość to prędkość plus grawitacja
            manY -= velocity; // opada ziomek

            if (manY <= 0) {  // robimy if żeby jak ziomek wyląduje na ziemi, nie spadał pod nią (manY spada cały czas więc dlatego warunek <=)
                manY = 0;
//            canJump = true; // ziomek opadł. Możemy skoczyć ponownie
            }

            if (manY >= Gdx.graphics.getHeight()) { // żeby nie wyjebało go w kosmos za bardzo. Tylko na wysokość postaci.
                manY = Gdx.graphics.getHeight();
            }

        } else if (gameState == 0) { // czekamy na start.
            if (Gdx.input.justTouched()) { // start na tapnięcie
                gameState = 1;
            }
        } else if (gameState == 2) { // koniec gry
            if (Gdx.input.justTouched()) { // start na tapnięcie
                gameState = 1;
                manY = Gdx.graphics.getHeight() / 2; // ustawiamy ziomka w pozycji początkowej na środku ekranu (wysokość)
                score = 0; // reset score
                velocity = 0; // reset velocity
                coinX.clear(); // czyścimy coiny
                coinY.clear();
                coinRectangle.clear();
                coinCount = 0;
                bombX.clear(); // czyścimy bomby
                bombY.clear();
                bombRectangle.clear();
                bombCount = 0;
            }
        }

        if (gameState == 2) {
            batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth(), manY); // rysujemy dizzy ziomka
        } else {
            batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth(), manY); // ustawiamy ziomka na środku ekranu, pozycja manY i ruszamy go.
        }
        manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth(), manY, man[manState].getWidth(), man[manState].getHeight()); // robimy prostokąt na ziomku (współrzędne początkowe i końcowe.

        // na końcu potem jak już wszystko jest wymalowane i porobione prostokąty - sprawdzamy kolizę.

        for (int i = 0; i < coinRectangle.size(); i++) { // sprawdzamy coiny. Robimy loop for wszystkie coiny (ilość wszystkich coinów)
            if (Intersector.overlaps(manRectangle, coinRectangle.get(i))) { // sprawdzamy czy prostokąt ludka nachodzi na prostokąt danej monety
                score++;
                coinRectangle.remove(i); // pozbycie się rectangla coina i samego coina coina jak się w niego wejdzie bo nabija się punkty przez jakiś czas
                coinX.remove(i);
                coinY.remove(i);
//                break; // wychodzimy z pętli for. nie wiem po co to więc zakomentuję
            }
        }

        for (int i = 0; i < bombRectangle.size(); i++) { // sprawdzamy bomby. Robimy loop for wszystkie bomby (ilość wszystkich bomb)
            if (Intersector.overlaps(manRectangle, bombRectangle.get(i))) { // sprawdzamy czy prostokąt ludka nachodzi na prostokąt danej bomby
                bombRectangle.remove(i); // podobnie pozbywamy się bomby
                bombX.remove(i);
                bombY.remove(i);
                gameState = 2; // game over
//                break; // wychodzimy z pętli for. nie wiem po co to więc zakomentuję
            }
        }

        font.draw(batch, String.valueOf(score), 50, Gdx.graphics.getHeight() - 20); // tu wypisujemy punkty. W pozycji 100,200

        batch.end();
    }

    @Override
    public void dispose() { // chyba automatycznie jest wykonywany dipose gdy wyłączamy grę, ale głowy nie dam.
        batch.dispose();
    }
}