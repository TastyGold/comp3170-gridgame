package gridgame.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import comp3170.InputManager;

public class PlayerController {
	public static final float width = 0.8f;
	public static final float height = 1.2f;
	private static final float halfWidth = width / 2;
	private static final float halfHeight = height / 2;
	private static final float minTileDistX = halfWidth + 0.5f;
	private static final float minTileDistY = halfHeight + 0.5f;
	
	private Player mesh;
	private GridTerrain terrain;
	
	private Vector2f position = new Vector2f();
	private Vector2f velocity = new Vector2f();
	private Vector2f temp = new Vector2f();
	
	private static final float moveSpeed = 9f;
	private static final float gravity = 75f;
	private static final float jumpVelocity = 18f;
	private static final float terminalVelocity = 40f;

	public PlayerController(Player mesh, GridTerrain terrain) {
		this.mesh = mesh;
		this.terrain = terrain;
	}
	
	public void update(InputManager input, float deltaTime) {

		// rotate turret on KEY_LEFT or KEY_RIGHT
		velocity.set(0, velocity.y);
		if (input.isKeyDown(GLFW_KEY_A)) {
			velocity.x = -moveSpeed;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			velocity.x = moveSpeed;
		}
//		if (input.isKeyDown(GLFW_KEY_S)) { 
//			velocity.y = -moveSpeed;
//		}
//		if (input.isKeyDown(GLFW_KEY_W)) {
//			velocity.y = moveSpeed;
//		}
		
		velocity.y -= gravity * deltaTime;
		velocity.y = Math.max(-terminalVelocity, velocity.y);
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			velocity.y = jumpVelocity;
		}
		
		temp.set(velocity);
		temp.mul(deltaTime);

		movePlayer(temp, terrain);
		
		mesh.setPosition(position);
	}
	
	private Vector2f resolveVector = new Vector2f();
	private void movePlayer(Vector2f delta, GridTerrain terrain) {
		
		int minX = (int) Math.floor(position.x + delta.x - halfWidth);
		int minY = (int) Math.floor(position.y + delta.y - halfHeight);

		int maxX = (int) Math.ceil(position.x + delta.x + halfWidth);
		int maxY = (int) Math.ceil(position.y + delta.y + halfHeight);
		
		position.add(delta);
		
		resolveVector.set(0, 0);
		for (int x = maxX; x >= minX; x--) {
			for (int y = maxY; y >= minY; y--) {
				if (terrain.getTile(x, y) > 0 && intersectsTile(x, y)) {
					resolveTileIntersection(x, y, position, terrain);
				}
			}
		}
		
		//position.add(resolveVector);
	}
	
	private void resolveTileIntersection(int x, int y, Vector2f dest, GridTerrain terrain) {
		float tileCenterX = x + 0.5f;
		float tileCenterY = y + 0.5f;
		
		float dx = position.x - tileCenterX;
		float dy = position.y - tileCenterY;
		
		boolean priorityX = Math.abs(dx) > Math.abs(dy);

		if (priorityX) {
			float correction = getTileCorrection(dx, minTileDistX, x, y, 1, 0);
			
			dest.x += correction;
			
			if (correction > 0) velocity.x = Math.max(0, velocity.x);
			if (correction < 0) velocity.x = Math.min(0, velocity.x);
		}
		else {
			float correction = getTileCorrection(dy, minTileDistY, x, y, 0, 1);

			dest.y += correction;
			
			if (correction > 0) velocity.y = Math.max(0, velocity.y);
			if (correction < 0) velocity.y = Math.min(0, velocity.y);
		}
	}
	
	private float getTileCorrection(float dv, float minDv, int x, int y, int xOffset, int yOffset) {

		float correction = 0;
		if (dv < 0 && dv > -minDv && terrain.getTile(x - xOffset, y - yOffset) <= 0) {
			correction = -minDv - dv;
		}
		if (dv >= 0 && dv < minDv && terrain.getTile(x + xOffset, y + yOffset) <= 0) {
			correction = minDv - dv;
		}
		return correction;
	}
	
	private boolean intersectsTile(int x, int y) {
		return !(position.x - halfWidth > x + 1
			|| position.y - halfHeight > y + 1
			|| position.x + halfWidth < x
			|| position.y + halfHeight < y);
	}
}
