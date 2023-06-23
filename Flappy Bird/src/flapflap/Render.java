package flapflap;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Render extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		FlappyBird.floppy.repaint(g);
	}

}
