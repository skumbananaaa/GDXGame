package com.pinnkeyy.gdx.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;
import com.pinnkeyy.gdx.sprites.Mario;

public class Mushroom extends Item
{
    public Mushroom(PlayScreen screen, float x, float y)
    {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0.0f);
    }

    @Override
    public void defineItem()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float boxR = 8.0f;
        shape.setAsBox(boxR / GDXGame.PPM, boxR / GDXGame.PPM);
        fdef.filter.categoryBits = GDXGame.ITEM_BIT;
        fdef.filter.maskBits =
                GDXGame.MARIO_BIT |
                GDXGame.OBJECT_BIT |
                GDXGame.GROUND_BIT |
                GDXGame.COIN_BIT |
                GDXGame.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario)
    {
        destroy();
    }

    @Override
    public void update(float dt)
    {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2.0f, body.getPosition().y - getHeight() / 2.0f);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }

}
