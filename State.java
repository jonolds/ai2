class State {
	double cost;
	State parent;
	int[] pos;
	State(int x, int y) {
		pos = new int[]{x, y};
	}
	State(double cost, State parent) {
		this.cost = cost;
		this.parent = parent;
	}
	State(double cost, State parent, int x, int y) {
		this(cost, parent);
		pos = new int[]{x, y};
	}

	State(State parent, int[] pos) {
		this.parent = parent;
		this.pos = new int[2];
		this.pos[0] = pos[0];
		this.pos[1] = pos[1];
	}

	void printAll() {
		System.out.println("[" + pos[0] + "," + pos[1] + "]  cost: " + cost + "  parent: " + ((parent == null) ? "null" : parent));
	}
	void printPos() {
		System.out.println("[" + pos[0] + "," + pos[1] + "]");
	}
	void printCost() {
		System.out.println(cost);
	}
}