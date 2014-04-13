package edu.virginia.cs.runner;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WheelObject extends RunnerObject {
	
	public WheelObject(int x, int y, int r)
	{
		super(x, y, r * 2, r * 2);
		mBodyDef.type = BodyType.KinematicBody;
		this.setTextureName("wheel");
	}	
	
	@Override
	public Rectangle getSourceRect()
	{
		return new Rectangle(0, 64, 256, 256);
	}
}
