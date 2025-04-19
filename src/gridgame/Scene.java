package gridgame;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import gridgame.sceneobjects.Axes;
import gridgame.sceneobjects.Camera;
import gridgame.sceneobjects.Grid;
import gridgame.sceneobjects.GridTerrain;
import gridgame.sceneobjects.Player;
import gridgame.sceneobjects.PlayerController;

public class Scene extends SceneObject{
	
	public static Scene theScene;
	
	private Camera mainCamera;
	private PlayerController player;
	
	public Scene()
	{
		theScene = this;
		
		Grid grid = new Grid(-15,-15,15,15);
		grid.setParent(this);
		
		GridTerrain terrain = new GridTerrain(-15, -15, 14, 14);
		terrain.setParent(grid);
		
		terrain.setRect(-7, -5, 6, -3, 1);
		terrain.setRect(1, 0, 1, 3, 1);
		terrain.setRect(6, -2, 10, -1, 1);
		terrain.setRect(-3, 1, -3, 3, 1);
		terrain.setRect(-4, 2, -2, 2, 1);
		terrain.updateTileBuffers();
		
		// iso mode
		//grid.getMatrix().set(new Matrix4f(1,-0.5f,0,0, 1,0.5f,0,0, 0,0,1,0, 0,0,0,1));

		Axes axes = new Axes();
		axes.setParent(this);
		

		Player playerMesh = new Player();
		player = new PlayerController(playerMesh, terrain);
		playerMesh.setParent(grid);
		
		mainCamera = new Camera(15);
		mainCamera.setParent(playerMesh);
		mainCamera.getMatrix().translation(0,0,5);
	}

	public void update(InputManager input, float deltaTime) {
		player.update(input, deltaTime);
	}
	
	public Camera getCamera() {
		return mainCamera;
	}
}