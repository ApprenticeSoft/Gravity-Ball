package Box2DUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BuoyancyController {

	private Fixture fluidSensor;
	private List<Vector2> fluidVertices;
	private Set<Fixture> fixtures;
	private World world;

	public boolean isFluidFixed = true;
	public float fluidDrag = 0.25f;
	public float fluidLift = 0.25f;
	public float linearDrag = 0;
	public float maxFluidDrag = 2000;
	public float maxFluidLift = 500;

	public BuoyancyController(World world, Fixture fluidSensor) {
		this.world = world;
		this.fluidSensor = fluidSensor;
		fluidVertices = getFixtureVertices(fluidSensor);

		fixtures = new HashSet<Fixture>();
	}

	public void step() {
		for (Fixture fixture : fixtures) {
			if (fixture.getBody().isAwake()) {

				/* Create clipPolygon */
				List<Vector2> clipPolygon = getFixtureVertices(fixture);

				/* Create subjectPolygon */
				List<Vector2> subjectPolygon;
				if (isFluidFixed) {
					subjectPolygon = fluidVertices;
				} else {
					subjectPolygon = getFixtureVertices(fluidSensor);
				}

				/* Get intersection polygon */
				List<Vector2> clippedPolygon = PolygonIntersector.clipPolygons(
						subjectPolygon, clipPolygon);

				if (!clippedPolygon.isEmpty()) {
					applyForces(fixture, clippedPolygon.toArray(new Vector2[0]));
				}
			}
		}
	}

	private void applyForces(Fixture fixture, Vector2[] clippedPolygon) {
		PolygonProperties polygonProperties = PolygonIntersector
				.computePolygonProperties(clippedPolygon);

		/* Get fixtures bodies */
		Body fixtureBody = fixture.getBody();
		Body fluidBody = fluidSensor.getBody();

		/* Get fluid density */
		float density = fluidSensor.getDensity();

		/* Apply buoyancy force */
		float displacedMass = fluidSensor.getDensity()
				* polygonProperties.getArea();
		Vector2 gravity = world.getGravity();
		Vector2 buoyancyForce = new Vector2(-gravity.x * displacedMass,
				-gravity.y * displacedMass);
		fixtureBody.applyForce(buoyancyForce, polygonProperties.getCentroid(),
				true);

		/* Linear drag force */
		if (linearDrag != 0) {
			fixtureBody.applyForce(gravity.rotate90(1).nor().scl(linearDrag),
					polygonProperties.getCentroid(), true);
		}

		/* Apply drag and lift forces */
		int polygonVertices = clippedPolygon.length;
		for (int i = 0; i < polygonVertices; i++) {

			/* Apply drag force */

			/* End points and mid point of the edge */
			Vector2 firstPoint = clippedPolygon[i];
			Vector2 secondPoint = clippedPolygon[(i + 1) % polygonVertices];
			Vector2 midPoint = firstPoint.cpy().add(secondPoint).scl(0.5f);

			/*
			 * Find relative velocity between the object and the fluid at edge
			 * mid point.
			 */
			Vector2 velocityDirection = new Vector2(fixtureBody
					.getLinearVelocityFromWorldPoint(midPoint)
					.sub(fluidBody.getLinearVelocityFromWorldPoint(midPoint)));
			float velocity = velocityDirection.len();
			velocityDirection.nor();

			Vector2 edge = secondPoint.cpy().sub(firstPoint);
			float edgeLength = edge.len();
			edge.nor();

			Vector2 normal = new Vector2(edge.y, -edge.x);
			float dragDot = normal.dot(velocityDirection);

			if (dragDot >= 0) {

				/*
				 * Normal don't point backwards. This is a leading edge. Store
				 * the result of multiply edgeLength, density and velocity
				 * squared
				 */
				float tempProduct = edgeLength * density * velocity * velocity;

				float drag = dragDot * fluidDrag * tempProduct;
				drag = Math.min(drag, maxFluidDrag);
				Vector2 dragForce = velocityDirection.cpy().scl(-drag);
				fixtureBody.applyForce(dragForce, midPoint, true);

				/* Apply lift force */
				float liftDot = edge.dot(velocityDirection);
				float lift = dragDot * liftDot * fluidLift * tempProduct;
				lift = Math.min(lift, maxFluidLift);
				Vector2 liftDirection = new Vector2(-velocityDirection.y,
						velocityDirection.x);
				Vector2 liftForce = liftDirection.scl(lift);
				fixtureBody.applyForce(liftForce, midPoint, true);
			}
		}
	}

	public void addBody(Fixture fixture) {
		try {
			PolygonShape polygon = (PolygonShape) fixture.getShape();
			if (polygon.getVertexCount() > 2) {
				fixtures.add(fixture);
			}
		} catch (ClassCastException e) {
			Gdx.app.debug("BuoyancyController",
					"Fixture shape is not an instance of PolygonShape.");
		}
	}

	public void removeBody(Fixture fixture) {
		fixtures.remove(fixture);
	}

	private List<Vector2> getFixtureVertices(Fixture fixture) {
		PolygonShape polygon = (PolygonShape) fixture.getShape();
		int verticesCount = polygon.getVertexCount();

		List<Vector2> vertices = new ArrayList<Vector2>(verticesCount);
		for (int i = 0; i < verticesCount; i++) {
			Vector2 vertex = new Vector2();
			polygon.getVertex(i, vertex);
			vertices.add(new Vector2(fixture.getBody().getWorldPoint(vertex)));
		}

		return vertices;
	}
}
