package com.pinnkeyy.gdx.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.sprites.Mario;
import com.pinnkeyy.gdx.sprites.enemies.Enemy;
import com.pinnkeyy.gdx.sprites.items.Item;
import com.pinnkeyy.gdx.sprites.tileObjects.InteractiveTile;

public class WorldContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head")
        {
            if (fixB.getUserData() instanceof InteractiveTile)
            {
                ((InteractiveTile)fixB.getUserData()).onHeadHit();
            }
        }
        else if (fixB.getUserData() == "head")
        {
            if (fixA.getUserData() instanceof InteractiveTile)
            {
                ((InteractiveTile)fixA.getUserData()).onHeadHit();
            }
        }

        switch (cDef)
        {
            case GDXGame.ENEMY_HEAD_BIT | GDXGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == GDXGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case GDXGame.ENEMY_BIT | GDXGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == GDXGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case GDXGame.ENEMY_BIT | GDXGame.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == GDXGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case GDXGame.MARIO_BIT | GDXGame.ENEMY_BIT:
                Gdx.app.log("Mario", "Died");
                break;
            case GDXGame.ENEMY_BIT | GDXGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case GDXGame.ITEM_BIT | GDXGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == GDXGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case GDXGame.ITEM_BIT | GDXGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == GDXGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario)fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario)fixA.getUserData());
                break;

        }
    }

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
