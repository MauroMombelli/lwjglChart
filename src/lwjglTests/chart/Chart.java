package lwjglTests.chart;

import java.util.ArrayList;
import java.util.List;

public class Chart implements Panel {
	
	private List<Serie> series = new ArrayList<>();

	SeriesPrinter pr = new SeriesPrinter();
	@Override
	public void draw(double left, double right, double bottom, double top) {
		
		for(Serie s:series){
			pr.draw(s, left, right, bottom , top);
		}
	}

}
