package com.gravity.ball.body;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;
import com.gravity.ball.Couleurs;
import com.gravity.ball.Variables;

public class Polygone extends Obstacle{

	private Texture textureSolid;
	private PolygonSprite polySprite;
	float coordPoly[];
	
	public Polygone(World world, Camera camera, MapObject Object, Couleurs couleurs) {
		super(world, camera, Object, couleurs);
	}
	
	@Override
	public void create(World world, Camera camera, MapObject Object, Couleurs couleurs){
		coordPoly = new float[((PolygonMapObject) Object).getPolygon().getTransformedVertices().length];
    	for(int i = 0; i < ((PolygonMapObject) Object).getPolygon().getTransformedVertices().length; i++){
    		coordPoly[i] = ((PolygonMapObject) Object).getPolygon().getTransformedVertices()[i]*Variables.WORLD_TO_BOX;
    		System.out.println("coordPoly[" + i + "] : " + coordPoly[i]);
    	}
    	
    	PolygonShape ps = new PolygonShape();
    	ps.set(coordPoly);
    	
    	bodyDef = new BodyDef();
    	bodyDef.type = getBodyType();
    	
		body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = ps;
        fixtureDef.density = 2.0f;  
        fixtureDef.friction = 0f;  
        fixtureDef.restitution = 0.8f;
   
        body.createFixture(fixtureDef).setUserData("Objet");
        body.setUserData("Objet");
        body.setFixedRotation(false);
        
        this.dispose();
        
        /****************DESSIN*****************/
        // Creating the color filling (but textures would work the same way)
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(getCouleur()); 
        pix.fill();
        textureSolid = new Texture(pix);
        TextureRegion textureRegion = new TextureRegion(textureSolid);

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(coordPoly);

        PolygonRegion polyReg = new PolygonRegion(textureRegion, coordPoly, triangleIndices.toArray());

        polySprite = new PolygonSprite(polyReg);
        /***************************************/
	}
	
	public void draw(PolygonSpriteBatch polyBatch, Couleurs couleurs){
	    polySprite.draw(polyBatch);
	}
	
	public void drawOmbre(PolygonSpriteBatch polyBatch){
	    polySprite.draw(polyBatch);
	}
	
	public void setPos(float X, float Y){
        polySprite.setX(X);
        polySprite.setY(Y);
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurSol();
	}

}
