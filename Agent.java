import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.TreeSet;
import java.util.Vector;

class Agent {
	boolean newUcsFlag = false, clicked = false;
	public Vector<Integer[]> path;
	int[] mousePos;
	Planner plan;
	TreeSet<State> visited;


	void drawPlan(Graphics g, Model m) {
		g.setColor(Color.red);
		g.drawLine((int)m.getX(), (int)m.getY(), (int)m.getDestX(), (int)m.getDestY());

		if(newUcsFlag) {
			g.setColor(Color.pink);
			for(Integer[] iArr: path)
				g.drawOval(iArr[0], iArr[1], 4, 4);
			drawPath(g, m);
			newUcsFlag = false;
		}

//		g.drawLine(30, 30, 300, 300);
//		if(newUcsFlag)
//			drawPath(g, m);
//		if(clicked) {
//			g.setColor(Color.black);
//			g.drawString("[" + Integer.toString(mousePos[0]) + "," + Integer.toString(mousePos[1]) + "]", mousePos[0], mousePos[1]);
//		}
	}

	public void drawPath(Graphics g, Model m) {
		g.setColor(Color.green);
		for(int i = 0; i < path.size()-1; i++) {
			int x1 = path.get(i)[0], y1 = path.get(i)[1];
			int x2 = path.get(i+1)[0], y2 = path.get(i+1)[1];
			System.out.println(x1 + "," + y1 + " " + x2 + "," + y2);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	void update(Model m) {
		Controller c = m.getController();
		while(true) {
			MouseEvent e = c.nextMouseEvent();
			if(e == null)
				break;
			plan = new Planner(m, e.getX(), e.getY());
			path = plan.ucs();
			visited = plan.visited;
			if(!path.isEmpty())
				newUcsFlag = true;
			else
				System.out.println("returned path empty");
			//m.setDest(e.getX(), e.getY());
			System.out.println(visited.size());
		}
	}

	int roundTen(int s) {
		return ((s+5)/10)*10;
	}
	
	public static void main(String[] args) throws Exception {
		Controller.playGame();
	}


	void drawGridLines(Graphics g, Model m) {
		g.setColor(Color.lightGray);
		for(int i = 0; i < g.getClipBounds().width; i+=10)
			g.drawLine(i, 0, i, g.getClipBounds().height);
		for(int i = 0; i < g.getClipBounds().height; i+=10)
			g.drawLine(0, i, g.getClipBounds().width, i);

		g.setColor(new Color(230, 0, 230));
		for(int i = 0; i < g.getClipBounds().width; i+=100)
			g.drawLine(i, 0, i, g.getClipBounds().height);
		for(int i = 0; i < g.getClipBounds().height; i+=100)
			g.drawLine(0, i, g.getClipBounds().width, i);
	}

	Agent() {
		mousePos = new int[2];
	}
}