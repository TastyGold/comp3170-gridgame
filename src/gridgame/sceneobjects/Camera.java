package gridgame.sceneobjects;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import comp3170.SceneObject;

public class Camera extends SceneObject{
	
	// config
	private final static float CAMERA_NEAR = 1f;
	private final static float CAMERA_FAR = 10f;
	
	private float cameraFov;
	
	// matrices
	private Matrix4f viewMatrix = new Matrix4f(); // camera matrices
	private Matrix4f projectionMatrix = new Matrix4f();
	
	private Matrix4f mtwMatrix = new Matrix4f(); // matrix for model to world conversion
	
	// camera view bounds in world units
	private Vector2f cameraViewWorldSize = new Vector2f(); 
	
	public Matrix4f getViewMatrix(Matrix4f dest) {	
		getModelToWorldMatrix(mtwMatrix);
		mtwMatrix.normalize3x3(viewMatrix); // remove scale components from camera in scene graph
		
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.set(projectionMatrix);
	}
	
	public Vector2f getCameraWorldViewSize() {
		return cameraViewWorldSize;
	}
	
	public void resize(float screenWidth, float screenHeight) {

		// scaling based on screen height
		float aspect = screenHeight / screenWidth;
		
		float width = cameraFov / aspect;
		float height = cameraFov;
		
		// update camera world bounds size
		cameraViewWorldSize.set(width, height);

		//update projection matrix
		float left = -width / 2;
		float right = width / 2;
		float bottom = -height / 2;
		float top = height / 2;
		projectionMatrix.setOrtho(left, right, bottom, top, CAMERA_NEAR, CAMERA_FAR);
		
	}

	public Camera(float fov) { // initialize camera with starting bound size
		cameraFov = fov;
	}
}
