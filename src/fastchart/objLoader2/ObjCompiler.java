package fastchart.objLoader2;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class ObjCompiler {

	public static int setUpDisplayLists(File f) throws IOException {
		int compiledReference = GL11.glGenLists(1);
		GL11.glNewList(compiledReference, GL11.GL_COMPILE);
		{
			Model m = OBJLoader.loadModel(f);
			GL11.glBegin(GL11.GL_TRIANGLES);
			for (Model.Face face : m.getFaces()) {
				Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
				GL11.glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
				GL11.glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
				GL11.glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
				GL11.glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
				GL11.glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
				GL11.glVertex3f(v3.x, v3.y, v3.z);
			}
			GL11.glEnd();
		}
		GL11.glEndList();
		return compiledReference;
	}
}
