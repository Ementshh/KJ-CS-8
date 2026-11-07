package com.netlab.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;
    protected TextureRegion sprite;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;
    protected boolean active = true;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    // Render with SpriteBatch (respects animation PlayMode - LOOP vs NORMAL)
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (animation != null) {
                boolean looping = (animation.getPlayMode() == Animation.PlayMode.LOOP);
                TextureRegion currentFrame = animation.getKeyFrame(stateTime, looping);
                batch.draw(currentFrame, x, y, width, height);
            } else if (sprite != null) {
                batch.draw(sprite, x, y, width, height);
            }
        }
    }

    // Render fallback with ShapeRenderer (for geometric hitboxes)
    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null && color != null && active) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    // Core Hitbox: Centered smaller death hurtbox inside the object
    @Override
    public Rectangle getCoreHitbox() {
        float cw = width * 0.5f;
        float ch = height * 0.5f;
        return new Rectangle(x + (width - cw) / 2f, y + (height - ch) / 2f, cw, ch);
    }

    // Graze Hitbox: Full sprite dimensions for close-call detection
    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void onCollision(Collidable other) {
        // Base collision handler
    }

    public boolean isDestroyed() {
        return !active;
    }

    public void destroy() {
        this.active = false;
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        return (x < -50 || x > screenWidth + 50 || y < -50 || y > screenHeight + 50);
    }

    // Encapsulation: Getters and Setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) {
        if (width > 0) this.width = width;
    }

    public float getHeight() { return height; }
    public void setHeight(float height) {
        if (height > 0) this.height = height;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) {
        if (speed >= 0) this.speed = speed;
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public TextureRegion getSprite() { return sprite; }
    public void setSprite(TextureRegion sprite) { this.sprite = sprite; }

    public Animation<TextureRegion> getAnimation() { return animation; }
    public void setAnimation(Animation<TextureRegion> animation) {
        if (this.animation != animation) {
            this.animation = animation;
            this.stateTime = 0f; // Reset frame timer when changing animation
        }
    }

    public boolean isActive() { return active; }
}
