package com.gravity.ball.body;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gravity.ball.Couleurs;

public class ObstacleLéger extends Obstacle{

	public ObstacleLéger(World world, Camera camera, RectangleMapObject rectangleObject, Couleurs couleurs){
		super(world, camera, rectangleObject, couleurs);
		
		body.setUserData("ObstacleLéger");
		body.getFixtureList().get(0).setRestitution(0.5f);
		
		//Couleur
		couleur = couleurs.getCouleurLéger();
		
		//Masse
		if(rectangleObject.getProperties().get("Masse") != null){
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * Float.parseFloat(rectangleObject.getProperties().get("Masse").toString())
			);
			body.resetMassData();
		}
		else{
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * 0.6f
			);
			body.resetMassData();
		}
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.DynamicBody;
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurLéger();
	}
}
