package gridgame;

import comp3170.InputManager;
import comp3170.SceneObject;
import gridgame.sceneobjects.Axes;
import gridgame.sceneobjects.Camera;
import gridgame.sceneobjects.Grid;
import gridgame.sceneobjects.GridTerrain;
import gridgame.sceneobjects.Player;

public class Scene extends SceneObject{
	
	public static Scene theScene;
	
	private Player player;
	
	private Camera mainCamera;
	
	public Scene()
	{
		theScene = this;
		player = new Player();
		player.setParent(this);
		
		Grid grid = new Grid(-15,-15,15,15);
		grid.setParent(this);
		
		GridTerrain terrain = new GridTerrain(-15, -15, 14, 14);
		terrain.setParent(grid);

		Axes axes = new Axes();
		axes.setParent(this);
		
		mainCamera = new Camera(32);
		mainCamera.setParent(this);
		mainCamera.getMatrix().translation(0,0,5);
	}

	public void update(InputManager input, float deltaTime) {
		
	}
	
	public Camera getCamera() {
		return mainCamera;
	}
}