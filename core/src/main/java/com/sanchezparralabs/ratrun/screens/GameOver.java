/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.sanchezparralabs.ratrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.sanchezparralabs.ratrun.Invaders;

/** The game over screen displays the final score and a game over text and waits for the user to touch the screen in which case it
 * will signal that it is done to the orchestrating GdxInvaders class.
 *
 * @author mzechner */
public class GameOver extends InvadersScreen {
	/** the SpriteBatch used to draw the background, logo and text **/
	private final SpriteBatch spriteBatch;
	/** the background texture **/
	private final Texture background;
	/** the logo texture **/
	private final Texture logo;
	/** the font **/
	private final BitmapFont font;
	/** is done flag **/
	private boolean isDone = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();

	private final GlyphLayout glyphLayout = new GlyphLayout();

	public GameOver (Invaders invaders) {
		super(invaders);
		spriteBatch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("data/planet.jpg"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		logo = new Texture(Gdx.files.internal("data/title.png"));
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		font = new BitmapFont(Gdx.files.internal("data/font16.fnt"), Gdx.files.internal("data/font16.png"), false);

		if (invaders.getController() != null) {
			invaders.getController().addListener(new ControllerAdapter() {
				@Override
				public boolean buttonUp(Controller controller,
						int buttonIndex) {
					controller.removeListener(this);
					isDone = true;
					return false;
				}
			});
		}
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		background.dispose();
		logo.dispose();
		font.dispose();
	}

	@Override
	public boolean isDone () {
		return isDone;
	}

	@Override
	public void draw (float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewMatrix.setToOrtho2D(0, 0, 640, 480);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(background, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spriteBatch.enableBlending();
		spriteBatch.draw(logo, 0, 320 - 128, 480, 128, 0, 256, 512, 256, false, false);
		glyphLayout.setText(font, "It is the end my friend.\nTouch to continue!",
				Color.WHITE, 480, Align.center, false);
		spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		font.draw(spriteBatch, glyphLayout, 0, 160 + glyphLayout.height / 2);
		spriteBatch.end();
	}

	@Override
	public void update (float delta) {
		if (Gdx.input.justTouched()) {
			isDone = true;
		}
	}
}
