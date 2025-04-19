package gridgame.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import comp3170.InputManager;

public class PlayerController {
	public static final float width = 0.8f;
	public static final float height = 1.2f;
	
	private Player mesh;
	private GridTerrain terrain;
	
	private Vector2f position = new Vector2f();
	private Vector2f velocity = new Vector2f();
	private Vector2f temp = new Vector2f();
	
	private static final float moveSpeed = 15f;
	private static final float gravity = 150f;
	private static final float jumpVelocity = 30f;
	private static final float terminalVelocity = 40f;

	public PlayerController(Player mesh, GridTerrain terrain) {
		this.mesh = mesh;
	}
	
	public void update(InputManager input, float deltaTime) {

		// rotate turret on KEY_LEFT or KEY_RIGHT
		if (input.isKeyDown(GLFW_KEY_A)) {
			position.x -= moveSpeed * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			position.x += moveSpeed * deltaTime;
		}
//		if (input.isKeyDown(GLFW_KEY_S)) { 
//			position.y -= moveSpeed * deltaTime;
//		}
//		if (input.isKeyDown(GLFW_KEY_W)) {
//			position.y += moveSpeed * deltaTime;
//		}
		
		velocity.y -= gravity * deltaTime;
		velocity.y = Math.max(-terminalVelocity, velocity.y);
		if (input.wasKeyPressed(GLFW_KEY_SPACE)) {
			velocity.y = jumpVelocity;
		}
		
		temp.set(velocity);
		temp.mul(deltaTime);
		position.add(temp);
		
		mesh.setPosition(position);
	}
	
	public boolean isCollidingTerrain(GridTerrain terrain) {
		return false;
	}
}
