package com.pinnkeyy.gdx.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;
import com.pinnkeyy.gdx.sprites.Mario;

public abstract class Item extends Sprite
{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16.0f / GDXGame.PPM, 16.0f / GDXGame.PPM);
        toDestroy = false;
        destroyed = false;

        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Mario mario);

    public void destroy()
    {
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y)
    {
        if (x) velocity.x = -velocity.x;
        if (y) velocity.y = -velocity.y;
    }

    public void update(float dt)
    {
        if (toDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    @Override
    public void draw(Batch batch)
    {
        if (!destroyed)
            super.draw(batch);
    }
}
