package com.gravity.ball.body;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gravity.ball.Couleurs;
import com.gravity.ball.MyGdxGame;
import com.gravity.ball.Variables;

public class Plateforme {

	final MyGdxGame game;
	private PolylineMapObject polylineObject;
	private Vector2[] ligne;
	private Vector2 direction;
	private BodyDef bodyDef;
	private PolygonShape ps;
	public Body body;
	private FixtureDef fixtureDef;
	private int étape;
	public float width, height, largeur, vitesse;
	private boolean retour, boucle;
	
	public Plateforme(final MyGdxGame game, World world, PolylineMapObject polylineObject){
		this.game = game;
		this.polylineObject = polylineObject;
		retour = false;
		étape = 1;
		
		if(polylineObject.getProperties().get("Vitesse") != null)
			vitesse = Float.parseFloat((String) polylineObject.getProperties().get("Vitesse"));
		else vitesse = 5;
		
		if(polylineObject.getProperties().get("Boucle") != null)
			boucle = true;
		else boucle = false;
		
		if(polylineObject.getProperties().get("Largeur") != null)
			largeur = Integer.parseInt((String) polylineObject.getProperties().get("Largeur"));
		else
			largeur = 2;
		
		width = largeur*(32 * Variables.WORLD_TO_BOX/2);
		height = 16*Variables.WORLD_TO_BOX;
		direction = new Vector2();
		
		ligne = new Vector2[polylineObject.getPolyline().getTransformedVertices().length/2];
    	for(int i = 0; i < ligne.length; i++){
    		ligne[i] = new Vector2(polylineObject.getPolyline().getTransformedVertices()[i*2]*Variables.WORLD_TO_BOX, polylineObject.getPolyline().getTransformedVertices()[i*2 + 1]*Variables.WORLD_TO_BOX);
    	}   
    	
    	ps = new PolygonShape();
    	ps.setAsBox(width, height);

    	bodyDef = new BodyDef();
    	bodyDef.type = BodyType.KinematicBody;
    	bodyDef.position.set(ligne[0]);
    	
    	fixtureDef = new FixtureDef();
		fixtureDef.shape = ps;
        fixtureDef.density = 0.0f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 0f;

    	body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData("Objet");
        body.setUserData("Objet");
        
        ps.dispose();

        direction = new Vector2(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y);
        body.setLinearVelocity(direction.clamp(vitesse, vitesse));
	}
	
	public void déplacement(){
		if(!boucle){
			if(!retour){
				if(!new Vector2(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y).hasSameDirection(direction)){
					étape++;
					
			        if(étape == ligne.length){
			        	retour = true;
			        	étape = ligne.length - 2;
			        }
			        
					direction.set(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y);
				}
			}
			else{
				if(!new Vector2(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y).hasSameDirection(direction)){
					étape--;
					
			        if(étape < 0){
			        	retour = false;
			        	étape = 1;
			        }
			        
					direction.set(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y);
				}
			}	
		}
		else{
			if(!new Vector2(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y).hasSameDirection(direction)){
				étape++;
				
		        if(étape == ligne.length){
		        	étape = 0;
		        }
		        
				direction.set(ligne[étape].x - body.getPosition().x, ligne[étape].y - body.getPosition().y);
			}
		}
        body.setLinearVelocity(direction.clamp(vitesse, vitesse));  
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return body.getPosition().x;
	}
	
	public float getY(){
		return body.getPosition().y;
	}
	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas, Couleurs couleurs){
		/*
		if(durete == 0) 
			batch.setColor(couleurs.getCouleur0());
		else if(durete == 1) 
			batch.setColor(couleurs.getCouleur1());
		else if(durete == 2) 
			batch.setColor(couleurs.getCouleur2());
		else if(durete == 3)
			batch.setColor(couleurs.getCouleur3());
		else if(durete == 4)
			batch.setColor(couleurs.getCouleur4());
		*/

		batch.setColor(couleurs.getCouleurSol());
		batch.draw(textureAtlas.findRegion("Barre"), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				width,
				height,
				2 * width,
				2 * height,
				1,
				1,
				body.getAngle()*MathUtils.radiansToDegrees);
	}
	
	public void drawOmbre(SpriteBatch batch, TextureAtlas textureAtlas){
		batch.setColor(0,0,0,1);
		batch.draw(textureAtlas.findRegion("Barre"), 
				this.body.getPosition().x - width + Variables.ombresX, 
				this.body.getPosition().y - height + Variables.ombresY,
				width,
				height,
				2 * width,
				2 * height,
				1,
				1,
				body.getAngle()*MathUtils.radiansToDegrees);
	}
}
