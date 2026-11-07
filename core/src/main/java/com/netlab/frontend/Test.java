package com.netlab.frontend;

import com.netlab.frontend.commands.BombCommand;
import com.netlab.frontend.commands.Command;
import com.netlab.frontend.commands.FocusCommand;
import com.netlab.frontend.commands.InputHandler;
import com.netlab.frontend.commands.MoveCommand;
import com.netlab.frontend.commands.ShootCommand;
import com.netlab.frontend.observers.GameObserver;
import com.netlab.frontend.objects.GameObject;
import com.netlab.frontend.objects.Player;
import com.netlab.frontend.objects.bullets.Bullet;
import com.netlab.frontend.objects.bullets.BulletType;
import com.netlab.frontend.objects.enemies.Boss;
import com.netlab.frontend.objects.enemies.Enemy;
import com.netlab.frontend.objects.enemies.Fairy;
import com.netlab.frontend.objects.items.Item;
import com.netlab.frontend.objects.items.ItemType;
import com.netlab.frontend.systems.BulletManager;
import com.netlab.frontend.systems.CollisionReferee;
import com.netlab.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Test runner = new Test(); // Instantiating Test runner instance

        // ==========================================
        // MODULE 1: BASIC CLASSES & OBJECT INTERACTION
        // ==========================================
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 1: BASIC CLASSES & OBJECT INTERACTION ===");

        // Instantiating objects (Player and Enemy)
        Player reimu = new Player("Reimu Hakurei", 100, 15, 3);
        Enemy fairyBoss = new Enemy("Cirno (Stage 2 Boss)", 50);

        System.out.println("\n--- Initial Battle State ---");
        System.out.println("Player: " + reimu.getName() + " | HP: " + reimu.getHp() + " | Power: " + reimu.getPower() + " | SpellCards: " + reimu.getSpellCards());
        System.out.println("Enemy:  " + fairyBoss.getName() + " | HP: " + fairyBoss.getHp());

        System.out.println("\n--- Turn 1: Player Shoots Enemy ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 2: Enemy Counter-attacks ---");
        fairyBoss.attack(reimu, 30);

        System.out.println("\n--- Turn 3: Player Shoots Enemy Finishing Blow ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 4: Enemy Deals Fatal Damage to Reimu ---");
        fairyBoss.attack(reimu, 80);

        System.out.println("\n=== Battle Simulation Complete ===");


        // ==========================================
        // MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM ===");

        // Instantiating polymorphic objects
        Player reimu2 = new Player("Reimu Hakurei", 100, 15, 3);
        Fairy fairy = new Fairy("Stage 1 Fairy", 20);
        Boss cirno = new Boss("Cirno (Stage 2 Boss)", 150);
        Item pointItem = new Item(200, 450, 12, 12, 120f, "Point Item", 1000L);

        System.out.println("\n--- Testing Encapsulation & Inheritance ---");
        System.out.println("Player: " + reimu2.getName() + " | Position: (" + reimu2.getX() + ", " + reimu2.getY() + ")");
        System.out.println("Fairy:  " + fairy.getName() + " | Defeat Worth: " + fairy.getScoreValue() + " pts");
        System.out.println("Boss:   " + cirno.getName() + " | Defeat Worth: " + cirno.getScoreValue() + " pts | Size: " + cirno.getWidth() + "x" + cirno.getHeight());
        System.out.println("Item:   " + pointItem.getItemType() + " | Value: " + pointItem.getScoreValue() + " pts | Speed: " + pointItem.getSpeed());

        System.out.println("\n--- Testing Item Movement Update ---");
        System.out.println("Initial Item Y: " + pointItem.getY());
        pointItem.update(0.5f);
        System.out.println("Item Y after 0.5s update: " + pointItem.getY() + " (linear downward movement)");

        System.out.println("\n--- Testing Scoring System ---");
        System.out.println("Initial Score: " + reimu2.getScore());
        reimu2.shoot(fairy);
        reimu2.collectItem(pointItem);
        reimu2.shoot(cirno);
        System.out.println("Final Score: " + reimu2.getScore() + " pts");

        System.out.println("\n=== Module 2 Test Completed Successfully ===");


        // ==========================================
        // MODULE 3: POLYMORPHISM, ABSTRACTION & COLLISION HANDLING
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 3: POLYMORPHISM & ABSTRACTION ===");

        // 1. Testing Abstraction & Enums
        System.out.println("--- Testing ItemType & BulletType Enums ---");
        System.out.println("Available Item Types: " + ItemType.POWER + ", " + ItemType.POINT + ", " + ItemType.BOMB + ", " + ItemType.LIFE);
        System.out.println("Available Bullet Types: " + BulletType.DANMAKU + ", " + BulletType.AMULET + ", " + BulletType.LASER + ", " + BulletType.MASTER_SPARK);

        // 2. Initializing List<GameObject> entities for iterative updates
        List<GameObject> entities = new ArrayList<>();
        Player p = new Player(100, 100, "Reimu Hakurei", 100, 15, 3);
        Fairy f = new Fairy(100, 114, "Stage 1 Fairy", 20);     // Placed at (100, 114) -> Overlaps player core hitbox!
        Boss b = new Boss(200, 200, "Cirno", 150);
        Item item = new Item(300, 300, ItemType.POWER);

        entities.add(p);
        entities.add(f);
        entities.add(b);
        entities.add(item);

        System.out.println("\n--- Testing Iterative Update on List<GameObject> entities ---");
        System.out.println("Initial Item Y: " + item.getY());
        for (GameObject entity : entities) {
            entity.update(0.5f);
        }
        System.out.println("Item Y after entity.update(0.5s): " + item.getY());

        System.out.println("\n--- Testing Polymorphic Collision Information ---");

        // Test 1: Player at (100, 100) collides with Fairy at (100, 114)
        System.out.println("Simulating collision at (100, 100) (Fairy position):");
        if (p.getCoreHitbox().overlaps(f.getCoreHitbox())) {
            p.onCollision(f);
        }

        // Test 2: Move player to Boss at (208, 208)
        p.setX(208);
        p.setY(208);
        System.out.println("Moving player to (200, 200) (Boss position):");
        if (p.getCoreHitbox().overlaps(b.getCoreHitbox())) {
            p.onCollision(b);
        }

        // Test 3: Move player to Item position at (290, item.getY())
        p.setX(290);
        p.setY(item.getY());
        System.out.println("Moving player to (300, " + item.getY() + ") (Item current position):");
        if (p.getGrazeHitbox().overlaps(item.getCoreHitbox())) {
            p.onCollision(item);
        }

        System.out.println("\n=== Module 3 Test Completed Successfully ===");


        // ==========================================
        // MODULE 4: COLLECTIONS, GENERICS & ITERATOR SAFE REMOVAL
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 4: COLLECTIONS, GENERICS & ITERATORS ===");

        List<GameObject> gameEntities = new ArrayList<>();

        Player player4 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);
        Fairy targetFairy = new Fairy(200, 200, "Target Fairy", 20); // Placed at y=200, HP 20

        gameEntities.add(player4);
        gameEntities.add(targetFairy);

        System.out.println("\n--- Initial Entities in List<GameObject> ---");
        System.out.println("Entities Count: " + gameEntities.size());

        System.out.println("\n--- Turn 1: Player Fires Bullet ---");
        Bullet bullet1 = new Bullet(200, 150, 400f, BulletType.AMULET, 25);
        gameEntities.add(bullet1);

        System.out.println("Entities Count before update: " + gameEntities.size());

        System.out.println("\n--- Updating frame: Bullet moves upward & collides with Fairy ---");
        for (int i = 0; i < gameEntities.size(); i++) {
            for (int j = i + 1; j < gameEntities.size(); j++) {
                GameObject e1 = gameEntities.get(i);
                GameObject e2 = gameEntities.get(j);
                if (e1.getCoreHitbox().overlaps(e2.getCoreHitbox())) {
                    e1.onCollision(e2);
                    e2.onCollision(e1);
                }
            }
        }

        runner.updateAndClean(gameEntities, 0.125f, 640, 480);

        System.out.println("Entities Count after generic Iterator cleanup: " + gameEntities.size());

        System.out.println("\n=== Module 4 Test Completed Successfully ===");


        // ==========================================
        // MODULE 7: OBJECT POOL & MEDIATOR PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 7: OBJECT POOL & MEDIATOR ===");

        BulletManager bulletManager = new BulletManager();
        CollisionReferee referee = new CollisionReferee();

        Player player7 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);

        Fairy enemyFairy = new Fairy(200, 200, "Stage 1 Fairy", 20); // HP 20
        List<GameObject> m7Entities = new ArrayList<>();
        m7Entities.add(enemyFairy);

        System.out.println("\n--- Testing Object Pool ---");
        System.out.println("Initial Player Pool Size: " + bulletManager.getPlayerPoolSize());
        System.out.println("Initial Active Player Bullets: " + bulletManager.getActivePlayerBullets().size());

        // Direct Object Pool Shooting
        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 1st shoot: " + bulletManager.getActivePlayerBullets().size());

        // Update bullets movement (y=50+48 -> 98, moves upwards to y=200 over 0.25s)
        bulletManager.update(0.26f, 640, 480);

        System.out.println("\n--- Testing Mediator Pattern Collision Resolution ---");
        referee.resolveCollisions(player7, m7Entities, bulletManager);

        // Cleanup offscreen / destroyed bullets back into pool
        bulletManager.update(0.01f, 640, 480);

        System.out.println("\n--- Object Pool Recycling Check ---");
        System.out.println("Active Player Bullets after hit & recycling: " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Recycled Player Pool Size: " + bulletManager.getPlayerPoolSize());

        // Shooting again re-uses pooled bullet!
        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 2nd shoot (Re-used pooled instance): " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Player Pool Size after re-using: " + bulletManager.getPlayerPoolSize());

        System.out.println("\n--- Testing Graze vs Core Hitbox Detection ---");
        // Spawn 16x16 enemy bullet at (190, 50) -> Overlaps Graze Hitbox (x: 200..232, y: 50..98), misses Core Hitbox (x: 212..220, y: 70..78)
        Bullet enemyBullet = bulletManager.spawnEnemyBullet(190, 50, 0, 0, 15);
        long scoreBeforeGraze = player7.getScore();
        referee.resolveCollisions(player7, m7Entities, bulletManager);
        System.out.println("Graze score added (+50 pts): " + (player7.getScore() - scoreBeforeGraze == 50));
        System.out.println("Player Graze Counter: " + player7.getGrazeCount());

        System.out.println("\n=== Module 7 Test Completed Successfully ===");


        // ==========================================
        // MODULE 8: COMMAND PATTERN & OBSERVER PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 8: COMMAND & OBSERVER PATTERNS ===");

        Player player8 = new Player(200, 50, "Reimu Hakurei", 100, 15, 3);
        BulletManager bManager8 = new BulletManager();
        GameHUD hud = new GameHUD();

        System.out.println("\n--- Testing Observer Pattern Registration ---");
        player8.registerObserver(hud);
        System.out.println("HUD Received Initial Score: " + hud.getScore());
        System.out.println("HUD Received Initial HP:    " + hud.getHp());
        System.out.println("HUD Received Initial Lives: " + hud.getLives());
        System.out.println("HUD Received Initial Bombs: " + hud.getSpellCards());
        System.out.println("HUD Received Initial Graze: " + hud.getGrazeCount());

        System.out.println("\n--- Testing Command Pattern Execution ---");

        // 1. Move Command
        Command moveUp = new MoveCommand(0, 0.5f);
        moveUp.execute(player8, bManager8);
        System.out.println("Player position after MoveCommand: (" + player8.getX() + ", " + player8.getY() + ")");

        // 2. Focus Command
        Command focusOn = new FocusCommand(true);
        focusOn.execute(player8, bManager8);
        System.out.println("Player Focus Mode Enabled: " + player8.isFocused());

        // 3. Shoot Command
        Command shoot = new ShootCommand();
        shoot.execute(player8, bManager8);
        System.out.println("Active Player Bullets after ShootCommand: " + bManager8.getActivePlayerBullets().size());

        // 4. Bomb / Spell Card Command (Screen Clearing)
        bManager8.spawnEnemyBullet(200, 300, 0, -100, 15);
        bManager8.spawnEnemyBullet(250, 350, 0, -100, 15);
        System.out.println("Active Enemy Bullets before Bomb: " + bManager8.getActiveEnemyBullets().size());

        Command bomb = new BombCommand();
        bomb.execute(player8, bManager8);

        System.out.println("Active Enemy Bullets after BombCommand: " + bManager8.getActiveEnemyBullets().size());
        System.out.println("HUD Received Updated Bombs: " + hud.getSpellCards());

        System.out.println("\n--- Testing Observer Notification on Graze & Item Collection ---");
        player8.addGraze();
        System.out.println("HUD Received Updated Graze: " + hud.getGrazeCount());

        Item pItem = new Item(200, 50, ItemType.POWER);
        player8.collectItem(pItem);
        System.out.println("HUD Received Updated Score: " + hud.getScore());

        System.out.println("\n--- Testing Life Stock & Respawn System ---");
        System.out.println("Initial Lives: " + player8.getLives());
        player8.update(6.0f); // Expire bomb invulnerability timer (5.0s)
        player8.takeDamage(100, bManager8, null);
        System.out.println("Lives after fatal hit: " + player8.getLives() + " (Respawned with i-frames: " + player8.isInvulnerable() + ")");
        System.out.println("HUD Received Updated Lives: " + hud.getLives());
        Item lifeItem = new Item(200, 50, ItemType.LIFE);
        player8.collectItem(lifeItem);
        System.out.println("Lives after collecting LIFE item: " + player8.getLives());
        System.out.println("HUD Received Restored Lives: " + hud.getLives());

        System.out.println("\n--- Testing InputHandler (Bob Nystrom Invoker & Key Rebinding) ---");
        InputHandler inputHandler = new InputHandler();
        System.out.println("Default Shoot Key: " + inputHandler.getKeyShoot() + " (Z)");
        inputHandler.setKeyShoot(62); // Rebind to Space
        System.out.println("Rebound Shoot Key: " + inputHandler.getKeyShoot() + " (Space)");
        System.out.println("Zero-GC Commands Buffer Initialized: " + (inputHandler.handleInput(0.016f) != null));

        System.out.println("\n=== Module 8 Test Completed Successfully ===");
    }

    // Non-static (Instance) Generic Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                if (entity.isOffScreen(screenWidth, screenHeight)) {
                    System.out.println("Removed via Generic Iterator (off-screen): " + entity.getClass().getSimpleName() + " at y=" + entity.getY());
                } else {
                    System.out.println("Removed via Generic Iterator (destroyed): " + entity.getClass().getSimpleName());
                }
                iterator.remove(); // Safe removal using Iterator!
            }
        }
    }
}
