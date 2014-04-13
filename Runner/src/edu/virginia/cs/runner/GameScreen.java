package edu.virginia.cs.runner;

import com.badlogic.gdx.Gdx;

public class GameScreen extends AbstractScreen {

	private ViewRenderer viewer;
	private Model model;
	
	public GameScreen() {
		SingletonAssetManager.getInstance().finishLoading();
		viewer = new ViewRenderer();
		model = new Model();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewer.resize(width, height);
	}
		
	@Override
	public void render(float delta) {
		super.render(delta);
		if (model.isDead && model.timeDead > 1.15f)
		{
			this.changeScreen(GameScreen.class);
		}
		else
		{
			model.update(delta);
			viewer.centerCameraOn(new Point(model.getPlayer().getX(), 0));
			viewer.render(model);
		}
	}
	
	@Override
	public void keyUp(int keyCode) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		int xOffset = (Gdx.graphics.getWidth() - Model.VIEWPORT_WIDTH) / 2;
		int yOffset = (Gdx.graphics.getHeight() - Model.VIEWPORT_HEIGHT) / 2;
		int adjustedTouchX = screenX - xOffset;
		int adjustedTouchY = screenY - yOffset;
		if (adjustedTouchX > 0 && adjustedTouchX < Model.VIEWPORT_WIDTH && 
				adjustedTouchY > 0 && adjustedTouchY < Model.VIEWPORT_HEIGHT)
		{
			if (adjustedTouchX > Model.VIEWPORT_WIDTH - Model.BUTTON_SIZE
					&& adjustedTouchX < Model.VIEWPORT_WIDTH
					&& adjustedTouchY > 0
					&& adjustedTouchY < Model.BUTTON_SIZE)
			{
				//hit plus
				model.increaseSpeed = true;
			}
			
			if (adjustedTouchX > Model.VIEWPORT_WIDTH - Model.BUTTON_SIZE
					&& adjustedTouchX < Model.VIEWPORT_WIDTH
					&& adjustedTouchY > Model.VIEWPORT_HEIGHT - Model.BUTTON_SIZE
					&& adjustedTouchY < Model.VIEWPORT_HEIGHT)
			{
				//hit minus
				model.decreaseSpeed = true;
			}
		}
			
	}
	
	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button, boolean dragging) {
		int xOffset = (Gdx.graphics.getWidth() - Model.VIEWPORT_WIDTH) / 2;
		int yOffset = (Gdx.graphics.getHeight() - Model.VIEWPORT_HEIGHT) / 2;
		int adjustedTouchX = screenX - xOffset;
		int adjustedTouchY = screenY - yOffset;
		if (adjustedTouchX > 0 && adjustedTouchX < Model.VIEWPORT_WIDTH && 
				adjustedTouchY > 0 && adjustedTouchY < Model.VIEWPORT_HEIGHT)
		{
			//if (adjustedTouchX > Model.VIEWPORT_WIDTH - Model.BUTTON_SIZE
			//		&& adjustedTouchX < Model.VIEWPORT_WIDTH
			//		&& adjustedTouchY > 0
			//		&& adjustedTouchY < Model.BUTTON_SIZE)
			{
				//unhit plus
				model.increaseSpeed = false;
			}
			
			//if (adjustedTouchX > Model.VIEWPORT_WIDTH - Model.BUTTON_SIZE
			//		&& adjustedTouchX < Model.VIEWPORT_WIDTH
			//		&& adjustedTouchY > Model.VIEWPORT_HEIGHT - Model.BUTTON_SIZE
			//		&& adjustedTouchY < Model.VIEWPORT_HEIGHT)
			{
				//unhit minus
				model.decreaseSpeed = false;
			}
		}
			
	}
}
