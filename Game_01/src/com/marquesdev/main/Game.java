package com.marquesdev.main;

import java.awt.Canvas;
import java.awt.Color;
//import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.Toolkit;
//import java.awt.Image;
//import java.awt.Point;
//import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.marquesdev.entities.BulletShoot;
import com.marquesdev.entities.Enemy;
import com.marquesdev.entities.Entity;
import com.marquesdev.entities.Npc;
import com.marquesdev.entities.Player;
import com.marquesdev.graficos.Spritesheet;
import com.marquesdev.graficos.UI;
import com.marquesdev.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1,MAX_LEVEL = 2;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;

	//public int xx,yy;

	/*public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	public Font newfont;*/

	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;

	//sistema de cutscene!
	public static int entrada = 1;
	public static int comecar = 2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	public int timeCena = 0,maxTimeCena = 60*3;

	public Menu menu;
	public static int[] lightMap;
	public static int[] minimapPixels;
	public static int[] pixels;

	public static BufferedImage minimapa;
	public BufferedImage lightMapImg;
	

	public boolean saveGame = false;
	
	public int mx,my;

	public Npc npc;

	public Game(){
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando objetos.
		ui = new UI();
		
		lightMap = new int[WIDTH*HEIGHT];
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		pixels =((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0,16,16));
		entities.add(player);
		world = new World("/level1.png");

		npc = new Npc(32, 32, 16, 16, spritesheet.getSprite(32, 32, 16, 16));

		entities.add(npc);

		try {
			lightMapImg = ImageIO.read(getClass().getResource("/lightmap.png"));
			lightMap = lightMapImg.getRGB(0, 0, lightMapImg.getWidth(), lightMapImg.getHeight(), null, 0, lightMapImg.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* 
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		*/
		
		minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();

		menu = new Menu();
	}
	
	public void initFrame(){
		frame = new JFrame("Game #1");
		frame.add(this);
		//frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		//icone da janela e do mouse
		/* 
		Image imagem = null;
		try {
		imagem = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(image, new Point(0,0), "img");

		frame.setCursor(c);
		frame.setIconImage(imagem);
		*/
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		if(gameState == "NORMAL") {
		if (this.saveGame) {
			this.saveGame = false;
			String[] opt1 = {"level"};
			int[] opt2 = {this.CUR_LEVEL};
			Menu.saveGame(opt1,opt2,10);
			System.out.println("Jogo Salvo! ");
		}
		this.restartGame = false;
		
		if (Game.estado_cena == Game.jogando) {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		}else{
			if (Game.estado_cena == Game.entrada) {
				if (player.getX() < 100) {
					player.x++;
				}else{
					System.out.println("Game entrada concluida!");
					Game.estado_cena = Game.comecar;
				}
			}else if (Game.estado_cena == Game.comecar) {
				timeCena++;
				if (timeCena == maxTimeCena) {
					Game.estado_cena = Game.jogando;
				}
			}
		}
		/* 
		if(enemies.size() == 0) {
			//Avancar para o proximo level!
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL){
				CUR_LEVEL = 1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			//System.out.println(newWorld);
			World.restartGame(newWorld);
		}
		*/
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
					else
						this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				Game.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				//System.out.println(newWorld);
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
	}
	
	/* 
	public void drawRectangleExample(int xoff, int yoff){
		for(int xx = 0; xx < 32; xx++){
			for(int yy = 0; yy < 32; yy++){
				int xOff = xx + xoff;
				int yOff = yy + yoff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
					continue;
				pixels[xOff + (yOff*WIDTH)] = 0xff0000;
			}
		}
	}
	*/

	public void applyLight(){
		
		for (int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++){
				if (lightMap[xx+yy*WIDTH] == 0xff000000) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x808080, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
		
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderizacao do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		applyLight();
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		// Aqui é onde renderizamos o jogo
		g.drawImage(image, 0, 0,/*Toolkit.getDefaultToolkit().getScreenSize().width*/WIDTH*SCALE, /*Toolkit.getDefaultToolkit().getScreenSize().height*/HEIGHT*SCALE,null);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Municao: " + player.ammo,580,20);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE,HEIGHT*SCALE);
			g.setFont(new Font("arial",Font.BOLD,36));
			g.setColor(Color.white);
			g.drawString("Game Over",(WIDTH*SCALE) / 2 - 90,(HEIGHT* SCALE) / 2 - 20);
			g.setFont(new Font("arial",Font.BOLD,32));
			if(showMessageGameOver)
				g.drawString(">Pressione Enter para reiniciar<",(WIDTH*SCALE) / 2 - 230,(HEIGHT* SCALE) / 2 + 40);
		}else if(gameState == "MENU") {
			menu.render(g);
		}

		if (Game.estado_cena == Game.comecar) {
			g.drawString("Ready...",(WIDTH*SCALE) / 2 - 90,(HEIGHT* SCALE) / 2 - 20);
		}

		World.renderMiniMap();
		g.drawImage(minimapa, 585, 345,World.WIDTH*4,World.HEIGHT*4, null);
		
		// teste de metodo de rotacao de objetos com o mouse
		/*
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2( 200+25 - my, 200+25 - mx);
		g2.rotate(angleMouse, 200+25, 200+25);
		System.out.println(angleMouse);
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 50);
		*/

		//adicionado metodo e teste da nova fonte
		/* 
		g.setFont(newfont);
		g.setColor(Color.red);
		g.drawString("Teste com a nova Fonte", 90, 90);
		*/
		bs.show();
	}
	
	public void run() {
		requestFocus();
		//Sound.music.play();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;
		}

		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_K){
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gameState == "NORMAL") {
				this.saveGame = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			npc.showMessage = false;
		}

		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// ...
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// ...
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// ...
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}

	
}
