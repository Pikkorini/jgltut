package rosick.mckesson.II.tut04;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import rosick.GLWindow;
import rosick.jglsdk.framework.Framework;


/**
 * Visit https://github.com/rosickteam/OpenGL for project info, updates and license terms.
 * 
 * II. Positioning
 * 4. Objects at Rest 
 * http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2004.html
 * @author integeruser
 */
public class ShaderPerspective02 extends GLWindow {
	
	public static void main(String[] args) {		
		new ShaderPerspective02().start();
	}
	
	
	private static final String BASEPATH = "/rosick/mckesson/II/tut04/data/";

	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private final float vertexData[] = {
			 0.25f,  0.25f, -1.25f, 1.0f,
			 0.25f, -0.25f, -1.25f, 1.0f,
			-0.25f,  0.25f, -1.25f, 1.0f,
	
			 0.25f, -0.25f, -1.25f, 1.0f,
			-0.25f, -0.25f, -1.25f, 1.0f,
			-0.25f,  0.25f, -1.25f, 1.0f,
	
			 0.25f,  0.25f, -2.75f, 1.0f,
			-0.25f,  0.25f, -2.75f, 1.0f,
			 0.25f, -0.25f, -2.75f, 1.0f,
	
			 0.25f, -0.25f, -2.75f, 1.0f,
			-0.25f,  0.25f, -2.75f, 1.0f,
			-0.25f, -0.25f, -2.75f, 1.0f,
	
			-0.25f,  0.25f, -1.25f, 1.0f,
			-0.25f, -0.25f, -1.25f, 1.0f,
			-0.25f, -0.25f, -2.75f, 1.0f,
	
			-0.25f,  0.25f, -1.25f, 1.0f,
			-0.25f, -0.25f, -2.75f, 1.0f,
			-0.25f,  0.25f, -2.75f, 1.0f,
	
			 0.25f,  0.25f, -1.25f, 1.0f,
			 0.25f, -0.25f, -2.75f, 1.0f,
			 0.25f, -0.25f, -1.25f, 1.0f,
	
			 0.25f,  0.25f, -1.25f, 1.0f,
			 0.25f,  0.25f, -2.75f, 1.0f,
			 0.25f, -0.25f, -2.75f, 1.0f,
	
			 0.25f,  0.25f, -2.75f, 1.0f,
			 0.25f,  0.25f, -1.25f, 1.0f,
			-0.25f,  0.25f, -1.25f, 1.0f,
	
			 0.25f,  0.25f, -2.75f, 1.0f,
			-0.25f,  0.25f, -1.25f, 1.0f,
			-0.25f,  0.25f, -2.75f, 1.0f,
	
			 0.25f, -0.25f, -2.75f, 1.0f,
			-0.25f, -0.25f, -1.25f, 1.0f,
			 0.25f, -0.25f, -1.25f, 1.0f,
	
			 0.25f, -0.25f, -2.75f, 1.0f,
			-0.25f, -0.25f, -2.75f, 1.0f,
			-0.25f, -0.25f, -1.25f, 1.0f,
	
	
	
	
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
	
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
	
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
	
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
	
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
	
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
	
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
	
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
	
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
	
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
	
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
	
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
	};
	
	private int theProgram;
	private int offsetUniform;
	private int vertexBufferObject;
	private int vao;
	
	private int frustumScaleUnif;
	private int zNearUnif, zFarUnif;
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private void initializeProgram() {			
		ArrayList<Integer> shaderList = new ArrayList<>();
		shaderList.add(Framework.loadShader(GL_VERTEX_SHADER, 	BASEPATH + "ManualPerspective.vert"));
		shaderList.add(Framework.loadShader(GL_FRAGMENT_SHADER, BASEPATH + "StandardColors.frag"));

		theProgram = Framework.createProgram(shaderList);
		
		offsetUniform = glGetUniformLocation(theProgram, "offset");
		
		frustumScaleUnif = glGetUniformLocation(theProgram, "frustumScale");
		zNearUnif = glGetUniformLocation(theProgram, "zNear");
		zFarUnif = glGetUniformLocation(theProgram, "zFar");
		
		glUseProgram(theProgram);
		glUniform1f(frustumScaleUnif, 1.0f);
		glUniform1f(zNearUnif, 1.0f);
		glUniform1f(zFarUnif, 3.0f);
		glUseProgram(0);
	}
	
	private void initializeVertexBuffer() {
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexDataBuffer.put(vertexData);
		vertexDataBuffer.flip();
		
        vertexBufferObject = glGenBuffers();	       
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
	    glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	@Override
	protected void init() {
		initializeProgram();
		initializeVertexBuffer(); 

		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		glEnable(GL_CULL_FACE);
	    glCullFace(GL_BACK);
	    glFrontFace(GL_CW);
	}
	
	
	@Override
	protected void display() {		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		glUseProgram(theProgram);

		glUniform2f(offsetUniform, 0.5f, 0.5f);

		int colorData = vertexData.length * 2;
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorData);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * 12);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glUseProgram(0);
	}
}