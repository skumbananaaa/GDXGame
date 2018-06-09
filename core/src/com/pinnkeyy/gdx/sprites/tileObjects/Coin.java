package com.pinnkeyy.gdx.sprites.tileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;
import com.pinnkeyy.gdx.sprites.items.ItemDefinition;
import com.pinnkeyy.gdx.sprites.items.Mushroom;

public class Coin extends InteractiveTile
{
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, Rectangle bounds)
    {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCatFilter(GDXGame.COIN_BIT);
    }

    @Override
    public void onHeadHit()
    {
        if (getCell().getTile().getId() == BLANK_COIN)
            GDXGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else
        {
            GDXGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
            screen.spawnItem(new ItemDefinition(new Vector2(body.getPosition().x, body.getPosition().y + 16.0f / GDXGame.PPM), Mushroom.class));
        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        PlayScreen.getCurrentHUD().addScore(100);
    }
}
