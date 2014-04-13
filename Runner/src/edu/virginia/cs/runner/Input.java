package edu.virginia.cs.runner;

import com.badlogic.gdx.Gdx;

public class Input {
	
	private InputListener listener;
	private MyInputProcessor a;
	
	private boolean dragging = false;
	
	private int prevX;
	private int prevY;
	
	
	public Input(){
		a = new MyInputProcessor(this);
		Gdx.input.setInputProcessor(a);
	}

	public void setListener(InputListener listener) {
		this.listener = listener;
	}
	
	public void keyDown(int keyCode){
		
		Gdx.app.log("Runner", "A key was pressed, its keycode was "+keyCode);
		
		if(listener != null) {
			listener.keyDown(keyCode);
		}
	}
	
	public void keyUp(int keyCode){
		Gdx.app.log("Runner", "A key was released, its keycode was "+keyCode);

		if(listener != null) {
			listener.keyUp(keyCode);
		}
	}
	
	public void touchDown(int screenX, int screenY, int pointer, int button){
		Gdx.app.log("Runner", "The mouse was pressed at x-coord: "+screenX);
		Gdx.app.log("Runner", "The mouse was pressed at y-coord: "+screenY);

		if(listener != null) {
			listener.touchDown(screenX, screenY, pointer, button);
		}
	}
	
	public void touchUp(int screenX, int screenY, int pointer, int button){
		Gdx.app.log("Runner", "The mouse was realeased at x-coord: "+screenX);
		Gdx.app.log("Runner", "The mouse was realeased at y-coord: "+screenY);

		if(listener != null) {
			listener.touchUp(screenX, screenY, pointer, button, dragging);
		}
		
		dragging = false;
	}
	
	public void mouseMoved(int screenX, int screenY) {
//		System.out.println("The mouse was moved: "+screenX+", "+screenY);

		int deltaX = screenX - prevX;
		int deltaY = screenY - prevY;
		
		prevX = screenX;
		prevY = screenY;

		if(listener != null) {
			listener.mouseMoved(screenX, screenY, deltaX, deltaY);
		}
	}
	
	public void scrolled(int amount) {
//		System.out.println("Mouse wheel scroll amount: "+amount);

		if(listener != null) {
			listener.scrolled(amount);
		}
	}

	public void touchDragged(int screenX, int screenY, int pointer) {
//		System.out.println("The mouse was dragged: "+screenX+", "+screenY);

		int deltaX = screenX - prevX;
		int deltaY = screenY - prevY;
		
		prevX = screenX;
		prevY = screenY;
		
		dragging = true;
		
		if(listener != null) {
			listener.touchDragged(screenX, screenY, pointer, deltaX, deltaY);
		}
	}
}
