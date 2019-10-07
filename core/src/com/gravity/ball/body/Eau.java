package com.gravity.ball.body;

import Box2DUtils.BuoyancyController;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.gravity.ball.Couleurs;

public class Eau extends Obstacle{

	public BuoyancyController buoyancyController;
	
	public Eau(World world, Camera camera, MapObject rectangleObject, Couleurs couleurs) {
		super(world, camera, rectangleObject, couleurs);
		
		//Couleur
		couleur = couleurs.getCouleurEau();
		
		body.getFixtureList().get(0).setSensor(true);
		body.getFixtureList().get(0).setUserData("Eau");
		body.getFixtureList().get(0).setDensity(0.105f);
		body.setUserData("Eau");
		
		buoyancyController = new BuoyancyController(world, body.getFixtureList().get(0));
	}
	
	@Override
	public void actif(){
		buoyancyController.step();
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurEau();
	}
	
	@Override
	public void drawOmbre(SpriteBatch batch, TextureAtlas textureAtlas){
	}
	
	public BuoyancyController getBuoyancyController(){
		return buoyancyController;
	}
}
