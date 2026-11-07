package com.netlab.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.netlab.frontend.systems.BulletManager;

public class Boss extends Enemy {
    private ShootingPattern shootingPattern = new SpreadShot(200f, 3, 30f); // Pre-made plug-and-play strategy for Boss Cirno

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && !isDestroyed()) {
            shootingPattern.execute(x + width / 2 - 8, y, bulletManager, false);
        }
    }

    public ShootingPattern getShootingPattern() { return shootingPattern; }
    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }
}
