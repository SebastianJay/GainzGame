package edu.virginia.cs.runner;

import com.badlogic.gdx.math.Rectangle;

public class PlayerObject extends RunnerObject {

	protected int mTexWidth, mTexHeight;
	protected int mTexIndex = 1;
	private boolean animatingRight = true;
	protected float mAnimationTimer = 0.0f;
	private static final float ANIMATE_TIME = 0.08f;
	private static final float SPEED_X = 50.0f;
	
	public PlayerObject()
	{
		this(0, 0);
	}
	
	public PlayerObject(int x, int y)
	{
		super(x, y, 32, 32);
		this.setTextureName("player");
		mTexIndex = 1;
		mTexWidth = 64;
		mTexHeight = 64;
		mLinVelX = SPEED_X;
		mLinVelY = 0.0f;
	}
	
	@Override
	public void update(float dt)
	{
		mAnimationTimer += dt;
		if (mAnimationTimer > ANIMATE_TIME)
		{
			mAnimationTimer -= ANIMATE_TIME;
			if (animatingRight)
			{
				mTexIndex++;
				if (mTexIndex > 8)
				{
					mTexIndex = 7;
					animatingRight = false;
				}
			}
			else
			{
				mTexIndex--;
				if (mTexIndex < 1)
				{
					mTexIndex = 2;
					animatingRight = true;
				}
			}
		}
		
	}
	
	@Override
	public Rectangle getSourceRect()
	{
		return new Rectangle(mTexWidth * mTexIndex, 0, mTexWidth, mTexHeight);
	}
}
