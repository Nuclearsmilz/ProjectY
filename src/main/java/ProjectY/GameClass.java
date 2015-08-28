package main.java.ProjectY;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import main.java.ProjectY.graphics.Screen;

public class GameClass extends Canvas implements Runnable {

/**
	 * 
	 */
	//The Size Of The Window
	private static final long serialVersionUID = 1L;
public static int width = 300;
public static int height = width / 16 *9 ;
public static int scale = 3;

public static String title = "ProjectY";

private Thread thread;
private JFrame frame;
private boolean running = false;



private Screen screen;

private BufferedImage Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
private int [] pixels =  ((DataBufferInt) Image.getRaster().getDataBuffer()).getData();

public GameClass () {
	
	Dimension size = new Dimension (width*scale,height*scale);
	setPreferredSize(size);
	
	screen = new Screen(width, height);
	
	
	
	frame = new JFrame();
}

//Running State
public synchronized void start() {
	running = true;
	thread = new Thread (this, "Display");
	thread.start();
	
	}	
//Stopped State
	public synchronized void stop() {
		running = false;
		try {
		thread.join();
	}	catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	
	//While Running Loops
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0 ;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		
		while (running) {
				long now = System.nanoTime();
			delta += (now - lastTime) / ns ;
			lastTime = now;
			while (delta>= 1) {
				update();
				updates++;
				delta--;
			}
		
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "ups," + frames + "fps");
				frame.setTitle(title + "  |  " + updates + "ups," + frames + "fps" );
				frames = 0;
				updates = 0;
			}
		}
		
	}
	
	public void update() {
		
	}
	//Buffering
	public void render() {
		BufferStrategy bs = getBufferStrategy() ;
		if(bs == null) {
			createBufferStrategy(3) ;
			return;
			
		}
		screen.Clear();
		screen.render();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
				
				
		Graphics g = bs.getDrawGraphics();
		g.drawImage(Image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();		
		bs.show();
	}
	// Main String 
	public static void main (String[]  args) {
		
		
		GameClass game = new GameClass();
		game.frame.setResizable(false);
		game.frame.setTitle(game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
		
	}
}
