import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JFrame implements ActionListener {
	Controller controller;
	Model model;
	private MyPanel panel;
	Color color;

	public View(Controller c, Model m) throws Exception {
		this.controller = c;
		this.model = m;
		// Make the game window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Moving Robot");
		this.setSize(250, 110);
		this.panel = new MyPanel();
		this.panel.addMouseListener(controller);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) { repaint(); }// indirectly calls MyPanel.paintComponent

	class MyPanel extends JPanel {
		Image image_robot, redX, yelX;

		MyPanel() throws Exception {
			this.image_robot = ImageIO.read(new File("robot_blue.png"));
			redX = ImageIO.read(new File("redX.png"));
			yelX = ImageIO.read(new File("yelX2.png"));
		}

//		void drawTerrain(Graphics g) {
//			byte[] t2 = model.t2;
//			int posBlue2 = 0;
//			int posRed = (10 * 10 - 1) * 4;
//			for (int y = 0; y < 10; y++)
//				for (int x = 0; x < 10; x++) {
//					int bb = t2[posBlue2 + 1] & 0xff;
//					int gg = t2[posBlue2 + 2] & 0xff;
//					int rr = t2[posBlue2 + 3] & 0xff;
//					g.setColor(new Color(rr, gg, bb));
//					g.fillRect(10 * x, 10 * y, 10, 10);
//					posBlue2 += 4;
//				}
//		}
		int x, y, w = 10, h = 10;
		void drawTerrain(Graphics g) {
			//System.out.println(g.getClipBounds());
			byte[] terrain = model.getTerrain();
			int posBlue = 16;
			int posRed = (60 * 60 - 1) * 4;
			for(int y = 0; y < 7; y++) {
				for(int x = 0; x < 7; x++) {
					int bb = terrain[posBlue + 1] & 0xff;
					int gg = terrain[posBlue + 2] & 0xff;
					int rr = terrain[posBlue + 3] & 0xff;
					g.setColor(new Color(rr, gg, bb));
					g.fillRect(10 * x, 10 * y, 10, 10);
					posBlue += 4;
				}
				posBlue += 212;
				for(int x = 60; x < 120; x++) {
					int bb = terrain[posRed + 1] & 0xff;
					int gg = terrain[posRed + 2] & 0xff;
					int rr = terrain[posRed + 3] & 0xff;
					g.setColor(new Color(rr, gg, bb));
					g.fillRect(10 * x, 10 * y, 10, 10);
					posRed -= 4;
				}
			}
//			System.out.println(terrain[20] & 0xff);
//			g.setColor(new Color(87, 122, 129));
//			g.fillRect(40, 40, 500, 100);
		}

		void drawSprites(Graphics g) {
			ArrayList<Model.Sprite> sprites = model.getSprites();
			for(int i = 0; i < sprites.size(); i++) {
				// Draw the robot image
				Model.Sprite s = sprites.get(i);
				g.drawImage(redX, (int)s.destX - 8, (int)s.destY -8, null);
				g.drawImage(yelX, (int)s.x - 8, (int)s.y - 8, null);
				//g.drawImage(image_robot, (int)s.x - 12, (int)s.y - 32, null);
			}
		}

		public void paintComponent(Graphics g) {
			// Give the agents a chance to make decisions
			if(!controller.update())
				View.this.dispatchEvent(new WindowEvent(View.this, WindowEvent.WINDOW_CLOSING)); // Close this window
			// Draw the view
			drawTerrain(g);
			drawSprites(g);
			controller.agent.drawPlan(g, model);
		}
	}
}