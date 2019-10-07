package com.gravity.ball.body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;
import com.gravity.ball.Couleurs;
import com.gravity.ball.Variables;

public class Balle {

	public Body body, bodyDetecteur;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private CircleShape balleShape;
	private PolygonShape detecteurShape;
	//private CircleShape detecteurShape;
	private float rayon, posXInit, posYInit;
	private Vector2 directionSaut;
	public boolean contactSupérieur;
	public int contact;
	
	public Balle(World world, Camera camera, TiledMap tiledMap){

		MapObjects personnages = (MapObjects)tiledMap.getLayers().get("Spawn").getObjects();
		
		contact = 1;
		Variables.SautTime = TimeUtils.millis();
		contactSupérieur = false;
		rayon = Variables.nbPixelTile * Variables.WORLD_TO_BOX / 2;	
		posXInit = (personnages.get("Balle").getProperties().get("x", float.class) + personnages.get("Balle").getProperties().get("width", float.class)/2) * Variables.WORLD_TO_BOX;
		posYInit = (personnages.get("Balle").getProperties().get("y", float.class) + personnages.get("Balle").getProperties().get("height", float.class)) * Variables.WORLD_TO_BOX;
		directionSaut = new Vector2();
		
		balleShape = new CircleShape();
		balleShape.setRadius(rayon);	
		
		bodyDef = new BodyDef();
		bodyDef.position.set(posXInit, posYInit);
        bodyDef.type = BodyType.DynamicBody; 
        bodyDef.bullet = true;						//TRÈS IMPORTANT POUR ÉVITER L'EFFET TUNNEL À HAUTE VITESSE
        
        body = world.createBody(bodyDef);
        //body.setFixedRotation(false);
        body.setFixedRotation(true);
        	
        fixtureDef = new FixtureDef();  
        fixtureDef.shape = balleShape; 
        fixtureDef.density = (float)(Variables.DENSITÉ/(rayon * rayon * Math.PI));
        fixtureDef.friction = 0.01f;  
        fixtureDef.restitution = 0.1f;  
        body.createFixture(fixtureDef).setUserData("Balle"); 
        
        //Création du détecteur
        //bodyDef.position.y = bodyDef.position.y - rayon/2;
        detecteurShape = new PolygonShape();
        //detecteurShape.setAsBox(0.8f*rayon,0.66f*rayon, new Vector2(0, 0.33f*rayon), 0); 
        detecteurShape.setAsBox(0.5f*rayon, rayon, new Vector2(0, 0), 0); 
        //detecteurShape = new CircleShape();
        //detecteurShape.setRadius(2f*rayon);
		fixtureDef.shape = detecteurShape;
        fixtureDef.density = 0.0f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 0.0f;  
		fixtureDef.isSensor = true;			
		body.createFixture(fixtureDef).setUserData("BalleDetecteur");
		
        body.setUserData("Balle");
        
        balleShape.dispose();
        detecteurShape.dispose();
	}
	
	public void déplacement(){
		
		if(Gdx.input.isKeyPressed(Keys.W) && contact > 0 /*&& !contactSupérieur*/){
			if(TimeUtils.millis() - Variables.SautTime >= 330){
				body.applyForceToCenter(0, Variables.sautBalle, true);
	        	contact = 0;
	        	contactSupérieur = false;
	        	System.out.println("TimeUtils.millis() - Variables.SautTime = " + (TimeUtils.millis() - Variables.SautTime));
	        	Variables.SautTime = TimeUtils.millis();
			}
        }
        
		if(Gdx.input.isKeyPressed(Keys.A)){
        	body.applyForceToCenter(-Variables.vitesseBalle, 0, true);
        }
		else if(Gdx.input.isKeyPressed(Keys.D)){
        	body.applyForceToCenter(Variables.vitesseBalle, 0, true);
        }
		/*
		//Limitation de la vitesse verticale      
        if(body.getLinearVelocity().y > Variables.vitesseMaxBalle){
        	System.out.println("Vitesse verticale = " + body.getLinearVelocity().y);
        	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, Variables.vitesseMaxBalle));
        }
        */
        /*
        if(body.getLinearVelocity().y == 0){
        	contact = 1;
        	contactSupérieur = true;
        }
        */
        
		/*
        if(Gdx.input.isKeyPressed(Keys.A)){
        	déplacementCharacter.x = - vitesse * Variables.WORLD_TO_BOX;
        	droite = false;
        }
	    else if(Gdx.input.isKeyPressed(Keys.D)){
	    	déplacementCharacter.x = vitesse * Variables.WORLD_TO_BOX;
	    	droite = true;
	    }
	    else 
	    	déplacementCharacter.x = 0;
        */
        //Déplacement avec les boutons
        /*
		if(boutonSaut.isPressed() && contact > 0){
        	bodyHero.applyForceToCenter(0, saut, true);
        	contact = 0;
        }
        if(boutonGauche.isPressed()){
        	déplacementCharacter.x = -vitesse * Variables.WORLD_TO_BOX;
        	droite = false;
        }
	    else if(boutonDroite.isPressed()){
	    	déplacementCharacter.x = vitesse * Variables.WORLD_TO_BOX;
	    	droite = true;
	    }
	    else 
	    	déplacementCharacter.x = 0;
        */
        /*
        déplacementCharacter.y = bodyHero.getLinearVelocity().y;   
        bodyHero.setLinearVelocity(new Vector2(déplacementCharacter.x + vitesseBase, déplacementCharacter.y));
        */
	}
	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas, Couleurs couleurs){
		batch.setColor(couleurs.getCouleurBalle());
		batch.draw(textureAtlas.findRegion("Balle"), 
				this.body.getPosition().x - rayon, 
				this.body.getPosition().y - rayon, 
				2 * rayon, 
				2 * rayon);
	}
	
	public void drawOmbre(SpriteBatch batch, TextureAtlas textureAtlas){
		batch.setColor(0,0,0,1);
		batch.draw(textureAtlas.findRegion("Balle"), 
				this.body.getPosition().x - rayon + Variables.ombresX, 
				this.body.getPosition().y - rayon + Variables.ombresY, 
				2 * rayon, 
				2 * rayon);
	}
	
	public float getX(){
		return body.getPosition().x;
	}
	
	public float getY(){
		return body.getPosition().y;
	}
	
	public Vector2 getOrigine(){
		return new Vector2(posXInit, posYInit);
	}
	
	public void setDirectionSaut(Vector2 vec2){
		directionSaut = vec2.nor();
		//System.out.println("directionSaut : " + directionSaut + " || contact : " + contact);
	}
	
	public void saute(){
		if(Gdx.input.isKeyPressed(Keys.W))
        	System.out.println("contact = " + contact + " || contactSupérieur = " + contactSupérieur);
		if(Gdx.input.isKeyPressed(Keys.W) && contact > 0 && contactSupérieur){
			if(body.getLinearVelocity().y < 0)
	        	body.applyForceToCenter(0, Variables.sautBalle, true);
				//body.setLinearVelocity(body.getLinearVelocity().x, 0);
        	body.applyForceToCenter(0, Variables.sautBalle, true);
        	System.out.println("contact = " + contact + " || contactSupérieur = " + contactSupérieur);
        	contact = 0;
        	contactSupérieur = false;
        	System.out.println("----------------------------------------SAUT");
        	System.out.println("contact = " + contact + " || contactSupérieur = " + contactSupérieur);
        }
	}
	
	public void restart(){
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
		body.setTransform(getOrigine(), body.getAngle());
	}
}
