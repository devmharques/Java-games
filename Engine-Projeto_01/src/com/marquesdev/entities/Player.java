package com.marquesdev.entities;

//import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.marquesdev.world.World;

public class Player extends Entity{
	public boolean right,up,left,down;
	/*
	public boolean right,up,left,down;
	public boolean jump = false;
	public int z = 0;
	public int jumpFrames = 50, jumpCur = 0;
	public boolean isjumping = false;
	public boolean jumpUp = false, jumpDown = false;
	public int jumpSpd = 1;
	*/

	public Player(int x, int y, int width, int height, int speed,BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
	}
	
	public void tick(){
		depht = 1;
		/*
		if (jump) {
			if (isjumping == false) {
				jump = false;
				isjumping = true;
				jumpUp = true;
			}
			
		}
		
		if (isjumping == true) {
			
				if (jumpUp) {
					jumpCur+=2;
				} else if (jumpDown) {
					jumpCur-=2;
					if (jumpCur <= 0) {
						isjumping = false;
						jumpDown = false;
						jumpUp = false;
					}
				}
				z = jumpCur;
				if (jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
					//System.out.println("Chegou na altura maxima!");
				}
			
		}
		*/

		
		if(right && World.isFree((int)(x+speed),this.getY())) {
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))){
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))){
			y+=speed;
		}
		
	
	}

}
