package edu.virginia.cs.runner;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WallObject extends RunnerObject {
	
	public WallObject(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		mBodyDef.type = BodyType.StaticBody;
		this.setTextureName("wall");
	}	
	
}
