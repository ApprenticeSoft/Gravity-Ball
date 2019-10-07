package com.gravity.ball.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.gravity.ball.Couleurs;
import com.gravity.ball.LecteurCarte;
import com.gravity.ball.MyCamera;
import com.gravity.ball.MyGdxGame;
import com.gravity.ball.OrthogonalTiledMapRendererWithSprites;
import com.gravity.ball.Variables;
import com.gravity.ball.body.Eau;
import com.gravity.ball.body.Obstacle;
import com.gravity.ball.body.Plateforme;

public class GameScreen extends InputAdapter implements Screen{

	final MyGdxGame game;
	private MyCamera camera;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	private LecteurCarte lecteurCarte;
	private World world;
    private Box2DDebugRenderer debugRenderer;
    
	private TextureAtlas textureAtlas; 

    private int nbTileHorizontal, dimension;
	private float ratio;
	private Couleurs couleurs;
	private boolean useFBO;
    
	private PolygonSpriteBatch polyBatch;
	private ShaderProgram shaderProgram;
    FrameBuffer fbo;
    TextureRegion fboRegion;
	
	public GameScreen(final MyGdxGame gam){
		game = gam;
		
		useFBO = true;
		nbTileHorizontal = 50;
		dimension = nbTileHorizontal * Variables.nbPixelTile;
		ratio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		
		camera = new MyCamera();
		camera.setToOrtho(false, dimension * Variables.WORLD_TO_BOX, dimension * Variables.WORLD_TO_BOX * ratio);
        camera.update();  
		
        polyBatch = new PolygonSpriteBatch();
        polyBatch.setProjectionMatrix(camera.combined);
        
		textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
		couleurs = new Couleurs(1);
        
        world = new World(new Vector2(0, Variables.GRAVITÉ), true);
        /***********************************************************TEST********************************************************/
        /**********************************************POUR ÉVITER LES MICRO REBONDS********************************************/
		World.setVelocityThreshold(10.0f);	//La valeur par défaut est 1.0
		System.out.println("-----------------------------world.getVelocityThreshold() = " + world.getVelocityThreshold());
		/***********************************************************************************************************************/
        debugRenderer = new Box2DDebugRenderer();

        //tiledMap = new TmxMapLoader().load("Niveaux/Niveau 1.tmx");
        tiledMap = new TmxMapLoader().load("Niveaux/Niveau 1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap,Variables.WORLD_TO_BOX, game.batch);
        
        lecteurCarte = new LecteurCarte(gam, tiledMap, world, camera, couleurs);  

        //Shader
        fbo = new FrameBuffer(Format.RGBA4444, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        ShaderProgram.pedantic = false;	     
        shaderProgram = new ShaderProgram(
        		  Gdx.files.internal("Shaders/vPassThrough.glsl"),
        		  Gdx.files.internal("Shaders/fOmbre.glsl")
        		);
        System.out.println("Shader log : " + shaderProgram.getLog());
	}

	@Override
	public void render(float delta) {   
		Gdx.gl.glClearColor(couleurs.getCouleurFond().r,couleurs.getCouleurFond().g,couleurs.getCouleurFond().b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.mouvement(lecteurCarte.balle, tiledMap);
        camera.update();
        
		world.step(Variables.BOX_STEP, Variables.BOX_VELOCITY_ITERATIONS, Variables.BOX_POSITION_ITERATIONS); 
        //debugRenderer.render(world, camera.combined);	
		
		for(Plateforme plateforme : lecteurCarte.plateformes){
			plateforme.déplacement();
		}
		lecteurCarte.balle.déplacement();
        
        //Affichage de la carte
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        
        //Shader
        if(useFBO){
	        fbo.begin();
	      	Gdx.graphics.getGL20().glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
	      	Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT ); 
	      	game.batch.setProjectionMatrix(camera.combined);
	      	
	      	game.batch.begin();
			lecteurCarte.drawOmbre(game.batch, textureAtlas); 	
			game.batch.end();
		
			polyBatch.begin();
			lecteurCarte.drawPolygoneOmbre(polyBatch, camera);
			polyBatch.end();
	      	fbo.end();
	      	
	  		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
	  		fboRegion.flip(false, true);
	        
			game.batch.begin(); 
			game.batch.setColor(1, 1, 1, 1);
			game.batch.setShader(shaderProgram);
			game.batch.draw(fboRegion, 
							camera.position.x - camera.viewportWidth/2, 
							camera.position.y - camera.viewportHeight/2, 
							camera.viewportWidth, 
							camera.viewportHeight);
			game.batch.setShader(null);	
			lecteurCarte.draw(game.batch, textureAtlas); 
			game.batch.end();
			
			polyBatch.begin();
			lecteurCarte.drawPolygone(polyBatch, camera);
			polyBatch.end();
        }
        else{
        	game.batch.begin(); 
			game.batch.setColor(1, 1, 1, 1);
			lecteurCarte.draw(game.batch, textureAtlas/*, couleurs*/); 
			game.batch.setShader(null);	
			game.batch.end();
		
			polyBatch.begin();
			lecteurCarte.drawPolygone(polyBatch, camera);
			polyBatch.end();
	      	fbo.end();
        }
        
        //Activité du niveau
        for(Obstacle obstacle : lecteurCarte.obstacles)
        	obstacle.actif();
        
        //Niveau perdu
        if(lecteurCarte.balle.getY() < -5)
        	lecteurCarte.balle.restart();
		
		//Control FrameBuffer
        if(Gdx.input.isKeyJustPressed(Keys.Q))
        	useFBO = !useFBO;
        
        //System.out.println("lecteurCarte.balle.contact = " + lecteurCarte.balle.contact + " || lecteurCarte.balle.peutSauter = " + lecteurCarte.balle.peutSauter);  
	}
	
	@Override
	public void show() {
		world.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
			    Body b = contact.getFixtureB().getBody();
				Fixture FixtureA = contact.getFixtureA();
				Fixture FixtureB = contact.getFixtureB();
			    
			    if((a.getUserData() != null && a.getUserData().equals("Balle")) || (b.getUserData() != null && b.getUserData().equals("Balle"))) {
			    	if(!FixtureA.getUserData().equals("Eau") && !FixtureB.getUserData().equals("Eau"))
			    		lecteurCarte.balle.contact++;
			    	System.out.println("BEGIN : contact = " + lecteurCarte.balle.contact);
				}
			    if((FixtureA.getUserData() != null && FixtureA.getUserData().equals("BalleDetecteur")) || (FixtureB.getUserData() != null && FixtureB.getUserData().equals("BalleDetecteur"))) {
			    	if(!FixtureA.getUserData().equals("Eau") && !FixtureB.getUserData().equals("Eau"))
			    		lecteurCarte.balle.contactSupérieur = true;
				}
			    
			    //EAU
			    if ((FixtureA.getUserData() != null && FixtureA.getUserData().equals("Eau")) && FixtureB.getBody().getType() == BodyType.DynamicBody) {
			    	for(Obstacle obstacle : lecteurCarte.obstacles){
			    		if(obstacle.body.getFixtureList().get(0) == FixtureA){
			    			Eau eau = (Eau) obstacle;
			    			eau.buoyancyController.addBody(FixtureB);
			    		}
			    	}
				} 
			    else if ((FixtureB.getUserData() != null && FixtureB.getUserData().equals("Eau")) && FixtureA.getBody().getType() == BodyType.DynamicBody) {
					for(Obstacle obstacle : lecteurCarte.obstacles){
			    		if(obstacle.body.getFixtureList().get(0) == FixtureB){
			    			Eau eau = (Eau) obstacle;
			    			eau.buoyancyController.addBody(FixtureA);
			    		}
			    	}
				}
			}

			@Override
			public void endContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
			    Body b = contact.getFixtureB().getBody();
				Fixture FixtureA = contact.getFixtureA();
				Fixture FixtureB = contact.getFixtureB();
			    
			    if((a.getUserData() != null && a.getUserData().equals("Balle")) || (b.getUserData() != null && b.getUserData().equals("Balle"))) {
			    	if(lecteurCarte.balle.contact > 0)
			    		lecteurCarte.balle.contact--;
			    	System.out.println("END : contact = " + lecteurCarte.balle.contact);
				}
			    if((FixtureA.getUserData() != null && FixtureA.getUserData().equals("BalleDetecteur")) || (FixtureB.getUserData() != null && FixtureB.getUserData().equals("BalleDetecteur"))) {
			    	lecteurCarte.balle.contactSupérieur = false;
				}
			    
			    //EAU
			    if ((FixtureA.getUserData() != null && FixtureA.getUserData().equals("Eau")) && FixtureB.getBody().getType() == BodyType.DynamicBody) {
			    	for(Obstacle obstacle : lecteurCarte.obstacles){
			    		if(obstacle.body.getFixtureList().get(0) == FixtureA){
			    			Eau eau = (Eau) obstacle;
			    			eau.buoyancyController.removeBody(FixtureB);
			    		}
			    	}
				} 
			    else if ((FixtureB.getUserData() != null && FixtureB.getUserData().equals("Eau")) && FixtureA.getBody().getType() == BodyType.DynamicBody) {
					for(Obstacle obstacle : lecteurCarte.obstacles){
			    		if(obstacle.body.getFixtureList().get(0) == FixtureB){
			    			Eau eau = (Eau) obstacle;
			    			eau.buoyancyController.removeBody(FixtureA);
			    		}
			    	}
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Body a = contact.getFixtureA().getBody();
			    Body b = contact.getFixtureB().getBody();
			    
			    if((a.getUserData() != null && a.getUserData().equals("Objet")) && (b.getUserData() != null && b.getUserData().equals("Objet"))) {
			    	contact.setEnabled(false);
				}
			    if((a.getUserData() != null && a.getUserData().equals("Balle")) || (b.getUserData() != null && b.getUserData().equals("Balle"))) {
			    	lecteurCarte.balle.setDirectionSaut(oldManifold.getLocalNormal());
			    }
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
		});
	}
	
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
	}

}
