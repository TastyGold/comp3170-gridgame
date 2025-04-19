package gridgame.sceneobjects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import static org.lwjgl.opengl.GL33.glDrawElementsInstanced;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class GridTerrain extends SceneObject {
	
	// shaders
	private static final String VERTEX_SHADER = "grid_terrain_vertex.glsl";
	private static final String FRAGMENT_SHADER = "simple_fragment.glsl";

	private Shader shader;
	
	// buffers
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f[] positions;
	private int positionBuffer;
	private float[] tiles;
	private int tileBuffer;
	
	private float[] color = { 0.55f, 0.5f, 0.6f }; // purplish
	
	public GridTerrain(int startX, int startY, int endX, int endY) {
		 // compile shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// initialise vertex buffer
		vertices = new Vector4f[] {
				new Vector4f(0,0,0,1),
				new Vector4f(1,0,0,1),
				new Vector4f(1,1,0,1),
				new Vector4f(0,1,0,1),
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// initialise index buffer
		indices = new int[] {
				0,1,2,
				0,2,3
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);

		// initialise instance data
		positions = new Vector3f[(endX - startX + 1) * (endY - startY + 1)];
		tiles = new float[(endX - startX + 1) * (endY - startY + 1)];
		
		int i = 0;
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				positions[i] = new Vector3f(x, y, 0);
				tiles[i] = Math.max(Math.abs(x + 0.5f), Math.abs(y + 0.5f)) > 12 ? 1 : 0;
				i++;
			}
		}

		positionBuffer = GLBuffers.createBuffer(positions);
		tileBuffer = GLBuffers.createBuffer(tiles, GL_FLOAT);
	}

	public void setColors(Color color) {
		color.getRGBColorComponents(this.color);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		// pass the mesh in once
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_color", color);
		
		// configure rendering
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT, GL_FILL);
		
		// pass data for every instance as attributes
		shader.setAttribute("a_worldPos", positionBuffer);
		shader.setAttribute("a_tilePresence", tileBuffer);

		// tell OpenGL these attributes are instanced
		glVertexAttribDivisor(shader.getAttribute("a_worldPos"), 1);
		glVertexAttribDivisor(shader.getAttribute("a_tilePresence"), 1);
		
		// draw all the instances at once
		glDrawElementsInstanced(
		 GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0, positions.length);
		
		// unset instanced modifiers, "OpenGL is a statemachine"
		glVertexAttribDivisor(shader.getAttribute("a_worldPos"), 0);
		glVertexAttribDivisor(shader.getAttribute("a_tilePresence"), 0);
	}

}
