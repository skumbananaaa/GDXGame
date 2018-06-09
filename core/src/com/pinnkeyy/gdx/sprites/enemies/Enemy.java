package com.pinnkeyy.gdx.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;

public abstract class Enemy extends Sprite
{
    protected World world;
    protected PlayScreen screen;
    public Body body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y)
    {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x / GDXGame.PPM, y / GDXGame.PPM);
        defineEnemy();
        velocity = new Vector2(1.0f, -2.0f);
        body.setActive(false);
    }

    public void reverseVelocity(boolean x, boolean y)
    {
        if (x) velocity.x = -velocity.x;
        if (y) velocity.y = -velocity.y;
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead();
}
