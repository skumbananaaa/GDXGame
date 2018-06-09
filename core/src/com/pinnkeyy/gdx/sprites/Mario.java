package com.pinnkeyy.gdx.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;

public class Mario extends Sprite
{
    public enum State
    {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING
    };

    public State currentState;
    public State prevState;

    public World world;
    public Body body;

    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;

    public Mario(PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0.0f;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++) frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++) frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        marioJump = new Animation<TextureRegion>(0.1f, frames);


        defineMario();

        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / GDXGame.PPM, 16 / GDXGame.PPM);
        setRegion(marioStand);
    }

    private void defineMario()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32.0f / GDXGame.PPM, 32.0f / GDXGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float boxR = 8.0f;
        shape.setAsBox(boxR / GDXGame.PPM, boxR / GDXGame.PPM);
        fdef.filter.categoryBits = GDXGame.MARIO_BIT;
        fdef.filter.maskBits =
                GDXGame.GROUND_BIT |
                GDXGame.BRICK_BIT |
                GDXGame.COIN_BIT |
                GDXGame.ENEMY_BIT |
                GDXGame.OBJECT_BIT |
                GDXGame.ENEMY_HEAD_BIT |
                GDXGame.ITEM_BIT;
        fdef.shape = shape;
        body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-3.0f / GDXGame.PPM, boxR / GDXGame.PPM), new Vector2(3.0f / GDXGame.PPM, boxR / GDXGame.PPM));

        fdef.shape = head;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("head");
    }

    public State getState()
    {
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && prevState == State.JUMPING))
            return State.JUMPING;
        else if (body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public TextureRegion getFrame(float dt)
    {
        currentState = getState();

        TextureRegion region;

        switch (currentState)
        {
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX())
        {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == prevState ? stateTimer + dt : 0;
        prevState = currentState;
        return region;
    }

    public void update(float dt)
    {
        setPosition(body.getPosition().x - getWidth() / 2.0f, body.getPosition().y - getHeight() / 2.0f - 1.5f / GDXGame.PPM);
        setRegion(getFrame(dt));
    }
}
