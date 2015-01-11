package fastchart;

import java.awt.geom.Point2D;

import org.lwjgl.opengl.GL11;

public class SeriePrinter {

	private double[] lineColor = new double[] { 1.0, 1.0, 1.0 };
	private double[] borderColor = new double[] { 1.0, 0.0, 1.0 };
	private float lineWidth = 2.5f;
	private int pointToDraw = 10;

	public void draw(Serie s, double minX, double maxX, double minY, double maxY) {
		draw(s, pointToDraw, minX, maxX, minY, maxY, lineWidth, lineColor, borderColor);
	}

	public void draw(Serie s, int numToDraw, double minX, double maxX, double minY, double maxY, float lineWidth, double[] lineColor, double[] borderColor) {
		if (s.size() == 0){
			return;
		}
		GL11.glBegin(GL11.GL_LINES);

		GL11.glLineWidth(lineWidth);
		
		double[] lineColor2 = s.getLineColor();
		GL11.glColor3d(lineColor2[0], lineColor2[1], lineColor2[2]);

		double incrementX = (maxX - minX) / (numToDraw-1);
		double actualX = maxX;
		
		double midY = (maxY+minY)/2.0;
		
		double scaleY = s.getScaleY();
		//double scaleY = Math.min(Math.min(maxY/s.maxY.doubleValue(), minY/s.minY.doubleValue()), 1);
		//System.out.println("midY "+midY+" maxY "+maxY+" minY "+minY+" scale "+scaleY);

		Point2D last = s.getLast();// this is the last point

		for (int i = 1; i < Math.min(s.size(), numToDraw); i++) {
			Point2D point2d = s.getFromBottom(i);
			GL11.glVertex3d(actualX, ensureRanged( (last.getY()+midY)*scaleY, minY, maxY), 0);
			actualX -= incrementX;
			GL11.glVertex3d(actualX, ensureRanged( (point2d.getY()+midY)*scaleY, minY, maxY), 0);
			last = point2d;
		}
		
		if ( borderColor!= null ){
			GL11.glColor3d(borderColor[0], borderColor[1], borderColor[2]);
			//top
			GL11.glVertex3d(minX, maxY, 0);
			GL11.glVertex3d(maxX, maxY, 0);
			
			//bottom
			GL11.glVertex3d(minX, minY, 0);
			GL11.glVertex3d(maxX, minY, 0);
		}
		GL11.glEnd();
	}

	private double ensureRanged(double value, double min, double max) {
		//return Math.min(Math.max(value, min), max);
		return value;
	}

	public void setLineColor(int r, int g, int b) {
		lineColor[0] = r;
		lineColor[1] = g;
		lineColor[2] = b;
	}
}
