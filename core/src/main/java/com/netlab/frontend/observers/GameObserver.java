package com.netlab.frontend.observers;

public interface GameObserver {
    void onScoreChanged(long newScore);
    void onHpChanged(int currentHp);
    void onSpellCardsChanged(int currentSpellCards);
    void onPowerChanged(int currentPower);
    void onGrazeChanged(int currentGraze);
    default void onLivesChanged(int currentLives) {}
}
