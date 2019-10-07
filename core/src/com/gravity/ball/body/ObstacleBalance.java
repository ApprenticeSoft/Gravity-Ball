package com.gravity.ball.body;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.gravity.ball.Couleurs;

public class ObstacleBalance extends Obstacle{

	private Body bodyAttache;
	private PolygonShape attacheShape;
	public RevoluteJoint revoluteJoint;
	private float speed = 200;
	private float torque = 800;
	private float attacheX = 0;
	private float attacheY = 0;
	private float angleRef = 0;
	private float angleMin, angleMax;
	
	public ObstacleBalance(World world, Camera camera, MapObject rectangleObject, Couleurs couleurs) {
		super(world, camera, rectangleObject, couleurs);
		
		//Couleur
		couleur = couleurs.getCouleurLéger();
		
		//Position du point d'attache
		if(width >= height){		
			if(rectangleObject.getProperties().get("Position") != null)
				attacheX = width * Float.parseFloat(rectangleObject.getProperties().get("Position").toString());
			else
				attacheX = 0;
		}
		else{	
			if(rectangleObject.getProperties().get("Position") != null)
				attacheY = height * Float.parseFloat(rectangleObject.getProperties().get("Position").toString());
			else
				attacheY = 0;
		}	
		//Angles de référence, Min et Max
		if(rectangleObject.getProperties().get("angleRef") != null)
			angleRef = Float.parseFloat(rectangleObject.getProperties().get("angleRef").toString()) * MathUtils.degreesToRadians;
		if(rectangleObject.getProperties().get("angleMin") != null)
			angleMin = Float.parseFloat(rectangleObject.getProperties().get("angleMin").toString()) * MathUtils.degreesToRadians;
		if(rectangleObject.getProperties().get("angleMax") != null)
			angleMax = Float.parseFloat(rectangleObject.getProperties().get("angleMax").toString()) * MathUtils.degreesToRadians;	
		//Masse
		if(rectangleObject.getProperties().get("Masse") != null){
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * Float.parseFloat(rectangleObject.getProperties().get("Masse").toString())
			);
			body.resetMassData();
		}
		//Collision
		if(rectangleObject.getProperties().get("Contact") != null)
			if(rectangleObject.getProperties().get("Contact").toString().equals("oui"))
				body.setUserData("Contact");
		//Vitesse et force
		if(rectangleObject.getProperties().get("speed") != null)
			speed = Float.parseFloat(rectangleObject.getProperties().get("speed").toString());
		if(rectangleObject.getProperties().get("torque") != null)
			torque = Float.parseFloat(rectangleObject.getProperties().get("torque").toString());
		
		//Création du point d'attache
		bodyDef.type = BodyType.StaticBody;	
		bodyDef.position.x = bodyDef.position.x + attacheX;
		bodyDef.position.y = bodyDef.position.y + attacheY;
		
		attacheShape = new PolygonShape();
		attacheShape.setAsBox(0.1f, 0.1f); 	
		fixtureDef.shape = attacheShape;
        fixtureDef.density = 0.0f;  
        fixtureDef.friction = 1.0f; 
        fixtureDef.restitution = 0.0f; 
        fixtureDef.isSensor = true;
		
		bodyAttache = world.createBody(bodyDef);
		bodyAttache.createFixture(fixtureDef).setUserData("Attache");;
		
		//Création du joint
		RevoluteJointDef rjDef = new RevoluteJointDef();
		rjDef.bodyA = body;
		rjDef.bodyB = bodyAttache;
		rjDef.collideConnected = false;
		rjDef.localAnchorA.set(attacheX, attacheY);
		rjDef.enableMotor = true;
		rjDef.motorSpeed = speed;
		rjDef.maxMotorTorque = torque;
		rjDef.enableLimit = true;
		rjDef.lowerAngle = angleMin;
		rjDef.upperAngle = angleMax;
		rjDef.referenceAngle = angleRef;
		
		revoluteJoint = (RevoluteJoint) world.createJoint(rjDef);
		attacheShape.dispose();
		
		System.out.println("bodyAttache.getPosition() = " + bodyAttache.getPosition().toString());
		System.out.println("body.getPosition() = " + body.getPosition().toString());
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.DynamicBody;
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurLéger();
	}
	
	@Override
	public void actif(){
		/*
		revoluteJoint.setMaxMotorTorque(torque * Math.abs(revoluteJoint.getJointAngle()));
		revoluteJoint.setMotorSpeed(speed * Math.abs(revoluteJoint.getJointAngle()) * -Math.signum(revoluteJoint.getJointAngle()));
		if(revoluteJoint.getJointAngle() <= revoluteJoint.getLowerLimit() || revoluteJoint.getJointAngle() >= revoluteJoint.getUpperLimit())
			revoluteJoint.setMotorSpeed(-revoluteJoint.getMotorSpeed());
		*/

		
		//System.out.println("revoluteJoint.getMotorSpeed() = " + revoluteJoint.getMotorSpeed());
		//System.out.println("revoluteJoint.getJointAngle() = " + revoluteJoint.getJointAngle());
		//System.out.println("revoluteJoint.getMaxMotorTorque() = " + revoluteJoint.getMaxMotorTorque());
	}
}
