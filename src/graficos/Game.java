package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 4;
	
	private BufferedImage image;
	
	private Spritesheet sheet;
	private BufferedImage player;
	
	//278x307 player width and height ################
	
	public Game() {
		sheet = new Spritesheet("/mago.jpg");
		player = sheet.getSprite(0, 0, 16, 16);
		setPreferredSize(new Dimension(WIDTH * SCALE , HEIGHT * SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame(){
		frame = new JFrame("Meu Jogo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		
		isRunning = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		
		Game game = new Game();
		game.start();
		
	}
	
	public void update() {
		
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(5, 5, 50));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, 1, 1);
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Olá mundo", 4, 90);
		
		/* Renderização do jogo */
		
		g.drawImage(player, 20, 20, null);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		bs.show();
	}
	
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfFrames = 60.0;
		double ns = 1000000000 / amountOfFrames;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				
				System.out.print("FPS: " + frames + " | ");
				frames = 0;
				timer += 1000;
				
			}
		}
		stop();
	}
	
}
