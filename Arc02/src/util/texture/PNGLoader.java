package util.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class PNGLoader {

	public static Texture2D load(String filename) throws IOException {
		return load(filename, GL11.GL_TEXTURE_2D);
	}
	public static Texture2D load(String filename, int target) throws IOException {
		
		BufferedImage img = null;
		img = ImageIO.read(new File(filename));
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				
				int rgb = img.getRGB(x, y);

				buffer.put((byte) ((rgb >> 16) & 0xFF));
				buffer.put((byte) ((rgb >> 8) & 0xFF));
				buffer.put((byte) (rgb & 0xFF));
				buffer.put((byte) ((rgb >> 24) & 0xFF)); 
				
			}
		}
		
		buffer.flip();
		
		Texture2D tex = new Texture2D(img.getWidth(), img.getHeight(), buffer);
		
		return tex;
	}
	
	public static TextureCubemap loadCubemap(String filename) {
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int fr = img.getWidth()/4;
		

		ByteBuffer posx = BufferUtils.createByteBuffer(fr * fr * 4);
		ByteBuffer negx = BufferUtils.createByteBuffer(fr * fr * 4);
		ByteBuffer posy = BufferUtils.createByteBuffer(fr * fr * 4);
		ByteBuffer negy = BufferUtils.createByteBuffer(fr * fr * 4);
		ByteBuffer posz = BufferUtils.createByteBuffer(fr * fr * 4);
		ByteBuffer negz = BufferUtils.createByteBuffer(fr * fr * 4);
		
		ByteBuffer buffers[] = {posx, negx, posy, negy, posz, negz};
		
		BufferedImage sub = null;
		
		for (int i = 0; i < 6; i++) {
			
			switch (i) {
			case 0:
				sub = img.getSubimage(fr*2, fr, fr, fr);
				break;
			case 1:
				sub = img.getSubimage(0, fr, fr, fr);
				break;
			case 2:
				sub = img.getSubimage(fr, 0, fr, fr);
				break;
			case 3:
				sub = img.getSubimage(fr, fr*2, fr, fr);
				break;
			case 4:
				sub = img.getSubimage(fr, fr, fr, fr);
				break;
			case 5:
				sub = img.getSubimage(fr*3, fr, fr, fr);
				break;
			}

			for (int y = 0; y < sub.getHeight(); y++) {
				for (int x = 0; x < sub.getWidth(); x++) {
					int rgb = sub.getRGB(x, y);
	
					buffers[i].put((byte) ((rgb >> 16) & 0xFF));
					buffers[i].put((byte) ((rgb >> 8) & 0xFF));
					buffers[i].put((byte) ((rgb) & 0xFF));
					buffers[i].put((byte) ((rgb >> 24) & 0xFF));
				}
			}
			buffers[i].flip();
		}
		
		TextureCubemap tex = new TextureCubemap(fr, fr, buffers);
		
		return tex;
	}

}
