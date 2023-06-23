package flapflap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JFrame;

public class FlappyBird implements ActionListener, MouseListener {
	
	public static FlappyBird floppy;
	public final int WIDTH = 800, HEIGHT = 800;
	public Render renderer;
	public Rectangle bird;
	public int ticks, yMotion, score;
	public static int highscore;
	public ArrayList<Rectangle> cols;
	public boolean gameOver, started;
	public Random rand;
	
	public FlappyBird() {
		JFrame frame = new JFrame();
		Timer timer = new Timer(20, this);
		
		renderer = new Render();
		rand = new Random();
		
		frame.add(renderer);
		frame.setTitle("Flappy Bird");
		frame.setSize(WIDTH, HEIGHT);
		frame.addMouseListener(this);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		bird = new Rectangle(10, HEIGHT / 2 - 10, 20, 20);
		cols = new ArrayList<Rectangle>();
		
		addCol(true);
		addCol(true);
		addCol(true);
		addCol(true);
		
		timer.start();
	}
	
	public void jump() {
		if (gameOver) {
			bird = new Rectangle(10, HEIGHT / 2 - 10, 20, 20);
			cols.clear();
			score = 0;
			yMotion = 0;
			
			addCol(true);
			addCol(true);
			addCol(true);
			addCol(true);
			
			gameOver = false;
		}
		
		if (!started) {
			started = true;
		}
		else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}
			
			yMotion -= 10;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		int speed = 10;
		ticks++;
		
		if (started) {
			if (bird.x <= WIDTH / 2 - 10 - speed) {
				bird.x += speed;
			}
			else if (bird.x <= WIDTH / 2 - 10) {
				int xx = WIDTH / 2 - 10 - bird.x;
				for (int i = 0; i < cols.size(); i++) {
					Rectangle col = cols.get(i);
					col.x -= speed - xx;
				}
			}
			else {
				for (int i = 0; i < cols.size(); i++) {
					Rectangle col = cols.get(i);
					col.x -= speed;
				}
			}
			
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for (int i = 0; i < cols.size(); i++) {
				Rectangle col = cols.get(i);
				if (col.x + col.width < 0) {
					cols.remove(col);
					if (col.y == 0) {
						addCol(false);
					}
				}
			}
			
			bird.y += yMotion;
			
			for (Rectangle col : cols) {
				if (bird.x + bird.width / 2 > col.x + col.width / 2 - speed && bird.x + bird.width / 2 < col.x + col.width / 2 + speed) {
					if (!gameOver) {
						score++;
					}
				}
				
				if (col.intersects(bird)) {
					gameOver = true;
					
					bird.x = col.x - bird.width;
				}
			}
			if (bird.y >= HEIGHT - 100 || bird.y <= 0) {
				gameOver = true;
			}
			
			if (bird.y + yMotion >= HEIGHT - 100) {
				bird.y = HEIGHT - 100 - bird.height;
				gameOver = true;
			}
		}
		
		renderer.repaint();
	}
	
	public void addCol(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if (start) {
			cols.add(new Rectangle(WIDTH + width + cols.size() * 300, HEIGHT - height - 100, width, height));
			cols.add(new Rectangle(WIDTH + width + (cols.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else {
			cols.add(new Rectangle(cols.get(cols.size() - 1).x + 600, HEIGHT - height - 100, width, height));
			cols.add(new Rectangle(cols.get(cols.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(new Color(49, 82, 37));
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void repaint(Graphics g) {
		g.setColor(new Color(177, 235, 252));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(new Color(128, 66, 5));
		g.fillRect(0, HEIGHT - 90, WIDTH, 90);
		
		g.setColor(new Color(87, 181, 20));
		g.fillRect(0, HEIGHT - 100, WIDTH, 10);
		
		g.setColor(new Color(173, 26, 3));
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for (Rectangle col : cols) {
			paintColumn(g, col);
		}
		
		Font big = new Font("Times New Roman", 1, 100);
		Font small = new Font("Times New Roman", 1, 60);

		g.setFont(big);
		g.setColor(Color.white);
		
		if (gameOver) {
			g.drawString("GAME OVER", 95, HEIGHT / 2 - 50);
			g.drawString("Click to Restart", 70, 600);
			g.setFont(small);
			g.drawString(String.valueOf(score / 2), WIDTH / 2 -  15, 80);
			highscore = (int) Math.max(highscore, score / 2);
			g.drawString("Highest Score: " + String.valueOf(highscore), 220, 160);
		}
		if (!started) {
			g.setColor(new Color(23, 27, 135));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.white);
			g.drawString("Click to start!", 90, HEIGHT / 2 - 50);
			g.setFont(small);
			g.drawString("Use your mouse to play", 90, HEIGHT / 2 + 100);
		}
		if (!gameOver && started) {
			g.setFont(small);
			g.drawString(String.valueOf(score / 2), WIDTH / 2 - 15, 100);
		}
	}
	
	public static void main(String[] args) {
		floppy = new FlappyBird();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
