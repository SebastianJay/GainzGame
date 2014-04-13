package edu.virginia.cs.runner;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ViewRenderer {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private Rectangle viewport;
	
	public ViewRenderer()
	{
		camera = new OrthographicCamera(Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
		camera.zoom = 1.0f;
		camera.update();
		batch = new SpriteBatch();
		font = new BitmapFont();
		viewport = new Rectangle(0, 0, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
	}
		
	public void resize(int width, int height) {
		//camera.setToOrtho(false, width, height);
		//camera.viewportWidth = width;
		//camera.viewportHeight = height;
		
		/*
		float desiredRatio = Model.VIEWPORT_WIDTH * 1.0f / Model.VIEWPORT_WIDTH;
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);

        if(aspectRatio > desiredRatio) {
            scale = (float) height / (float) Model.VIEWPORT_WIDTH;
            crop.x = (width - Model.VIEWPORT_WIDTH * scale) / 2f;
        } else if(aspectRatio < desiredRatio) {
            scale = (float) width / (float) Model.VIEWPORT_WIDTH;
            crop.y = (height - Model.VIEWPORT_HEIGHT * scale) / 2f;
        } else {
            scale = (float) width / (float) Model.VIEWPORT_WIDTH;
        }

        float w = (float) Model.VIEWPORT_WIDTH * scale;
        float h = (float) Model.VIEWPORT_HEIGHT * scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
        */
		
		camera.update();
	}
	
	public void centerCameraOn(Point p) {
        Vector3 delta = new Vector3((p.x - camera.position.x) * camera.zoom, (p.y - camera.position.y) * camera.zoom, 0);
        camera.translate(delta);
        camera.update();
	}
	
	public void render(Model model)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glViewport((int)viewport.x, (int)viewport.y, (int)viewport.width, (int)viewport.height);
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		//Gdx.app.log("Dim", screenWidth + " " + screenHeight);
		int xOffset = (screenWidth - Model.VIEWPORT_WIDTH) / 2;
		int yOffset = (screenHeight - Model.VIEWPORT_HEIGHT) / 2;
		Gdx.gl.glViewport(xOffset, yOffset, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
		 xOffset = 0;
		 yOffset = 0;
		
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.setColor(Color.WHITE);
		
		//draw background first
		Texture bg = SingletonAssetManager.getInstance().get(model.backgroundChoice);
		batch.draw(bg, xOffset + ((model.blockIndex - 1) * Model.VIEWPORT_WIDTH) - Model.VIEWPORT_WIDTH / 2, 
				yOffset + -Model.VIEWPORT_HEIGHT / 2, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT, 
				0, 0, 819, 512, false, false);
		batch.draw(bg, xOffset + (model.blockIndex * Model.VIEWPORT_WIDTH) - Model.VIEWPORT_WIDTH / 2, 
				yOffset + -Model.VIEWPORT_HEIGHT / 2, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT, 
				0, 0, 819, 512, false, false);
		batch.draw(bg, xOffset + ((model.blockIndex + 1) * Model.VIEWPORT_WIDTH) -Model.VIEWPORT_WIDTH / 2, 
				yOffset + -Model.VIEWPORT_HEIGHT / 2, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT, 
				0, 0, 819, 512, false, false);
		
		//draw scene objects
		List<RunnerObject> list = model.getObjectsInView();
		for (RunnerObject obj : list)
		{
			Texture tex = SingletonAssetManager.getInstance().get(obj.getTextureName());
			Rectangle sourceRect = obj.getSourceRect();
			
			batch.draw(tex, xOffset + obj.getX(), yOffset + obj.getY(), obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(), 1.0f, 1.0f, obj.getRotation(),
					(int)sourceRect.x, (int)sourceRect.y, (int)sourceRect.width, (int)sourceRect.height, false, false);
			
		}
		
		//draw text
		font.draw(batch, "Current Distance: "+model.recordDistance+" cubits", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
				yOffset + Model.VIEWPORT_HEIGHT / 2 - 15);
		font.draw(batch, "Best Distance: "+Model.bestDistanceOfAll+" cubits", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
				yOffset + Model.VIEWPORT_HEIGHT / 2 - 30);
		if (model.worldSpeedFactor == Model.MAX_WORLD_SPEED)
			font.draw(batch, "World speed is max.", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
					yOffset + Model.VIEWPORT_HEIGHT / 2 - 45);
		else if (model.worldSpeedFactor == Model.MIN_WORLD_SPEED)
			font.draw(batch, "World speed is min.", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
					yOffset + Model.VIEWPORT_HEIGHT / 2 - 45);
		if (Model.showTutorial)
		{
			font.draw(batch, "Adjust the speed of obstacles using the plus and minus buttons.", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
					yOffset + Model.VIEWPORT_HEIGHT / 2 - 60);			
			font.draw(batch, "Avoid spikes, and do not stay idle for too long! Get as far as you can.", xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2 + 20, 
					yOffset + Model.VIEWPORT_HEIGHT / 2 - 75);			
		}
		
		//font.draw(batch, "Speed: "+model.recordSpeed, model.getPlayer().getX() - 30, Model.VIEWPORT_HEIGHT / 2 - 15);
		//font.draw(batch, "Time: "+model.recordTime, model.getPlayer().getX() - 30, Model.VIEWPORT_HEIGHT / 2 - 30);
		//font.draw(batch, "Best max speed time: "+model.bestFastTime, model.getPlayer().getX() - 30, Model.VIEWPORT_HEIGHT / 2 - 45);
		
		//draw HUD buttons
		String texButton1 = model.increaseSpeed ? "pluspressed" : "plus";
		String texButton2 = model.decreaseSpeed ? "minuspressed" : "minus";
		batch.draw((Texture)SingletonAssetManager.getInstance().get(texButton1),
				xOffset + model.getPlayer().getX() + Model.VIEWPORT_WIDTH / 2 - Model.BUTTON_SIZE,
				Model.VIEWPORT_HEIGHT / 2 - Model.BUTTON_SIZE, Model.BUTTON_SIZE, Model.BUTTON_SIZE);
		batch.draw((Texture)SingletonAssetManager.getInstance().get(texButton2),
				xOffset + model.getPlayer().getX() + Model.VIEWPORT_WIDTH / 2 - Model.BUTTON_SIZE,
				-Model.VIEWPORT_HEIGHT / 2, Model.BUTTON_SIZE, Model.BUTTON_SIZE);
		
		//draw "red screen of death"
		if (model.isDead)
		{
			float alpha = model.timeDead <= 1.0f ? model.timeDead : 1.0f;
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			batch.draw((Texture)SingletonAssetManager.getInstance().get("deathscreen"), 
					xOffset + model.getPlayer().getX() - Model.VIEWPORT_WIDTH / 2, 
					yOffset + - Model.VIEWPORT_HEIGHT / 2, 
					Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
		}
		
		batch.end();
	}
}
