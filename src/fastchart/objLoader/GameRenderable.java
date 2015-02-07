package fastchart.objLoader;

public abstract class GameRenderable {

	public double[] transform;
	public double[] rotation;

	public GameRenderable(double[] transform,double[] rotation) {
		this.transform = transform;
		this.rotation = rotation;
	}

	public abstract void render();

	public abstract void renderInterleavedDrawArray();

}