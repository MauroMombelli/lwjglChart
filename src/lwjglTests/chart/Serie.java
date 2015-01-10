package lwjglTests.chart;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Serie {
	private int size = Integer.MAX_VALUE;
	
	/**
	 * please note:
	 * this is FIFO where the newest element is on the RIGHT instead of on the left. This help a lot in the drawing process.
	 */
	private final LinkedList<Point2D> list = new LinkedList<Point2D>();
	
	public void addPoint(Point2D p){
		if (p != null)
			list.addFirst(p);
		
		while (list.size()> size){
			list.removeLast();
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
}
