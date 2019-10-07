package com.gravity.ball;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gravity.ball.body.Balle;
import com.gravity.ball.body.Eau;
import com.gravity.ball.body.Obstacle;
import com.gravity.ball.body.ObstacleL�ger;
import com.gravity.ball.body.ObstacleBalance;
import com.gravity.ball.body.ObstacleRotatif;
import com.gravity.ball.body.ObstacleBalan�oire;
import com.gravity.ball.body.ObstaclePoulie;
import com.gravity.ball.body.ObstacleSuspendu;
import com.gravity.ball.body.Plateforme;
import com.gravity.ball.body.Polygone;

public class LecteurCarte {

    MapObjects objects;
	public Array<Obstacle> obstacles, obstaclesOrganis�s;
	public Array<Polygone> polygones;
	public Array<Plateforme> plateformes;
	private Array<MapObject> poulies;
	public Balle balle;
	OrthographicCamera camera;
	World world;
	public float eauPosX, eauPosY, cameraOrigineX, cameraOrigineY;
	private Couleurs couleurs;
    
	public LecteurCarte(final MyGdxGame game, TiledMap tiledMap, World world, OrthographicCamera camera2, Couleurs couleurs){
		this.camera = camera2;
		this.world = world;
		this.couleurs = couleurs;

		cameraOrigineX = camera2.position.x;
		cameraOrigineY = camera2.position.y;
		
		System.out.println("Position camera : " + cameraOrigineX + ", " + cameraOrigineY);
		
		balle = new Balle(world, camera, tiledMap);
		objects = tiledMap.getLayers().get("D�cor").getObjects();
		
		poulies = new Array<MapObject>();

        obstacles = new Array<Obstacle>();    
        obstaclesOrganis�s = new Array<Obstacle>();        
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            if(rectangleObject.getProperties().get("Type") != null){
            	//Objets l�gers
            	if(rectangleObject.getProperties().get("Type").equals("L�ger")){
	            	ObstacleL�ger obstacle = new ObstacleL�ger(world, camera2, rectangleObject, couleurs);
	                obstacles.add(obstacle);
            	}
            	//Objets rotatifs
            	else if(rectangleObject.getProperties().get("Type").equals("Rotatif")){
	            	ObstacleRotatif obstacle = new ObstacleRotatif(world, camera2, rectangleObject, couleurs);
	                obstacles.add(obstacle);
            	}
            	//Objets ressorts
            	else if(rectangleObject.getProperties().get("Type").equals("Balance")){
	            	ObstacleBalance obstacle = new ObstacleBalance(world, camera2, rectangleObject, couleurs);
	                obstacles.add(obstacle);
            	}
            	//Balan�oires
            	else if(rectangleObject.getProperties().get("Type").equals("Balan�oire")){
	            	ObstacleBalan�oire obstacle = new ObstacleBalan�oire(world, camera2, rectangleObject, couleurs);
	                obstacles.add(obstacle);
            	}
            	//Obstacle Suspendu
            	else if(rectangleObject.getProperties().get("Type").equals("Suspendu")){
	            	ObstacleSuspendu obstacle = new ObstacleSuspendu(world, camera2, rectangleObject, couleurs);
	                obstacles.add(obstacle);
            	}
            	//Poulies
            	else if(rectangleObject.getProperties().get("Type").equals("Poulie")){
	            	poulies.add(rectangleObject);
	            	System.out.println("poulies.size = " + poulies.size);
            	}
            	//Eau
            	else if(rectangleObject.getProperties().get("Type").equals("Eau")){
	            	Eau eau = new Eau(world, camera2, rectangleObject, couleurs);
	                obstacles.add(eau);
            	}
            	//TEST
            	//else if(rectangleObject.getProperties().get("Type").equals("Test")){
	            //	ObstacleTest obstacle = new ObstacleTest(world, camera2, rectangleObject, couleurs);
	            //    obstacles.add(obstacle);
            	//}
            }
            else{
            	Obstacle obstacle = new Obstacle(world, camera2, rectangleObject, couleurs);
                obstacles.add(obstacle);
            }
        }
        
        //Cr�ation des poulies
        for(int i = poulies.size - 1; i > -1; i--){
        	if(poulies.get(i).getProperties().get("Groupe") != null){
        		for(int j = 0; j < poulies.size; j++){
        			if(Integer.parseInt(poulies.get(i).getProperties().get("Groupe").toString()) == Integer.parseInt(poulies.get(j).getProperties().get("Groupe").toString()) &&
        					i != j){  				
        				ObstaclePoulie obstacle = new ObstaclePoulie(world, camera, poulies.get(i), couleurs, poulies.get(j));
        				obstacles.add(obstacle);
        				
        				poulies.removeIndex(i);
        				poulies.removeIndex(j);
        				i--;
        			}
        		}
        	}	
        	else
    			System.out.println("TEST");
        }

        //Cr�ation de polygones
        polygones = new Array<Polygone>();
        for(PolygonMapObject polygonObject : objects.getByType(PolygonMapObject.class)){
        	Polygone polygone = new Polygone(world, camera2, polygonObject, couleurs);
        	polygones.add(polygone);
        }

        //Plateformes mobiles
        plateformes = new Array<Plateforme>();      
        for(PolylineMapObject polylineObject : objects.getByType(PolylineMapObject.class)){
        	if(polylineObject.getProperties().get("Eau") == null){
        		Plateforme plateforme = new Plateforme(game, world, polylineObject);
        		plateformes.add(plateforme);
        	}
        	else{
        		eauPosX = polylineObject.getPolyline().getTransformedVertices()[0];
        		eauPosY = polylineObject.getPolyline().getTransformedVertices()[1];
        		System.out.println("eauPosX = " + eauPosX);
        		System.out.println("eauPosY = " + eauPosY);
        	}
        }
        
        //Organisation des obstacles
        for(Obstacle obstacle : obstacles){
        	if(obstacle.getClass().toString().equals("class com.gravity.ball.body.ObstacleRessort"))
        		obstaclesOrganis�s.add(obstacle);
        }  
        for(Obstacle obstacle : obstacles){      	
        	if(!obstacle.getClass().toString().equals("class com.gravity.ball.body.ObstacleRessort") && !obstacle.getClass().toString().equals("class com.gravity.ball.body.Eau") && !obstacle.getClass().toString().equals("class com.gravity.ball.body.Obstacle"))
        		obstaclesOrganis�s.add(obstacle);
        } 
        for(Obstacle obstacle : obstacles){
        	if(obstacle.getClass().toString().equals("class com.gravity.ball.body.Eau")){
        		obstaclesOrganis�s.add(obstacle);
        	}
        } 
        for(Obstacle obstacle : obstacles){
        	if(obstacle.getClass().toString().equals("class com.gravity.ball.body.Obstacle")){
        		obstaclesOrganis�s.add(obstacle);
        	}
        }  
        /*
        System.out.println("obstacles.size = " + obstacles.size);
        System.out.println("obstaclesOrganis�s.size = " + obstaclesOrganis�s.size);

        System.out.println("********************************************************");
        for(int i = 0; i < obstacles.size; i++)
            System.out.println("obstacles.get(" + i + ") = " + obstacles.get(i).getClass().toString());

        System.out.println("********************************************************");
        for(int i = 0; i < obstaclesOrganis�s.size; i++)
            System.out.println("obstaclesOrganis�s.get(" + i + ") = " + obstaclesOrganis�s.get(i).getClass().toString());
        */ 
	}
	
	public Array<Obstacle> getObstacles(){
		return obstacles;
	}
	
	public Array<Plateforme> getPlateformes(){
		return plateformes;
	}
	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){
		//drawOmbre(batch, textureAtlas);
		drawBalle(batch, textureAtlas);
		drawPlateforme(batch, textureAtlas);
		drawObstacle(batch, textureAtlas);
	}
	
	public void drawBalle(SpriteBatch batch, TextureAtlas textureAtlas){  
        balle.draw(batch, textureAtlas, couleurs);        
	}		
	
	public void drawPlateforme(SpriteBatch batch, TextureAtlas textureAtlas){  
        for(Plateforme plateforme : plateformes){
        	plateforme.draw(batch, textureAtlas, couleurs);
        }         
	}	
	
	public void drawObstacle(SpriteBatch batch, TextureAtlas textureAtlas){
		 for(Obstacle obstacle : obstaclesOrganis�s)
			 obstacle.draw(batch, textureAtlas);
		/*
        for(Obstacle obstacle : obstacles){
        	if(obstacle.getClass().toString().equals("class com.gravity.ball.body.ObstacleRessort")){
        		obstacle.draw(batch, textureAtlas);
        	}
        }  
        for(Obstacle obstacle : obstacles){      	
        	if(!obstacle.getClass().toString().equals("class com.gravity.ball.body.ObstacleRessort") && !obstacle.getClass().toString().equals("class com.gravity.ball.body.Eau"))
            	obstacle.draw(batch, textureAtlas);
        } 
        for(Obstacle obstacle : obstacles){
        	if(obstacle.getClass().toString().equals("class com.gravity.ball.body.Eau")){
        		obstacle.draw(batch, textureAtlas);
        	}
        }  
        */   
	}
	
	public void drawPolygone(PolygonSpriteBatch batch, MyCamera camera){  
        for(Polygone polygone : polygones){
        	polygone.setPos(cameraOrigineX - camera.position.x, cameraOrigineY - camera.position.y);
        	polygone.draw(batch, couleurs);
        }         
	}
	
	public void drawPolygoneOmbre(PolygonSpriteBatch batch, MyCamera camera){  
        for(Polygone polygone : polygones){
        	polygone.setPos(cameraOrigineX - camera.position.x + Variables.ombresX, cameraOrigineY - camera.position.y + Variables.ombresY);
        	polygone.draw(batch, couleurs);
        }         
	}
	
	public void drawOmbre(SpriteBatch batch, TextureAtlas textureAtlas){
		balle.drawOmbre(batch, textureAtlas);
		
		for(Plateforme plateforme : plateformes){
        	plateforme.drawOmbre(batch, textureAtlas);
        } 
		
		for(Obstacle obstacle : obstacles){
        	obstacle.drawOmbre(batch, textureAtlas);
        } 
	}
}
