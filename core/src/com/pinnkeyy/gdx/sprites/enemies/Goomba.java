package com.pinnkeyy.gdx.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;

public class Goomba extends Enemy
{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y)
    {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));

        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0.0f;

        setBounds(getX(), getY(), 16.0f / GDXGame.PPM, 16.0f / GDXGame.PPM);

        setToDestroy = false;
        destroyed = false;
    }

    @Override
    protected void defineEnemy()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float boxR = 8.0f;
        shape.setAsBox(boxR / GDXGame.PPM, boxR / GDXGame.PPM);
        fdef.filter.categoryBits = GDXGame.ENEMY_BIT;
        fdef.filter.maskBits =
                GDXGame.GROUND_BIT |
                GDXGame.BRICK_BIT |
                GDXGame.COIN_BIT |
                GDXGame.ENEMY_BIT |
                GDXGame.MARIO_BIT |
                GDXGame.OBJECT_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-7.9f, 9.0f).scl(1.0f / GDXGame.PPM);
        vertices[1] = new Vector2(7.9f, 9.0f).scl(1.0f / GDXGame.PPM);
        vertices[2] = new Vector2(-3.0f, 3.0f).scl(1.0f / GDXGame.PPM);
        vertices[3] = new Vector2(3.0f, 3.0f).scl(1.0f / GDXGame.PPM);
        head.set(vertices);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = GDXGame.ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnHead()
    {
        setToDestroy = true;
    }

    @Override
    public void update(float dt)
    {
        stateTime += dt;

        if (setToDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0.0f;
        }
        else if (!destroyed)
        {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2.0f, body.getPosition().y - getHeight() / 2.0f);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    public void draw(Batch batch)
    {
        if (!destroyed || stateTime < 1.0f)
            super.draw(batch);
    }
}
