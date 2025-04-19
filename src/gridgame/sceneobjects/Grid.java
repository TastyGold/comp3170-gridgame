package gridgame.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Grid extends SceneObject {
	
	// borrowed from week 6 prac for debugging purposes
	
	static final private String VERTEX_SHADER = "simple_vertex.glsl";
	static final private String FRAGMENT_SHADER = "simple_fragment.glsl";
	private Shader shader;
	
	private Vector4f[] vertices;
	private int vertexBuffer;

	private Vector3f gridColor = new Vector3f(0.47f,0.45f,0.5f);

	public Grid(int startX, int startY, int endX, int endY) {	
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		vertices = new Vector4f[(endX - startX + endY - startY + 2) * 2];
		
		int k = 0;
		for (int i = startX; i <= endX; i++) {
			vertices[k++] = new Vector4f(i, startY, 0, 1);
			vertices[k++] = new Vector4f(i, endY, 0, 1);
		}
		for (int i = startY; i <= endY; i++) {
			vertices[k++] = new Vector4f(startX, i, 0, 1);
			vertices[k++] = new Vector4f(endX, i, 0, 1);
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);	
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);

		// X axis in red
		shader.setUniform("u_color", gridColor);
		glDrawArrays(GL_LINES, 0, vertices.length);
	}
}