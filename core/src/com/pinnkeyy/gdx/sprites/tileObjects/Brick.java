package com.pinnkeyy.gdx.sprites.tileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;

public class Brick extends InteractiveTile
{
    public Brick(PlayScreen screen, Rectangle bounds)
    {
        super(screen, bounds);
        fixture.setUserData(this);
        setCatFilter(GDXGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit()
    {
        setCatFilter(GDXGame.DESTROYED_BIT);
        getCell().setTile(null);
        PlayScreen.getCurrentHUD().addScore(200);
        GDXGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
