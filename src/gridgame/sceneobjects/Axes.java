package gridgame.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Axes extends SceneObject {
	
	// borrowed from week 6 prac for debugging purposes
	
	static final private String VERTEX_SHADER = "simple_vertex.glsl";
	static final private String FRAGMENT_SHADER = "simple_fragment.glsl";
	private Shader shader;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBufferX;
	private int indexBufferY;

	private final Vector3f RED = new Vector3f(1, 0, 0);
	private final Vector3f GREEN = new Vector3f(0, 1, 0);

	public Axes() {	
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// A set of i,j axes
		vertices = new Vector4f[] {
		//@formatter:off
			new Vector4f(0,0,0,1),
			new Vector4f(1,0,0,1),
			new Vector4f(0,1,0,1),
		//@formatter:on
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);

		indexBufferX = GLBuffers.createIndexBuffer(new int[] {0,1});		
		indexBufferY = GLBuffers.createIndexBuffer(new int[] {0,2});		
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);

		// X axis in red
		shader.setUniform("u_colour", RED);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferX);
		glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);

		// Y axis in green
		shader.setUniform("u_colour", GREEN);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferY);
		glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);
	}
}