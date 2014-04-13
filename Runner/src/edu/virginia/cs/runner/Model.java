package edu.virginia.cs.runner;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Model {

	public static int VIEWPORT_WIDTH = 1000;
	public static int VIEWPORT_HEIGHT = 600;
	public static int BUTTON_SIZE = 200;
	
	private List<RunnerObject> sceneObjects;
	private PlayerObject playerObject;
	private static final float GRAVITY_CONST = 100.0f;
	private static final float BUTTON_MULTIPLIER_CONST = 1.0275f;
	public static final float MAX_WORLD_SPEED = 7.5f;
	public static final float MIN_WORLD_SPEED = 0.01f;
	private static final float IDLE_DIE_TIME = 3.5f;
	private static final int BASE_ELEVATION = -50;
	private World physicsWorld;
	private int lastElevation;
	
	public float recordSpeed = 0.0f;
	public float recordTime = 0.0f;
	public float bestFastTime = 0.0f;
	public int recordDistance = 0;
	private float fastCounter = 0.0f;
	private float stoppedCounter = 0.0f;
	public static int bestDistanceOfAll = 0;

	public static boolean showTutorial = true;
	public static float tutorialTimer = 0.0f;
	private static final float TUTORIAL_TIME = 20.0f;
	
	public float worldSpeedFactor = 1.0f;
	
	public int blockIndex = 0;
	public boolean isDead = false;
	public float timeDead = 0.0f;
	public boolean increaseSpeed = false;
	public boolean decreaseSpeed = false;
	public String backgroundChoice = "";
	
	public Model()
	{
		sceneObjects = new ArrayList<RunnerObject>();
		playerObject = new PlayerObject(0, 0);
		physicsWorld = new World(new Vector2(0, -GRAVITY_CONST), true);
		//World.setVelocityThreshold(1e8f);
		switch((int)(Math.random() * 4))
		{
		case 0: backgroundChoice = "background"; break;
		case 1: backgroundChoice = "background2"; break;
		case 2: backgroundChoice = "background3"; break;
		case 3: backgroundChoice = "background4"; break;
		}
		loadScene();
	}
	
	public List<RunnerObject> getObjectsInView()
	{
		List<RunnerObject> list = new ArrayList<RunnerObject>();
		for(RunnerObject obj : sceneObjects)
		{
			if (obj.getX() + obj.getWidth() > playerObject.getX() - VIEWPORT_WIDTH / 2
				|| obj.getX() < playerObject.getX() + VIEWPORT_WIDTH / 2)
			{
				list.add(obj);
			}
		}
		list.add(playerObject);
		return list;
	}
	
	public PlayerObject getPlayer()
	{
		return playerObject;
	}
	
	public void update(float dt)
	{
		//update timers
		recordTime += dt;
		recordDistance = playerObject.getX();
		if (recordDistance > bestDistanceOfAll)
			bestDistanceOfAll = recordDistance;
		tutorialTimer += dt;
		if (tutorialTimer > TUTORIAL_TIME)
			showTutorial = false;
		if (isDead)
			timeDead += dt;
		
		float oldWorldSpeedFactor = worldSpeedFactor;
		if (increaseSpeed && !decreaseSpeed)
		{
			worldSpeedFactor *= BUTTON_MULTIPLIER_CONST;
			//Gdx.app.log("Button", "Increasing speed");
		}
		else if (decreaseSpeed && !increaseSpeed)
		{
			worldSpeedFactor /= BUTTON_MULTIPLIER_CONST;
			//Gdx.app.log("Button", "Decreasing speed");
		}
		if (worldSpeedFactor < MIN_WORLD_SPEED) worldSpeedFactor = MIN_WORLD_SPEED;
		if (worldSpeedFactor > MAX_WORLD_SPEED) worldSpeedFactor = MAX_WORLD_SPEED;
		float ratioMultiplier = worldSpeedFactor / oldWorldSpeedFactor;
		//Gdx.app.log("speed", "Multiplier"+worldSpeedFactor);
		
		//animations
		playerObject.update(dt);
		for (RunnerObject obj : sceneObjects)
		{ 
			if (obj instanceof SpikeObject)
			{ ((SpikeObject)obj).multiplyWorldFactor(ratioMultiplier); 
			obj.multiplySpeed(ratioMultiplier);}
			if (obj instanceof BouncerObject)
			{ ((BouncerObject)obj).multiplyWorldFactor(ratioMultiplier);
			obj.multiplySpeed(ratioMultiplier);}
			obj.update(dt); 
 		}
		
		
		//update sprite locations based on physics updates
		Array<Body> copy = new Array<Body>(sceneObjects.size() + 1);
		physicsWorld.getBodies(copy);
		for (Body b : copy)
		{
			RunnerObject obj = (RunnerObject)b.getUserData();
			obj.setX((int)b.getPosition().x - obj.getWidth() / 2);
			obj.setY((int)b.getPosition().y - obj.getHeight() / 2);
			obj.setRotation(b.getAngle());
			if (obj.needsSpeedRefreshed())
			{
				b.setLinearVelocity(obj.getLinearVelocity());
				b.setAngularVelocity(obj.getAngularVelocity());
				obj.setSpeedUpdated(); 
			}
			if (obj instanceof PlayerObject)
			{
				b.applyForceToCenter(40.0f, 0.0f, false);
				recordSpeed = b.getLinearVelocity().len2();
				if (recordSpeed < 10000f)
					fastCounter = 0.0f;
				else
					fastCounter += dt;
				if (fastCounter > bestFastTime)
					bestFastTime = fastCounter;
				
				if (b.getLinearVelocity().x == 0.0f)
					stoppedCounter += dt;
				else
					stoppedCounter = 0.0f;
				if (stoppedCounter > IDLE_DIE_TIME)
				{
					isDead = true;
					stoppedCounter = 0.0f;
				}
			}
		}
		
		// remove used scene objects
		for (Body b : copy)
		{
			if (b.getPosition().x < playerObject.getX() - Model.VIEWPORT_WIDTH / 2 - 300)
			{
				physicsWorld.destroyBody(b);
				sceneObjects.remove(b.getUserData());
			}
		}
		
		// generate future content randomly
		loadStuffRandomly();
		
		//step the physics
		physicsWorld.step(1.0f/60f, 6, 2);
	}
			
	private void loadStuffRandomly()
	{
		if (playerObject.getX() > (blockIndex) * VIEWPORT_WIDTH)
		{
			blockIndex++;
			//generate next block
			int blockX1 = (blockIndex * VIEWPORT_WIDTH) - VIEWPORT_WIDTH / 2, 
					blockX2 = (blockIndex * VIEWPORT_WIDTH) - VIEWPORT_WIDTH / 2 + VIEWPORT_WIDTH / 3, 
					blockX3 = (blockIndex * VIEWPORT_WIDTH) - VIEWPORT_WIDTH / 2 + VIEWPORT_WIDTH * 2 / 3;	//partition into three spaces
			int width = VIEWPORT_WIDTH / 3;
			Gdx.app.log("Runner", "Making next block..." + blockX1 + " " + blockX2 + " " + blockX3);
			int elevation1 = (int)(BASE_ELEVATION + (Math.random() * 90) - 45);
			WallObject ground = new WallObject(blockX1, -VIEWPORT_HEIGHT / 2, width + 5, elevation1 + (Model.VIEWPORT_HEIGHT / 2));
			int rand1 = (int)(Math.random() * 4);
			if (rand1 < 2)
			{
				SpikeSystem spikes = new SpikeSystem();
				spikes.addSpikes(blockX1 + (int)(Math.random() * width / 2), elevation1, (int)(Math.random() * 3) + 1, (float)Math.random() * 2.0f);
				for (SpikeObject spike : spikes.getSpikeList())
				{
					createPhysicsObject(spike);
					sceneObjects.add(spike);
				}
			}
			else if (rand1 == 2)
			{
				BouncerObject bouncer = new BouncerObject(blockX1 + (int)(Math.random() * width / 2), elevation1, 
						(int)(Math.random() * 40 + 5), (int)(Math.random() * 32) + 16);
				bouncer.randomize();
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
			}
			if (elevation1 > lastElevation)
			{
				BouncerObject bouncer = new BouncerObject(blockX1 - 64, lastElevation, 64, 32);
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
			}
			
			
			int elevation2 = (int)(BASE_ELEVATION + (Math.random() * 90) - 45);
			WallObject ground2 = new WallObject(blockX2, -VIEWPORT_HEIGHT / 2, width + 5, elevation2 + (Model.VIEWPORT_HEIGHT / 2));
			int rand2 = (int)(Math.random() * 4);
			if (rand2 < 2)
			{
				SpikeSystem spikes = new SpikeSystem();
				spikes.addSpikes(blockX2 + (int)(Math.random() * width / 2), elevation2, (int)(Math.random() * 5) + 1, (float)Math.random() * 2.0f);
				for (SpikeObject spike : spikes.getSpikeList())
				{
					createPhysicsObject(spike);
					sceneObjects.add(spike);
				}
			}
			else if (rand2 == 2)
			{
				BouncerObject bouncer = new BouncerObject(blockX2 + (int)(Math.random() * width / 2), elevation2, 
						(int)(Math.random() * 40), (int)(Math.random() * 32) + 16);
				bouncer.randomize();
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
			}
			if (elevation2 > elevation1)
			{
				BouncerObject bouncer = new BouncerObject(blockX2 - 64, elevation1, 64, 32);
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
				
			}
			
			
			int elevation3 = (int)(BASE_ELEVATION + (Math.random() * 90) - 45);
			WallObject ground3 = new WallObject(blockX3, -VIEWPORT_HEIGHT / 2, width + 5, elevation3 + (Model.VIEWPORT_HEIGHT / 2));
			int rand3 = (int)(Math.random() * 4);
			if (rand3 < 2)
			{
				SpikeSystem spikes = new SpikeSystem();
				spikes.addSpikes(blockX3 + (int)(Math.random() * width / 2), elevation3, (int)(Math.random() * 4) + 1, (float)Math.random() * 2.0f);
				for (SpikeObject spike : spikes.getSpikeList())
				{
					createPhysicsObject(spike);
					sceneObjects.add(spike);
				}
			}
			else if (rand3 == 2)
			{
				BouncerObject bouncer = new BouncerObject(blockX3 + (int)(Math.random() * width / 2), elevation3, 
						(int)(Math.random() * 40), (int)(Math.random() * 32) + 16);
				bouncer.randomize();
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
			}
			if (elevation3 > elevation2)
			{
				BouncerObject bouncer = new BouncerObject(blockX3 - 64, elevation2, 64, 32);
				createPhysicsObject(bouncer);
				sceneObjects.add(bouncer);
				
			}
			lastElevation = elevation3;
			
			createPhysicsObject(ground);
			createPhysicsObject(ground2);
			createPhysicsObject(ground3);
				
			sceneObjects.add(ground);
			sceneObjects.add(ground2);
			sceneObjects.add(ground3);
		}
	}
	
	private void loadScene()
	{
		//initialize scene objects
		createPhysicsObject(playerObject);

		lastElevation = BASE_ELEVATION;
		RunnerObject ground = new WallObject(-VIEWPORT_WIDTH / 2, -VIEWPORT_HEIGHT / 2, VIEWPORT_WIDTH, lastElevation + VIEWPORT_HEIGHT / 2);
		sceneObjects.add(ground);
		createPhysicsObject(ground);
		
		physicsWorld.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				if ((contact.getFixtureA().getBody().getUserData() instanceof PlayerObject && 
						contact.getFixtureB().getBody().getUserData() instanceof SpikeObject) ||
						(contact.getFixtureB().getBody().getUserData() instanceof PlayerObject && 
						contact.getFixtureA().getBody().getUserData() instanceof SpikeObject))
				{
					//Gdx.app.log("Runner", "Hit spike");
					isDead = true;
				}		
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
		});
	}
	
	private void createPhysicsObject(RunnerObject obj)
	{
		Body b = physicsWorld.createBody(obj.getBodyDefinition());
		b.setUserData(obj);
		Shape shape;
		if (obj instanceof WheelObject)
		{
			shape = new CircleShape();
			((CircleShape)shape).setRadius(obj.getWidth() / 2);
		}
		else
		{
			shape = new PolygonShape();
			//if (obj instanceof SpikeObject)
			//{
			//	((PolygonShape)shape).setAsBox(obj.getWidth() / 2, 22);//HARD CODED DAT
			//}
			//else
			((PolygonShape)shape).setAsBox(obj.getWidth() / 2, obj.getHeight() / 2);
		}
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.friction = 0.0f;
		b.createFixture(fixture);
		b.setLinearVelocity(obj.getLinearVelocity());
		b.setAngularVelocity(obj.getAngularVelocity());
		shape.dispose();
	}
	
}