package com.gravity.ball.body;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gravity.ball.Couleurs;

public class ObstacleRotatif extends Obstacle{
	
	private float vitesse = 90;

	public ObstacleRotatif(World world, Camera camera, MapObject rectangleObject, Couleurs couleurs) {
		super(world, camera, rectangleObject, couleurs);
		
		//Couleur
		couleur = couleurs.getCouleurLéger();
		
		//Vitesse de rotation
		if(rectangleObject.getProperties().get("Vitesse") != null)
			vitesse = Float.parseFloat((String) rectangleObject.getProperties().get("Vitesse"));
		
		body.setFixedRotation(false);
		body.setAngularVelocity(vitesse*MathUtils.degreesToRadians);
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurLéger();
	}

}
