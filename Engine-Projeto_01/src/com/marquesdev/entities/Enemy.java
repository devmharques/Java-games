package com.marquesdev.entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;



public class Enemy extends Entity{

	public Enemy(int x, int y, int width, int height, int speed,BufferedImage sprite) {
		super(x, y, width, height, speed,sprite);
		
	}

	public void tick(){
		depht = 0;
		/* 
		if (path == null || path.size() == 0) {
			Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
			path = AStar.findPath(Game.world, start, end);
		}	
	}else {
		if (new Random().nextInt(100) < 5) {
			Game.player.life-=Game.rand.nextInt(4);
			Game.player.isDamaged = true;
		}
	}
		if(new Random().nextInt(100) < 75)
			followPath(path);
		if(new Random().nextInt(100) < 5){
			Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
			path = AStar.findPath(Game.world, start, end);
		}
		
	}
	*/
	}
	
	public void render(Graphics g) {
	
	}
	
	

}