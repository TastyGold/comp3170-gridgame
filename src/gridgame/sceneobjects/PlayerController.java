package gridgame.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import comp3170.InputManager;

public class PlayerController {
	public static final float width = 0.8f;
	public static final float height = 1.2f;
	private static final float halfWidth = width / 2;
	private static final float halfHeight = height / 2;
	
	private Player mesh;
	private GridTerrain terrain;
	
	private Vector2f position = new Vector2f();
	private Vector2f velocity = new Vector2f();
	private Vector2f temp = new Vector2f();
	
	private static final float moveSpeed = 5f;
	private static final float gravity = 150f;
	private static final float jumpVelocity = 30f;
	private static final float terminalVelocity = 40f;

	public PlayerController(Player mesh, GridTerrain terrain) {
		this.mesh = mesh;
		this.terrain = terrain;
	}
	
	public void update(InputManager input, float deltaTime) {

		// rotate turret on KEY_LEFT or KEY_RIGHT
		velocity.set(0, 0);
		if (input.isKeyDown(GLFW_KEY_A)) {
			velocity.x = -moveSpeed;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			velocity.x = moveSpeed;
		}
		if (input.isKeyDown(GLFW_KEY_S)) { 
			velocity.y = -moveSpeed;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			velocity.y = moveSpeed;
		}
		
//		velocity.y -= gravity * deltaTime;
//		velocity.y = Math.max(-terminalVelocity, velocity.y);
//		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
//			velocity.y = jumpVelocity;
//		}
//		
		temp.set(velocity);
		temp.mul(deltaTime);

		movePlayer(temp, terrain);
		
		mesh.setPosition(position);
	}
	
	private void movePlayer(Vector2f delta, GridTerrain terrain) {
		
		int minX = (int) Math.floor(position.x + delta.x - halfWidth);
		int minY = (int) Math.floor(position.y + delta.y - halfHeight);

		int maxX = (int) Math.ceil(position.x + delta.x + halfWidth);
		int maxY = (int) Math.ceil(position.y + delta.y + halfHeight);
		
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				if (terrain.getTile(x, y) > 0 && intersectsTile(x, y)) {
					System.out.println("colliding");
				}
			}
		}
		
		position.add(delta);
	}
	
	private void 
	
	private boolean intersectsTile(int x, int y) {
		return !(position.x - halfWidth > x + 1
			|| position.y - halfHeight > y + 1
			|| position.x + halfWidth < x
			|| position.y + halfHeight < y);
	}
}
