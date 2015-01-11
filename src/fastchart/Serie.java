package fastchart;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Serie {
	private int size = Integer.MAX_VALUE;
	
	/**
	 * please note:
	 * this is FIFO where the newest element is on the RIGHT instead of on the left. This help a lot in the drawing process.
	 */
	private final LinkedList<Point2D> list = new LinkedList<Point2D>();
	
	public Number minY = 0;
	public Number maxY = 0;

	private double[] lineColor = new double[] { 1.0, 1.0, 1.0 };

	private double scaleY = 1.0/32768.0;
	
	public void addPoint(Point2D p){
		synchronized (this) {
			if (p != null){
				if ( p.getY() > maxY.doubleValue() ){
					maxY = p.getY();
				}
				
				if ( p.getY() < minY.doubleValue() ){
					minY = p.getY();
				}
				list.addFirst(p);
			}
			
			while (list.size()> size){
				list.removeLast();
			}
		}
	}

	public Point2D getLast(){
		return list.get(0);
	}

	public Point2D getFromBottom(int i) {
		return list.get(i);
	}

	public int size() {
		return list.size();
	}
	
	public void setLineColor(int r, int g, int b) {
		lineColor[0] = r;
		lineColor[1] = g;
		lineColor[2] = b;
	}

	public double[] getLineColor() {
		return lineColor;
	}

	public double getScaleY() {
		return scaleY;
	}
}
