package com.netlab.frontend.objects.patterns.shootingStrategy;

import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.patterns.ShootingPattern;
import com.netlab.frontend.systems.BulletManager;

public class SpreadShot implements ShootingPattern {
    private float speed;
    private int numBullets;
    private float spreadAngleDegrees;
    private GameObject target;
    private int damage = 1;

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees) {
        this(speed, numBullets, spreadAngleDegrees, null, 1);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, int damage) {
        this(speed, numBullets, spreadAngleDegrees, null, damage);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, GameObject target) {
        this(speed, numBullets, spreadAngleDegrees, target, 1);
    }

    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, GameObject target, int damage) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        // Base direction angle: Aim at target if present (ZUN FAN_AIMED), otherwise default 90 deg (Player) / 270 deg (Enemy)
        float centerAngle;
        if (target != null) {
            float targetCenterX = target.getX() + target.getWidth() / 2f;
            float targetCenterY = target.getY() + target.getHeight() / 2f;
            centerAngle = (float) Math.toDegrees(Math.atan2(targetCenterY - originY, targetCenterX - originX));
        } else {
            centerAngle = isPlayer ? 90f : 270f;
        }

        float startAngle = centerAngle - (spreadAngleDegrees / 2f);
        float angleStep = (numBullets > 1) ? (spreadAngleDegrees / (numBullets - 1)) : 0;

        int bulletDamage = isPlayer ? 25 : this.damage;

        for (int i = 0; i < numBullets; i++) {
            float angleDeg = startAngle + (i * angleStep);
            float angleRad = (float) Math.toRadians(angleDeg);

            float vx = (float) (speed * Math.cos(angleRad));
            float vy = (float) (speed * Math.sin(angleRad));

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(originX, originY, vx, vy, bulletDamage);
            } else {
                bulletManager.spawnEnemyBullet(originX, originY, vx, vy, bulletDamage);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }

    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float spreadAngleDegrees) { this.spreadAngleDegrees = spreadAngleDegrees; }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
