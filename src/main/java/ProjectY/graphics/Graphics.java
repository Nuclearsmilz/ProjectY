package main.java.ProjectY.graphics;

import java.util.Random;

public class Graphics {

	private int width, height;
	public int [] pixels;
	public final int MAP_SIZE = 8;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;
	
	public int [] tiles = new int [MAP_SIZE * MAP_SIZE];
	
	private Random random = new Random();
	
	
	public Graphics(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int [width * height] ;
		
		
		
		for (int i = 0; i < 32 *32; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
				
		}
		
		
	public void Clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels [i] = 0;
		}
	}
	
	public void render() {
			
			for (int y = 0; y < height; y++)  {	
				int yy = y ;
				if (yy< 0 || yy>= height) break ;
				
			for (int x = 0 ; x < width; x++) {
				int xx = x;
				if (xx< 0 || xx>= width) break ;
				
					
					int tileIndex = (xx / 5) + (yy / 6) * MAP_SIZE ;
					pixels [x + y * width] = tiles [tileIndex];
					// 20 + 30 * 100
					// (20, 30)
				
				}
			}
	}
	
	
}