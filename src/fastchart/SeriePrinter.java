package fastchart;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.lwjgl.opengl.GL11;

public class SeriePrinter {

	private double[] verticalBorderColor = new double[] { 0.411, 0.411, 0.411 };//gray
	private double[] borderColor = new double[] { 1.0, 0.0, 1.0 };
	private float lineWidth = 2.5f;
	private int pointToDraw = 1000;

	public void draw(Serie s, double minX, double maxX, double minY, double maxY) {
		draw(s, pointToDraw, minX, maxX, minY, maxY, lineWidth, borderColor);
	}

	public void draw(Serie s, int numToDraw, double minX, double maxX, double minY, double maxY, float lineWidth, double[] borderColor) {
		GL11.glBegin(GL11.GL_LINES);

		GL11.glLineWidth(lineWidth);
		
		drawVertical(numToDraw, minX, maxX, minY, maxY, borderColor);
		
		drawBorder(minX, maxX, minY, maxY, borderColor);

		drawLine(s, numToDraw, minX, maxX, minY, maxY);

		GL11.glEnd();
	}

	private void drawVertical(int numToDraw, double minX, double maxX, double minY, double maxY, double[] borderColor2) {
		if (borderColor != null) {
			GL11.glColor3d(verticalBorderColor[0], verticalBorderColor[1], verticalBorderColor[2]);
			
			double incrementX = (maxX - minX) / (numToDraw - 1);
			for (;minX<maxX; minX+=incrementX) { 
				GL11.glVertex3d(minX, minY, 0);
				GL11.glVertex3d(minX, maxY, 0);
			}
		}
	}

	private void drawLine(Serie s, int numToDraw, double minX, double maxX, double minY, double maxY) {
		if (s.size() == 0) {
			return;
		}

		Color lineColor2 = s.getLineColor();
		GL11.glColor3d(lineColor2.getRed(), lineColor2.getGreen(), lineColor2.getBlue());

		double incrementX = (maxX - minX) / (numToDraw - 1);
		double actualX = maxX;

		double midY = (maxY + minY) / 2.0;

		
		double scaleFactorByChartCompressionPile = (Math.abs(maxY)+Math.abs(minY))/2;
		
		double scaleY = s.getScaleY()*scaleFactorByChartCompressionPile;
		//1/(s.maxY+s.minY);
		//System.out.println("midy"+midY+"s.maxY-s.minY "+s.maxY+" "+s.minY+" - maxY-minY "+maxY+" "+minY+" scale:"+scaleY);
		//System.out.println("scaleFactorByChartCompressionPile"+scaleFactorByChartCompressionPile+" maxY "+maxY+" minY "+minY);
		
		

		Point2D last = s.getLast();// this is the last point
		
		double y = last.getY() * scaleY + midY;
		//System.out.println("midy"+midY+" maxY-minY "+maxY+" "+minY+" scale:"+scaleY+" y1 "+last.getY()+" y2 "+y);

		for (int i = 1; i < Math.min(s.size(), numToDraw); i++) {
			Point2D point2d = s.getFromBottom(i);
			y = last.getY() * scaleY + midY;
			GL11.glVertex3d(actualX, y, 0);
			actualX -= incrementX;
			y = point2d.getY() * scaleY + midY;
			GL11.glVertex3d(actualX, y, 0);
			last = point2d;
		}
	}

	private void drawBorder(double minX, double maxX, double minY, double maxY, double[] borderColor2) {
		// draw border
		if (borderColor != null) {
			GL11.glColor3d(borderColor[0], borderColor[1], borderColor[2]);
			// top
			GL11.glVertex3d(minX, maxY, 0);
			GL11.glVertex3d(maxX, maxY, 0);

			// bottom
			GL11.glVertex3d(minX, minY, 0);
			GL11.glVertex3d(maxX, minY, 0);
		}
	}

}
