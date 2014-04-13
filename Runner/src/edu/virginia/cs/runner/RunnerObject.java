package edu.virginia.cs.runner;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class RunnerObject {

	protected int mX, mY, mWidth, mHeight;
	protected String mTextureName;
	protected float mRotation;
	protected BodyDef mBodyDef;
	protected float mLinVelX, mLinVelY;
	protected float mAngularVelocity;
	protected boolean mRenewSpeed = false;
	
	public RunnerObject()
	{
		mBodyDef = new BodyDef();
		mBodyDef.type = BodyType.DynamicBody;
	}
	
	public RunnerObject(int x, int y, int width, int height)
	{
		this();
		this.mX = x;
		this.mY = y;
		this.mWidth = width;
		this.mHeight = height;
		mBodyDef.position.set(x + width / 2, y + height / 2);
	}
	
	public Point getPosition() { return new Point(mX, mY); }
	public int getX() { return mX; }
	public int getY() { return mY; }
	public int getWidth() { return mWidth; }
	public int getHeight() { return mHeight; }
	public String getTextureName() { return mTextureName; }
	public float getRotation() { return this.mRotation; }
	public Rectangle getSourceRect() { return new Rectangle(this.mX, this.mY, this.mWidth, this.mHeight); }
	public BodyDef getBodyDefinition() { return this.mBodyDef; }
	public Vector2 getLinearVelocity() { return new Vector2(this.mLinVelX, this.mLinVelY); }
	public boolean needsSpeedRefreshed() { return this.mRenewSpeed; }
	public float getAngularVelocity() { return mAngularVelocity; }
	
	public void setX(int x) { this.mX = x; }
	public void setY(int y) { this.mY = y;}
	public void setWidth(int width) { this.mWidth = width;}
	public void setHeight(int height) { this.mHeight = height; }
	public void setTextureName(String name) { this.mTextureName = name; }
	public void setRotation(float rotation) { this.mRotation = rotation; }
	public void setSpeedUpdated() { this.mRenewSpeed = false; }
	public void setAngularVelocity(float angVel) {	this.mAngularVelocity = angVel; }
	
	public void multiplySpeed(float ratioMultiplier) 
	{
		this.mLinVelX *= ratioMultiplier;
		this.mLinVelY *= ratioMultiplier;
		this.mRenewSpeed = true;
	}
	
	public void update(float dt)
	{
	}	
}
