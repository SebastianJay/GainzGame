package edu.virginia.cs.runner;

public interface InputListener {

	void keyDown(int keyCode);
	
	void keyUp(int keyCode);
	
	void touchDown(int screenX, int screenY, int pointer, int button);
	
	void touchUp(int screenX, int screenY, int pointer, int button, boolean dragging);
	
	void mouseMoved(int screenX, int screenY, int deltaX, int deltaY);
	
	void scrolled(int amount);
	
	void touchDragged(int screenX, int screenY, int pointer, int deltaX, int deltaY);
}
