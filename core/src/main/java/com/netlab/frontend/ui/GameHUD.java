package com.netlab.frontend.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.netlab.frontend.observers.GameObserver;

public class GameHUD implements GameObserver {
    private long score;
    private int hp;
    private int spellCards;
    private int power;
    private int grazeCount;
    private int lives = 3;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public GameHUD() {
        // Lazy initialized during rendering to safely support headless CLI tests
    }

    // Observer Pattern Updates
    @Override
    public void onScoreChanged(long newScore) {
        this.score = newScore;
    }

    @Override
    public void onHpChanged(int currentHp) {
        this.hp = currentHp;
    }

    @Override
    public void onLivesChanged(int currentLives) {
        this.lives = currentLives;
    }

    @Override
    public void onSpellCardsChanged(int currentSpellCards) {
        this.spellCards = currentSpellCards;
    }

    @Override
    public void onPowerChanged(int currentPower) {
        this.power = currentPower;
    }

    @Override
    public void onGrazeChanged(int currentGraze) {
        this.grazeCount = currentGraze;
    }

    public void renderBackground() {
        if (Gdx.graphics != null && Gdx.gl != null) {
            if (shapeRenderer == null) {
                shapeRenderer = new ShapeRenderer();
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            // Playfield boundary box: X: 32..416, Y: 16..560 (Width 384, Height 544)
            shapeRenderer.rect(32, 16, 384, 544);

            // Sidebar boundary box: X: 432..768, Y: 16..560 (Width 336, Height 544)
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(432, 16, 336, 544);
            shapeRenderer.end();
        }
    }

    public void render(SpriteBatch batch) {
        if (batch != null) {
            if (font == null && Gdx.files != null) {
                font = new BitmapFont();
                font.setColor(Color.WHITE);
            }

            if (font != null) {
                font.draw(batch, "=== TOUHOU PRACTICUM ===", 448, 540);
                font.draw(batch, "HighScore: " + String.format("%09d", Math.max(score, 999990L)), 448, 500);
                font.draw(batch, "Score:     " + String.format("%09d", score), 448, 470);
                font.draw(batch, "Player:     " + (lives > 0 ? "★ ".repeat(lives) : "GAME OVER (Press R)"), 448, 430);
                font.draw(batch, "SpellCards: " + "★ ".repeat(Math.max(0, spellCards)), 448, 390);
                font.draw(batch, "Power:     " + power + " / 128", 448, 350);
                font.draw(batch, "Graze:     " + grazeCount, 448, 310);

                font.draw(batch, "--- CONTROLS ---", 448, 230);
                font.draw(batch, "Arrow / WASD : Move", 448, 200);
                font.draw(batch, "Shift        : Focus Mode", 448, 170);
                font.draw(batch, "Z            : Shoot", 448, 140);
                font.draw(batch, "X            : SpellCard (Bomb)", 448, 110);
                font.draw(batch, "R            : Restart (on Game Over)", 448, 80);
            }
        }
    }

    public void dispose() {
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    // Encapsulation getters for Testing
    public long getScore() { return score; }
    public int getHp() { return hp; }
    public int getLives() { return lives; }
    public int getSpellCards() { return spellCards; }
    public int getPower() { return power; }
    public int getGrazeCount() { return grazeCount; }
}
