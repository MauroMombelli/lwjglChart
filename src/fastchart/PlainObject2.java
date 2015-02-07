package fastchart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import fastchart.objLoader2.ObjCompiler;
import fastchart.utils.Panel;

public class PlainObject2 implements Panel{
	
	private Integer pointerToPlaneCompiled = null;
	private File objFile;

	public PlainObject2(String filename) throws FileNotFoundException {
		this.objFile = new File(filename);
		if (!objFile.exists()){
			throw new FileNotFoundException();
		}
	}

	@Override
	public void draw(double left, double right, double bottom, double top) {
		if (pointerToPlaneCompiled == null){
			try {
				pointerToPlaneCompiled = ObjCompiler.setUpDisplayLists(objFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glCallList(pointerToPlaneCompiled);
	}

}
