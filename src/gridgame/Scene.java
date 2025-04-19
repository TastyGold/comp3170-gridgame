package gridgame;

import comp3170.InputManager;
import comp3170.SceneObject;
import gridgame.sceneobjects.Axes;
import gridgame.sceneobjects.Camera;
import gridgame.sceneobjects.Tank;

public class Scene extends SceneObject{
	
	public static Scene theScene;
	
	private Tank tank;
	
	private Camera mainCamera;
	
	private Axes axes;
	
	public Scene()
	{
		theScene = this;
		tank = new Tank();
		tank.setParent(this);
		
		mainCamera = new Camera(5);
		mainCamera.setParent(this);
		mainCamera.getMatrix().translation(0,0,5);
		
		axes = new Axes();
		axes.setParent(this);
	}

	public void update(InputManager input, float deltaTime) {
		
	}
	
	public Camera getCamera() {
		return mainCamera;
	}
}