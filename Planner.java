import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Vector;

class Planner extends Agent{
	public Model m;
	public State start, goal;
	public TreeSet<State> visited;
	public PriorityQueue<State> frontier;
	final int[] act = new int[] {10, -10, 10, 0, 10, 10, 0, 10, 0, -10, -10, -10, -10, 0, -10, 10};
	
	Planner(Model m, int destX, int destY) {
		this.m = m;
		this.start = new State((int)m.getX(), (int)m.getY());
		this.goal = new State(roundTen(destX), roundTen(destY));
	}

	Vector<Integer[]> ucs() {
		(frontier = new PriorityQueue<>(new CostComp())).add(start);
		(visited = new TreeSet<>(new PosComp())).add(start);
		State best = null;

		//
		while(!frontier.isEmpty()) {

			State s = frontier.poll();
			print(s.pos); System.out.print("    "); print(goal.pos);
			if(new PosComp().compare(s, goal) == 0) {
				if(best == null || best.cost > s.cost)
					best = s;
			}
			for(int i = 0; i < 16; i+=2) {
				int[] newPos = new int[] {s.pos[0] + act[i], s.pos[1] + act[i+1]};
				if(newPos[0] >= 0 && newPos[0] < 1200 && newPos[1] >= 0 && newPos[1] < 600) {
					float speed = m.getTravelSpeed((float)(s.pos[0] + newPos[0])/2, (float)(s.pos[1] + newPos[1])/2);
					double actCost = this.getDistance(s.pos[0], s.pos[1], newPos[0], newPos[1])/speed;
					State newChild = new State(actCost, s, newPos[0], newPos[1]);
					if(visited.contains(newChild)) {
						State oldChild = visited.floor(newChild);
						if(oldChild != null) if (s.cost + actCost < oldChild.cost) {
							oldChild.cost = s.cost + actCost;
							oldChild.parent = s;
						}
					}
					else {
						newChild.cost = s.cost + actCost;
						frontier.add(newChild);
						visited.add(newChild);
					}
				}
			}
		}
		return state2moves(best);
	}

	public Vector<Integer[]> state2moves(State state) {
		State s = state;
		Vector<Integer[]> moves = new Vector<>();
		while(s != null) {
			moves.add(new Integer[]{s.pos[0], s.pos[1]});
			s = s.parent;
		}
		return moves;
	}
	
	public float getDistance(int x1, int y1, int x2, int y2) {
		return (float)Math.sqrt((float)Math.pow(x2 - x1, 2) + (float)Math.pow(y2-y1, 2));
	}
	
	class CostComp implements Comparator<State> {
		public int compare(State a, State b) {
			return -Double.compare(a.cost, b.cost);
		}
	}
	
	class PosComp implements Comparator<State> {
		public int compare(State a, State b) {
			for(int i = 0; i < 2; i++) {
				if(a.pos[i] < b.pos[i])
					return -1;
				else if(a.pos[i] > b.pos[i])
					return 1;
			}
			return 0;
		}
	}

	void print(int[] s) {
		System.out.println("[" + s[0] + "," + s[1] + "]");
	}
}