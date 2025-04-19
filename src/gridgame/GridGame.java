package gridgame;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;
import gridgame.sceneobjects.Camera;

public class GridGame implements IWindowListener {
	
	// COMP3170 A1 FINAL SUBMISSION 47863021

	// config
	final private File DIRECTORY = new File("src/gridgame/shaders");
	
	private int screenWidth = 1600;
	private int screenHeight = 1200;
	
	private Vector4f backgroundColor = new Vector4f(0.075f,0.06f,0.13f, 1.0f); // Dark purple
	
	// references
	public static GridGame instance;
	
	private Window window;
	private Scene scene;
	
	private InputManager input;

	// runtime variables
	private long oldTime;

	// camera matrices
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	// class constructor
	public GridGame() throws OpenGLException {
		instance = this;
		window = new Window("GridGame", screenWidth, screenHeight, this);
		window.setResizable(true);
		window.run();
	}

	@Override
	public void init() {
		// initialise shader
		new ShaderLibrary(DIRECTORY);
		
		// initalise scene and input and deltaTime
		scene = new Scene();
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
		
		//resize(screenWidth, screenHeight);

		// set background color (desert sand)
		glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
	}

	@Override
	public void draw() {	
		// calculate delta time
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		// update scene
		scene.update(input, deltaTime);
		
		// reset screen for drawing
		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);

		// get current camera from scene
		Camera targetCamera = scene.getCamera();
		if (targetCamera == null) {
			System.err.println("Target camera is null!");
			return;
		}
		
		// calcalate mvp matrix from view and proj matrices obtained from camera
		targetCamera.getViewMatrix(viewMatrix);
		targetCamera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		scene.draw(mvpMatrix);
		
		input.clear();
	}
	
	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		// tell cameras to update their bounds data
		scene.getCamera().resize(width, height);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args) throws OpenGLException {
		new GridGame();
	}
}
