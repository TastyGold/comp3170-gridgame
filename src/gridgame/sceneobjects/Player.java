package gridgame.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Player extends SceneObject {
	
	// shaders
	private static final String VERTEX_SHADER = "simple_vertex.glsl";
	private static final String FRAGMENT_SHADER = "simple_fragment.glsl";

	private Shader shader;
	
	// buffers
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	
	private float[] color = { 1.0f, 0.0f, 0.0f };

	public Player() {

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER); // compile shader
		
		// initialise vertex buffer
		float w = PlayerController.width / 2;
		float h = PlayerController.height / 2;

		vertices = new Vector4f[] {
				new Vector4f(w,h,0,1),
				new Vector4f(-w,h,0,1),
				new Vector4f(-w,-h,0,1),
				new Vector4f(w,-h,0,1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// initilase index buffer
		indices = new int[] {
				0, 1, 2,
				0, 2, 3
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void setPosition(Vector2f position) {
		getMatrix().translation(position.x, position.y, 0);
	}

	public void setColor(Color colour) {
		colour.getRGBColorComponents(this.color);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_color", color);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
