package com.gravity.ball.body;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.gravity.ball.Couleurs;
import com.gravity.ball.Variables;

public class ObstacleBalan�oire extends Obstacle{
	
	private Body bodyAttache;
	private PolygonShape attacheShape;
	public RopeJoint ropeJointGauche, ropeJointDroite;
	private float �paisseurCorde = 0.2f;
	private float attacheY = 5 * Variables.nbPixelTile * Variables.WORLD_TO_BOX;
	
	public ObstacleBalan�oire(World world, Camera camera, MapObject rectangleObject, Couleurs couleurs) {
		super(world, camera, rectangleObject, couleurs);
		body.setUserData("Contact");
		
		//Couleur
		couleur = couleurs.getCouleurL�ger();
		
		//Longeur des cordes
		if(rectangleObject.getProperties().get("attacheY") != null){
			attacheY = Float.parseFloat(rectangleObject.getProperties().get("attacheY").toString()) * Variables.nbPixelTile * Variables.WORLD_TO_BOX;
		}
		
		//Masse
		if(rectangleObject.getProperties().get("Masse") != null){
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * Float.parseFloat(rectangleObject.getProperties().get("Masse").toString())
			);
			body.resetMassData();
		}
		else{
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * 5
			);
			body.resetMassData();
		}
		
		//Cr�ation du point d'attache
		bodyDef.type = BodyType.StaticBody;	
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
		
		//Cr�ation du Weldjoint
		/*
		WeldJointDef wjDef = new WeldJointDef();	
		wjDef.bodyA = body;
		wjDef.bodyB = bodyAttache;
		wjDef.collideConnected = false;
		wjDef.localAnchorA.set(attacheX, attacheY);
		wjDef.frequencyHz = 10;
		wjDef.dampingRatio = 0;
		wjDef.referenceAngle = angleRef;
		
		weldJoint = (WeldJoint) world.createJoint(wjDef);
		*/
		
		//Cr�ation du Ropejoint
		RopeJointDef ropejDef = new RopeJointDef();
		ropejDef.bodyA = body;
		ropejDef.bodyB = bodyAttache;
		ropejDef.collideConnected = false;
		ropejDef.localAnchorA.set(0.95f * width,0);
		ropejDef.localAnchorB.set(0,0);
		
		ropeJointDroite = (RopeJoint) world.createJoint(ropejDef);
		ropeJointDroite.setMaxLength(ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len());
		
		//Cr�ation du 2e Ropejoint
		RopeJointDef ropejDef2 = new RopeJointDef();
		ropejDef2.bodyA = body;
		ropejDef2.bodyB = bodyAttache;
		ropejDef2.collideConnected = false;
		ropejDef2.localAnchorA.set(-0.95f * width,0);
		ropejDef2.localAnchorB.set(0,0);
		
		ropeJointGauche = (RopeJoint) world.createJoint(ropejDef2);
		ropeJointGauche.setMaxLength(ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len());

		attacheShape.dispose();
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.DynamicBody;
	}
	
	@Override
	public Color getCouleur(){
		return couleurs.getCouleurL�ger();
	}
	
	@Override
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){
		//Dessin de la corde gauche
		batch.setColor(0,0,0,1);
		batch.draw(textureAtlas.findRegion("Barre"), 
					(ropeJointGauche.getAnchorB().x + ropeJointGauche.getAnchorA().x - �paisseurCorde)/2,
					(ropeJointGauche.getAnchorB().y + ropeJointGauche.getAnchorA().y - ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len())/2,
					�paisseurCorde/2,																				//Origine X (pour la rotation)
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len()/2,							//Origine Y (pour la rotation)
					�paisseurCorde,																					//Largeur de la corde
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len(), 							//Longueur de la corde
					1,
					1,
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).angle() + 90
					);
		
		//Dessin de la corde droite
		batch.draw(textureAtlas.findRegion("Barre"), 
				(ropeJointDroite.getAnchorB().x + ropeJointDroite.getAnchorA().x - �paisseurCorde)/2,
				(ropeJointDroite.getAnchorB().y + ropeJointDroite.getAnchorA().y - ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len())/2,
				�paisseurCorde/2,																				//Origine X (pour la rotation)
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len()/2,							//Origine Y (pour la rotation)
				�paisseurCorde,																					//Largeur de la corde
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len(), 							//Longueur de la corde
				1,
				1,
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).angle() + 90
				);
		
		//Dessin de la balan�oire
		batch.setColor(getCouleur());
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
		super.drawOmbre(batch, textureAtlas);
		
		//Dessin de la corde gauche
		batch.draw(textureAtlas.findRegion("Barre"), 
					(ropeJointGauche.getAnchorB().x + ropeJointGauche.getAnchorA().x - �paisseurCorde)/2 + Variables.ombresX,
					(ropeJointGauche.getAnchorB().y + ropeJointGauche.getAnchorA().y - ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len())/2 + Variables.ombresY,
					�paisseurCorde/2,																				
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len()/2,							
					�paisseurCorde,																					
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).len(), 							
					1,
					1,
					ropeJointGauche.getAnchorB().sub(ropeJointGauche.getAnchorA()).angle() + 90
					);
		
		//Dessin de la corde droite
		batch.draw(textureAtlas.findRegion("Barre"), 
				(ropeJointDroite.getAnchorB().x + ropeJointDroite.getAnchorA().x - �paisseurCorde)/2 + Variables.ombresX,
				(ropeJointDroite.getAnchorB().y + ropeJointDroite.getAnchorA().y - ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len())/2 + Variables.ombresY,
				�paisseurCorde/2,																				
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len()/2,							
				�paisseurCorde,																					
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).len(), 							
				1,
				1,
				ropeJointDroite.getAnchorB().sub(ropeJointDroite.getAnchorA()).angle() + 90
				);
	}
}
