package edu.virginia.cs.runner;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BouncerObject extends RunnerObject {

	private int baseY;
	private int retractY;
	private float showTimer = 0.0f;
	private float showTime = 1.0f;
	private boolean retracting = false;
	private boolean showing = true;
	
	private float baseSpeed = 50f;
	private static final float CLOSE_DIST = 10.0f;
	
	public BouncerObject(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		mBodyDef.type = BodyType.KinematicBody;
		this.setTextureName("wall");
		baseY = y;
		retractY = y - height - 15;
	}
	
	public void setShowTime(float time) {this.showTime = time; }
	public void setBaseSpeed(float speed) {this.baseSpeed = speed; }
	public void multiplyWorldFactor(float ratioMultiplier) { 
		this.showTime /= ratioMultiplier; 
		this.baseSpeed *= ratioMultiplier;
	}
	
	public void randomize() { 
		this.baseSpeed = (float)Math.random() * 200f + 400f; 
		this.showTime = 0.5f + (float)Math.random() * 1.0f;
	}
	
	@Override
	public Rectangle getSourceRect()
	{
		return new Rectangle(0, 0, 32, 32);
	}

	@Override
	public void update(float dt)
	{
		if (showing)
			showTimer += dt;
		if (showing && showTimer > showTime)
		{
			if (Math.abs(this.getY() - baseY) < CLOSE_DIST)
			{
				this.mLinVelY = -1 * baseSpeed;
				this.mRenewSpeed = true;
				showTimer = 0.0f;
				retracting = true;
				showing = false;
			}
			else if (Math.abs(this.getY() - retractY) < CLOSE_DIST)
			{
				this.mLinVelY = 1 * baseSpeed;
				this.mRenewSpeed = true;
				showTimer = 0.0f;
				retracting = false;
				showing = false;
			}
		}
		if (!showing && retracting && Math.abs(this.getY() - retractY) < CLOSE_DIST)
		{
			this.mLinVelY = 0;
			this.mRenewSpeed = true;
			showing = true;
		}
		else if (!showing && !retracting && Math.abs(this.getY() - baseY) < CLOSE_DIST)
		{
			this.mLinVelY = 0;
			this.mRenewSpeed = true;
			showing = true;
		}
	}
	
}
