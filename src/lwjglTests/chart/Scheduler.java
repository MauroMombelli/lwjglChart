package lwjglTests.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Scheduler {
	private long time = 0;
	private SortedMap<Long, List<Container>> schedulationMap = new TreeMap<>(); 

	
	public void add(TimedAction timedAction, long inteval) {
		new Container(timedAction, inteval, time);
	}

	public void execute(long time) {
		this.time = time;
		
		SortedMap<Long, List<Container>> subMap = schedulationMap.headMap(time);
		for ( List<Container> con:subMap.values() ){
			for (Container c:con){
				c.execute(time);
			}
		}
		schedulationMap = schedulationMap.tailMap(time);
		
		List<Container> list = schedulationMap.remove(time);
		if (list != null){
			for (Container c:list){
				c.execute(time);
			}
		}
	}
	
	class Container{
		private long i;
		private TimedAction t;

		public Container(TimedAction t, long interval, long start){
			this.t = t;
			this.i = interval;
			schedule(start);
		}
		
		public void execute(long time){
			t.execute(time);
			System.out.println("executed: "+time);
			schedule(time);
		}
		
		public void schedule(long now){
			List<Container> list = schedulationMap.get(now+i);
			if (list == null){
				list = new ArrayList<>();
				schedulationMap.put(now+i, list);
			}
			list.add(this);
			System.out.println("rescheduled: "+(now+i) );
		}
	}
}
