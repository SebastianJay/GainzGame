package edu.virginia.cs.runner;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class SplashScreen extends AbstractScreen {
	private Image splashImage;
	private SpriteBatch batch;
	private Rectangle viewport;
	
	private float fadeTimer = 0.0f;
	private static final float FADE_TIME = 1.0f;
	private boolean fadingIn = true;
	private boolean fadingOut = false;
	
    public SplashScreen() {
        super();
        batch = new SpriteBatch();
        viewport = new Rectangle(0, 0, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
    }

    @Override
    public void show() {

        super.show();

        /*
        // load the splash image and create the texture region
        SingletonAssetManager.getInstance().finishLoading();
        Texture splashTexture = SingletonAssetManager.getInstance().get("titlescreen");
        TextureRegion tr = new TextureRegion(splashTexture);
        Drawable splashDrawable = new TextureRegionDrawable(tr);

        splashImage = new Image(splashDrawable, Scaling.stretch);
        splashImage.setFillParent(true);

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.getColor().a = 0f;

        // configure the fade-in/out effect on the splash image

        splashImage.addAction(sequence(fadeIn(0.75f), delay(1.75f),
                fadeOut(0.75f), new Action() {

                    @Override
                    public boolean act(float delta) { // the last action will
                                                        // move to the next
                                                        // screen
                        changeScreen(GameScreen.class);
                        return true;
                    }
                }));

        // and finally we add the actor to the stage
        stage.addActor(splashImage);
        */
    }
    
	@Override
	public void render(float delta) {
		super.render(delta);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int xOffset = (screenWidth - Model.VIEWPORT_WIDTH) / 2;
		int yOffset = (screenHeight - Model.VIEWPORT_HEIGHT) / 2;
		Gdx.gl.glViewport(xOffset, yOffset, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);

		batch.begin();
		batch.setColor(Color.WHITE);
				
		Texture title = SingletonAssetManager.getInstance().get("titlescreen");
		
		if (fadingIn)
		{
			fadeTimer += delta;
			float alpha = fadeTimer / FADE_TIME;
			if (alpha > 1.0f) alpha = 1.0f;
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			Texture screen = SingletonAssetManager.getInstance().get("wall");
			batch.draw(screen, 0, 0, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
			
			if (fadeTimer >= FADE_TIME)
			{
				fadeTimer = 0.0f;
				fadingIn = false;
			}
		}
		if (fadingOut)
		{
			fadeTimer += delta;
			float alpha = 1.0f - fadeTimer / FADE_TIME;
			if (alpha < 0.0f) alpha = 0.0f;
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			Texture screen = SingletonAssetManager.getInstance().get("wall");
			batch.draw(screen, 0, 0, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
			
			if (fadeTimer >= FADE_TIME)
			{
				fadeTimer = 0.0f;
				fadingOut = false;
				changeScreen(GameScreen.class);
			}
		}
		
		batch.draw(title, 0, 0, Model.VIEWPORT_WIDTH, Model.VIEWPORT_HEIGHT);
		batch.end();
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
			if (!fadingIn)
				fadingOut = true;
		}
	}
}