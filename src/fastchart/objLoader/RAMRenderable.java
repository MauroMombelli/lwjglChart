package fastchart.objLoader;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

public class RAMRenderable extends GameRenderable {

	protected FloatBuffer normalsBuffer;
	protected FloatBuffer verticesBuffer;
	protected FloatBuffer interleavedBuffer;

	public RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {
		super(new double[3], new double[3]);
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}

	public RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer, double[] transform, double[] rotation) {
		super(transform, rotation);
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}

	@Override
	public void render() {

		GL11.glPushMatrix();

		verticesBuffer.rewind();

		
		GL11.glRotated(Math.toDegrees(rotation[0]), 1.0, 0.0, 0.0);
		GL11.glRotated(Math.toDegrees(rotation[1]), 0.0, 1.0, 0.0);
		GL11.glRotated(Math.toDegrees(rotation[2]), 0.0, 0.0, 1.0);
		
		GL11.glTranslated(transform[0], transform[1], transform[2]);
		
		GL11.glScaled(0.1, 0.1, 0.1);
		//GL11.glRotatef((float) Math.toDegrees(rotation[0]), 0, 0, 1);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glVertexPointer(3, 0, verticesBuffer);
		GL11.glNormalPointer(0, normalsBuffer);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		
		GL11.glPopMatrix();

	}

	@Override
	public void renderInterleavedDrawArray() {

		GL11.glPushMatrix();

		GL11.glRotated(Math.toDegrees(rotation[0]), 1.0, 0.0, 0.0);
		GL11.glRotated(Math.toDegrees(rotation[1]), 0.0, 1.0, 0.0);
		GL11.glRotated(Math.toDegrees(rotation[2]), 0.0, 0.0, 1.0);
		GL11.glTranslated(transform[0], transform[1], 0.0f);


		GL11.glInterleavedArrays(GL11.GL_N3F_V3F, 0, interleavedBuffer);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, interleavedBuffer.capacity() / 6);

		GL11.glPopMatrix();

	}

	public void setBuffers(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {

		this.verticesBuffer = verticesBuffer;
		this.normalsBuffer = normalsBuffer;
		this.interleavedBuffer = interleavedBuffer;

	}
}