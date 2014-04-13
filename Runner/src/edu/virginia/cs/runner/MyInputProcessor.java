package edu.virginia.cs.runner;

import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
	
	private Input myInput;
	
	public MyInputProcessor(Input a){
		myInput = a;
	}

	@Override
	public boolean keyDown(int keycode) {
		// when a key was pressed
		myInput.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// when a key was released
		myInput.keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// Input.keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		myInput.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		myInput.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		myInput.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		myInput.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		myInput.scrolled(amount);
		return false;
	}

}
