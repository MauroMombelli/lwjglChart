package fastchart;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Serie {
	private int size = Integer.MAX_VALUE;
	
	/**
	 * please note:
	 * this is FIFO where the newest element is on the RIGHT instead of on the left. This help a lot in the drawing process.
	 */
	private final LinkedList<Point2D> list = new LinkedList<Point2D>();
	
	public double minY = Double.MAX_VALUE;
	public double maxY = Double.MIN_VALUE;

	private Color lineColor = Color.black;

	private double scaleY = 1;
	
	public Serie(double scaleY){
		this.scaleY = scaleY;
	}
	
	public void addPoint(Point2D p){
		synchronized (this) {
			if (p != null){
				if ( p.getY() > maxY ){
					maxY = p.getY();
				}
				
				if ( p.getY() < minY ){
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
	
	public void setLineColor(float r, float g, float b) {
		lineColor = new Color(r, g, b);
	}
	
	public void setLineColor(Color color) {
		lineColor = color;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public double getScaleY() {
		return scaleY;
	}
}
