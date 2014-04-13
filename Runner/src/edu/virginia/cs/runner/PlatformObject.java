package edu.virginia.cs.runner;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlatformObject extends RunnerObject {

	private static final float CLOSE_DIST = 5.0f;
	private Point startPos, endPos;
	private boolean headingToEnd = false;
	private float baseSpeed = 1.0f;
	
	public PlatformObject(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		mBodyDef.type = BodyType.KinematicBody;
		mTextureName = "wall";
	}
	
	public void setTargetLocations(int startX, int startY, int endX, int endY)
	{
		startPos = new Point(startX, startY);
		endPos = new Point(endX, endY);
	}
	
	public void setBaseSpeedMultiplier(float mult)
	{
		baseSpeed = mult;
	}
	
	public void update(float dt)
	{
		if (headingToEnd && Math.abs(endPos.x - this.getX()) < CLOSE_DIST
				&& Math.abs(endPos.y - this.getY()) < CLOSE_DIST)
		{
			float mag = (float)Math.sqrt((endPos.x - startPos.x) * (endPos.x - startPos.x) + 
					(endPos.y - startPos.y) * (endPos.y - startPos.y));
			this.mLinVelX = (float)(startPos.x - endPos.x) / mag * baseSpeed;
			this.mLinVelY = (float)(startPos.y - endPos.y) / mag * baseSpeed;
			this.mRenewSpeed = true;
			headingToEnd = false;
		}
		else if (!headingToEnd && Math.abs(startPos.x - this.getX()) < CLOSE_DIST
				&& Math.abs(startPos.y - this.getY()) < CLOSE_DIST)
		{
			float mag = (float)Math.sqrt((endPos.x - startPos.x) * (endPos.x - startPos.x) + 
					(endPos.y - startPos.y) * (endPos.y - startPos.y));
			this.mLinVelX = (endPos.x - startPos.x) / mag * baseSpeed;
			this.mLinVelY = (endPos.y - startPos.y) / mag * baseSpeed;
			this.mRenewSpeed = true;
			headingToEnd = true;
		}
	}
	
}
