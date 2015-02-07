package fastchart;

import java.io.File;
import java.nio.file.Paths;

import fastchart.objLoader.ObjectHandler;
import fastchart.objLoader.RAMRenderable;
import fastchart.utils.Panel;

public class PlainObject implements Panel{
	
	private static final ObjectHandler oHandler = new ObjectHandler(Paths.get("Objects"));
	private RAMRenderable requestRAMMesh;
	
	public PlainObject(String filename) throws Exception {
		new File(filename);
		requestRAMMesh = oHandler.requestRAMMesh(filename);
		
		requestRAMMesh.transform[2] = 0f;
	}

	@Override
	public void draw(double left, double right, double bottom, double top) {
		requestRAMMesh.render();
	}

	public void setRotation(double[] euler) {
		requestRAMMesh.rotation = euler;
	}

}
