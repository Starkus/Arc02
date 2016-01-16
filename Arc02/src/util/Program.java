package util;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

import org.mariani.matrix.Mat4;
import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;
import org.mariani.vector.Vec4;

public class Program {
	
	static final boolean DEBUG = false;
	
	public static Program current = null;
	
	public static HashMap<String, Integer> SHADERS = new HashMap<String, Integer>();
	public HashMap<String, Integer> attributes = new HashMap<String, Integer>();
	
	public FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	
	private int program = -1;
	//private String vertexShaderName = null;
	private int vertexShaderIndex = -1;
	//private String fragmentShaderName = null;
	private int fragmentShaderIndex = -1;
	//private int geometricShader = -1;
	
	private int attributeIndex = -1;
	
	private HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	

	public Program() {
		program = GL20.glCreateProgram();
	}
	
	public void addShader(String filename) {
		ByteBuffer byte_buffer = null;
		int shader = 0;
		
		if (SHADERS.containsKey(filename)) {
			shader = SHADERS.get(filename);
			GL20.glAttachShader(program, shader);
			return;
		
		} else {
			try {
				
				String[] groups = filename.split("\\.");
				
				String type = groups[groups.length - 1];
				
				RandomAccessFile aFile = new RandomAccessFile(filename, "r");
				FileChannel inChannel = aFile.getChannel();
				byte_buffer = ByteBuffer.allocateDirect((int) inChannel.size());
				inChannel.read(byte_buffer);
				
				byte_buffer.flip();
				
				inChannel.close();
				aFile.close();
				
				if (type.equals("vert")) {
					if (compileVertexShader(byte_buffer)) {
						GL20.glAttachShader(program, vertexShaderIndex);
						SHADERS.put(filename, vertexShaderIndex);
					
					} else {
						System.out.println("******** Start vertex info log ********");
						printInformationLog(vertexShaderIndex);
						System.out.println("********* End vertex info log *********");
					}
				}
				
				if (type.equals("frag")) {
					if (compileFragmentShader(byte_buffer)) {
						GL20.glAttachShader(program, fragmentShaderIndex);
						SHADERS.put(filename, fragmentShaderIndex);
					
					} else {
						System.out.println("******** Start vertex info log ********");
						printInformationLog(fragmentShaderIndex);
						System.out.println("********* End vertex info log *********");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public boolean compileVertexShader(ByteBuffer b) {
		vertexShaderIndex = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShaderIndex, b);
		GL20.glCompileShader(vertexShaderIndex);
		
		return (GL20.glGetShaderi(vertexShaderIndex, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE);
	}
	
	public boolean compileFragmentShader(ByteBuffer b) {
		fragmentShaderIndex = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShaderIndex, b);
		GL20.glCompileShader(fragmentShaderIndex);
		
		return (GL20.glGetShaderi(fragmentShaderIndex, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE);
	}
	
	public void printInformationLog(int shader) {
        IntBuffer length = BufferUtils.createIntBuffer(1);
        GL20.glGetShader(shader, GL20.GL_INFO_LOG_LENGTH, length);
        ByteBuffer log = BufferUtils.createByteBuffer(length.get(0));
        GL20.glGetShaderInfoLog(shader, length, log);
        for (int i = 0; i < log.capacity(); i++) {
            System.out.print((char) log.get(i));
        }
    }

    public void linkProgram() {

        if (Program.DEBUG) System.out.println(getVersion());

        GL20.glLinkProgram(program);

        IntBuffer ib = BufferUtils.createIntBuffer(1000);

        ib.position(0);

        GL20.glGetProgram(program, GL20.GL_LINK_STATUS, ib);

        if (Program.DEBUG) System.out.println("Program link status " + ib.get(0));

        if (ib.get(0) == 0) {
            System.out.println("(!) Link unsuccessful");
        }
    }
    
    public void bind() {
        GL20.glUseProgram(program);
        Program.current = this;

    }
    public void unbind() {
        GL20.glUseProgram(0);
        Program.current = null;
    }

    /**
	 * Define the uniform variable name to be used in the shader.
	 * @param name
	 */
    public void defineUniform(String name) {
        uniforms.put(name, GL20.glGetUniformLocation(program, name));
    }


    /**
	 * Set the integer value associated with the uniform variable name.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, int i) {
        GL20.glUniform1i(uniforms.get(name).intValue(), i);
    }

    /**
	 * Set the float value associated with the uniform variable name.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, float i) {
        GL20.glUniform1f(uniforms.get(name).intValue(), i);
    }

    /**
	 * Set the int values associated with the uniform variable name.
	 * This is used to set a shader variable of type vec2.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, int a, int b) {
        GL20.glUniform2i(uniforms.get(name).intValue(), a, b);
    }

    /**
	 * Set the float values associated with the uniform variable name.
	 * This is used to set a shader variable of type vec2.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, float a, float b) {
        GL20.glUniform2f(uniforms.get(name).intValue(), a, b);
    }

    /**
	 * Set the int values associated with the uniform variable name.
	 * This is used to set a shader variable of type vec3.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, int a, int b, int c) {
        GL20.glUniform3i(uniforms.get(name).intValue(), a, b, c);
    }

    /**
	 * Set the float values associated with the uniform variable name.
	 * This is used to set a shader variable of type vec3.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, float a, float b, float c) {
        GL20.glUniform3f(uniforms.get(name).intValue(), a, b, c);
    }

    /**
	 * Set the int values associated with the uniform variable name.
	 * This is used to set a shader variable of type vec4.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */
    public void setUniform(String name, int a, int b, int c, int d) {
        GL20.glUniform4i(uniforms.get(name).intValue(), a, b, c, d);
    }

    /**
     * Set the float values associated with the uniform variable name.
     * This is used to set a shader variable of type vec4.
     *
     * @param name of the uniform variable.
     * @param The value to set the uniform variable.
     */
    public void setUniform(String name, float a, float b, float c, float d) {
        GL20.glUniform4f(uniforms.get(name).intValue(), a, b, c, d);
    }

    /**
	 * Set the float values associated with the uniform variable name.
	 * This is used to set a shader variable of type mat4.
	 *
	 * @param name of the uniform variable.
	 * @param The value to set the uniform variable.
	 */


    public void setUniform(String name, boolean transpose, Matrix4f matrix) {

        buffer.position(0);
        buffer.limit(16);
        matrix.store(buffer);
        buffer.position(0);

       GL20.glUniformMatrix4(uniforms.get(name).intValue(),transpose,buffer);

    }

    public void setUniform(String name, boolean transpose, Matrix3f matrix) {

        buffer.position(0);
        buffer.limit(9);
        matrix.store(buffer);
        buffer.position(0);

       GL20.glUniformMatrix3(uniforms.get(name).intValue(),transpose,buffer);

    }

    public void setUniform(String name, boolean transpose, Matrix2f matrix) {

        buffer.position(0);
        buffer.limit(4);
        matrix.store(buffer);
        buffer.position(0);

       GL20.glUniformMatrix4(uniforms.get(name).intValue(),transpose,buffer);

    }

   public void setUniform(String name, boolean transpose, FloatBuffer buffer) {

        buffer.position(0);

    switch(buffer.capacity())
    {
        case(4):
       GL20.glUniformMatrix2(uniforms.get(name).intValue(),transpose,buffer);
       break;
       case(9):
       GL20.glUniformMatrix3(uniforms.get(name).intValue(),transpose,buffer);
       break;
        case(16):
       GL20.glUniformMatrix4(uniforms.get(name).intValue(),transpose,buffer);
       break;
    }
    }


   public void setUniform(String name, Vec2 vec) {
	   
	   GL20.glUniform2f(uniforms.get(name).intValue(), vec.x, vec.y);
   }

   public void setUniform(String name, Vec3 vec) {

	   GL20.glUniform3f(uniforms.get(name).intValue(), vec.x, vec.y, vec.z);
		   
   }

   public void setUniform(String name, Vec4 vec) {

	   GL20.glUniform4f(uniforms.get(name).intValue(), vec.x, vec.y, vec.z, vec.w);
		   
   }

   public void setUniform(String name, boolean transpose, Mat4 matrix) {

       buffer.position(0);
       buffer.limit(16);

       buffer.put(matrix.x.x);
       buffer.put(matrix.x.y);
       buffer.put(matrix.x.z);
       buffer.put(matrix.x.w);

       buffer.put(matrix.y.x);
       buffer.put(matrix.y.y);
       buffer.put(matrix.y.z);
       buffer.put(matrix.y.w);

       buffer.put(matrix.z.x);
       buffer.put(matrix.z.y);
       buffer.put(matrix.z.z);
       buffer.put(matrix.z.w);

       buffer.put(matrix.w.x);
       buffer.put(matrix.w.y);
       buffer.put(matrix.w.z);
       buffer.put(matrix.w.w);
       
       buffer.position(0);

      GL20.glUniformMatrix4(uniforms.get(name).intValue(), transpose, buffer);

   }

   public void defineFragOut(int index, String variableName) {
       GL30.glBindFragDataLocation(program, index, variableName);
   }

   /**
* Create an attribute index to be associated with a in variable name of a shader program.
* The attribute index is used to bind attribute data within a Buffer Object.
*
* @param shaderAttribute
* @param attributeName
*/
   public void bindAttribute(String attributeName) {
       attributeIndex++;
       attributes.put(attributeName, attributeIndex);
       GL20.glBindAttribLocation(program, attributeIndex, attributeName);
   }

   /**
* Return the index associated with an attribute name
* @param attributeName
* @return
*/

   public int getAttributeIndex(String attributeName)
   {
       return attributes.get(attributeName);
   }

   public boolean containsAttribute(String attributeName)
   {
       return attributes.containsKey(attributeName);
   }

   /**
*  Detach the shader from the program and delete it.
*/
   public void delete() {
       IntBuffer shaderCount = BufferUtils.createIntBuffer(16);

       GL20.glGetProgram(program, GL20.GL_ATTACHED_SHADERS, shaderCount);

       if (Program.DEBUG) System.out.println("Deleted " + shaderCount.get(0) + " shaders");

       IntBuffer shaders = BufferUtils.createIntBuffer(shaderCount.get(0));

       GL20.glGetAttachedShaders(program, shaderCount, shaders);

       for (int i = 0; i < shaderCount.get(0); i++) {
           GL20.glDetachShader(program, shaders.get(i));
           GL20.glDeleteShader(shaders.get(i));
       }

       GL20.glUseProgram(0);
       GL20.glDeleteProgram(program);

   }

   /**
	* Get the index associated with this program.
	* @return
	*/
   public int getProgram() {
       return program;
   }

   /**
	*  Returns the shader version.
	* @return
	*/
   public static String getVersion() {
       return "Shader language version : " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
   }

   public static int getAttributeCapacity() {
       return GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS);
   }


}
