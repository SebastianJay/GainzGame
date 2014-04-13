package edu.virginia.cs.runner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RunnerGame extends Game implements ApplicationListener {
	
	AbstractScreen screen;	
	private Input input;
	
	@Override
	public void create() {	
		//
		Model.VIEWPORT_WIDTH = Gdx.graphics.getWidth();
		Model.VIEWPORT_HEIGHT = Gdx.graphics.getHeight();
		//
		input = new Input();
		RunnerGame.loadAssets();

		Texture.setEnforcePotImages(false);
		
		//createScreen(GameScreen.class);
		createScreen(SplashScreen.class);
	}

	@Override
	public void dispose() {
		super.dispose();
		//batch.dispose();
		//texture.dispose();
	}

	@Override
	public void render() {		
		Class<? extends AbstractScreen> newScreen = screen.checkScreenChange();
		if(newScreen != null) {
			createScreen(newScreen);
		}
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	private void createScreen(Class<? extends AbstractScreen> type)
	{
		screen = null;
		try {
			screen = type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		input.setListener(screen);
		setScreen(screen);
	}
	
	private static void loadAssets()
	{
		SingletonAssetManager m = SingletonAssetManager.getInstance();
		m.load("wheel", "data/Master.png", Texture.class);
		m.load("player", "data/Master.png", Texture.class);
		m.load("background", "data/sized_cave_819x512.png", Texture.class);
		m.load("background2", "data/Background3.png", Texture.class);
		m.load("background3", "data/Background4_modded.png", Texture.class);
		m.load("background4", "data/background5.png", Texture.class);
		m.load("wall", "data/black_square.png", Texture.class);
		m.load("spike", "data/Master.png", Texture.class);
		m.load("spike2", "data/Master.png", Texture.class);
		m.load("plus", "data/plus.png", Texture.class);
		m.load("pluspressed", "data/plusPressed.png", Texture.class);
		m.load("minus", "data/minus.png", Texture.class);
		m.load("minuspressed", "data/minusPressed.png", Texture.class);

		m.load("titlescreen", "data/Titlescreen.png", Texture.class);
		m.load("platform", "data/platform3.png", Texture.class);
		m.load("deathscreen", "data/red_square.png", Texture.class);
		
		m.finishLoading();
		//load images
	}
}
