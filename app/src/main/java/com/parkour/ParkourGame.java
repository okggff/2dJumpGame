package com.parkour;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * Root ApplicationAdapter. Owns the screen lifecycle and top-level render loop.
 * Currently renders a blank background — game screens will be wired in Task 6.
 */
public class ParkourGame extends ApplicationAdapter {

    @Override
    public void create() {
        Gdx.app.log("ParkourGame", "Game started — Java " + System.getProperty("java.version"));
    }

    @Override
    public void render() {
        // Dark slate background
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("ParkourGame", "Resize: " + width + "x" + height);
    }

    @Override
    public void dispose() {
        Gdx.app.log("ParkourGame", "Disposing resources");
    }
}
