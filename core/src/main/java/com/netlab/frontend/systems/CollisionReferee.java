package com.netlab.frontend.systems;

import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.enemies.Enemy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        // 1. Mediator checks Player Bullets vs Enemies (Core Hitbox)
        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                    if (bullet.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                        System.out.println("[CollisionReferee] Player Bullet hit " + enemy.getName() + " for " + bullet.getDamage() + " DMG!");
                        boolean defeated = enemy.takeDamage(bullet.getDamage());
                        if (defeated) {
                            player.addScore(enemy.getScoreValue());
                            Item drop = EntityFactory.createItem(enemy.getX(), enemy.getY(), ItemType.POWER);
                            newDrops.add(drop);
                            System.out.println("[CollisionReferee] Defeated " + enemy.getName() + "! Dropped POWER item at (" + enemy.getX() + ", " + enemy.getY() + ")");
                        }
                        bullet.destroy(); // Mark bullet for recycling by BulletManager
                    }
                }
            }
        }

        // 2. Mediator checks Enemy Bullets vs Player (Core Hitbox vs Graze Hitbox)
        List<Bullet> currentEnemyBullets = new ArrayList<>(bulletManager.getActiveEnemyBullets());
        for (Bullet bullet : currentEnemyBullets) {
            if (bullet.isDestroyed()) continue;

            if (!player.isDestroyed()) {
                // Priority A: Direct Core Hitbox Collision (Damage & Life Loss)
                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                    if (!player.isInvulnerable()) {
                        System.out.println("[CollisionReferee] CORE HIT! Enemy Bullet hit " + player.getName() + " for " + bullet.getDamage() + " DMG!");
                        bullet.destroy(); // Mark bullet for recycling by BulletManager
                        player.takeDamage(100, bulletManager, entities);
                        break; // Fatal hit wipes all active enemy bullets; break out of bullet loop for this frame!
                    }
                } 
                // Priority B: Graze Hitbox Collision (Passes close to player without hitting core)
                else if (!bullet.hasBeenGrazed() && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                    bullet.setGrazed(true);
                    player.addGraze(); // Increments graze count, adds 50 pts, and notifies GameObserver HUD!
                    System.out.println("[CollisionReferee] GRAZE! " + player.getName() + " grazed an enemy bullet (+50 pts)!");
                }
            }
        }

        // 3. Mediator checks Player vs Items and Enemies (Core vs Graze)
        for (GameObject entity : entities) {
            if (entity.isDestroyed()) continue;

            if (entity instanceof Item item) {
                // Items collected via Core or Graze hitbox
                if (player.getCoreHitbox().overlaps(item.getCoreHitbox()) || player.getGrazeHitbox().overlaps(item.getCoreHitbox())) {
                    System.out.println("[CollisionReferee] Player collected item: " + item.getItemType());
                    player.collectItem(item);
                }
            } else if (entity instanceof Enemy enemy) {
                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    if (!player.isInvulnerable()) {
                        System.out.println("[CollisionReferee] CORE COLLISION! Player collided with " + enemy.getName() + "!");
                        player.takeDamage(100, bulletManager, entities);
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
