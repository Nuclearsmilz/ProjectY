package main.java.ProjectY;

///edit here
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import main.java.ProjectY.graphics.*;
import main.java.ProjectY.input.*;

public class ProjectY extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	public static String title = "ProjectY";
	
	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private boolean running = true;
	
	private Screen screen;
	
	private BufferedImage Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) Image.getRaster().getDataBuffer()).getData();
	
	public ProjectY() {
		
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		
		addKeyListener(key);
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int ticks = 0;
		
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				ticks++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(ticks + " ups, " + frames + " fps");
				frame.setTitle(title + "  |  " + ticks + " ups, " + frames + " fps");
				ticks = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
	
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
			
		}
		screen.clear();
		screen.render();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(Image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		
		ProjectY game = new ProjectY();
		game.frame.setResizable(false);
		game.frame.setTitle(ProjectY.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}