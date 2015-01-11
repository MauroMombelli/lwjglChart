package fastchart;

import java.util.ArrayList;
import java.util.List;

import fastchart.utils.Panel;

public class Chart implements Panel {
	
	private List<Serie> series = new ArrayList<>();

	SeriePrinter pr = new SeriePrinter();
	@Override
	public void draw(double left, double right, double bottom, double top) {
		synchronized (series) {
			for(Serie s:series){
				synchronized (s) {
					pr.draw(s, left, right, bottom , top);
				}
			}
		}
	}
	
	public void addSerie(Serie s){
		synchronized (series) {
			series.add(s);
		}
	}

}
