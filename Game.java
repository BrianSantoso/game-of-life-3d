import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	private final int width;
	private final int height;
	//public static int height = width / 16 * 9;
	
	private Thread thread;
	private JFrame frame;
	private boolean running;
	
	private BufferedImage image;
	private int[] pixels;
	private Screen screen;
	
	private Renderer renderer;
	
	private Keyboard keyboard;
	
	private long now, last;
	private float dt, accumulation;
	private float accumulation2;
	private final float step;
	private final float step2;
	
	private Life life;
	
	public Game(float step, String title){
		
		width = 800;
		height = 800;
		
		now = System.currentTimeMillis();
		last = 0;
		dt = 0;
		accumulation = 0;
		accumulation2 = 0;
		step2 = 0.5f;
		this.step = step;
		
		running = false;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		screen = new Screen(width, height);
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		keyboard = new Keyboard();
		addKeyListener(keyboard);
		
		
		
		
		
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		/*
		vertices.add(   new Vertex(new Vector(2, 0, -5), 0xff0000)   );
		vertices.add(   new Vertex(new Vector(0, 1, -5), 0x00ff00)   );
		vertices.add(   new Vertex(new Vector(-1, 0, -5), 0x0000ff)   );
		*/
		
		/*
		float t = 0.2f;
		
		
		vertices.add( new Vertex(new Vector(0, 0, -3), 0xff0000) );
		vertices.add( new Vertex(new Vector(0, t, -3), 0xff0000) );
		vertices.add( new Vertex(new Vector(-t, 0, -3), 0xff0000) );
		
		vertices.add( new Vertex(new Vector(0, 0, -5), 0x00ff00) );
		vertices.add( new Vertex(new Vector(0, t, -5), 0x00ff00) );
		vertices.add( new Vertex(new Vector(-t, 0, -5), 0x00ff00) );
		
		vertices.add( new Vertex(new Vector(0, t, -5), 0x0000ff) );
		vertices.add( new Vertex(new Vector(-t, t, -5), 0x0000ff) );
		vertices.add( new Vertex(new Vector(-t, 0, -5), 0x0000ff) );
		
		
		
		vertices.add( new Vertex(new Vector(0, 0, -5-t)) );
		vertices.add( new Vertex(new Vector(0, 0, -5)) );
		vertices.add( new Vertex(new Vector(-t, 0, -5)) );
		
		vertices.add( new Vertex(new Vector(-t, 0, -5-t), 0xff0000) );
		vertices.add( new Vertex(new Vector(0, 0, -5-t), 0x00ff00) );
		vertices.add( new Vertex(new Vector(-t, 0, -5), 0x0000ff) );*/
		
		
		//life = new Life(new Vector(0, 0, -30), 30, 30, 30, 0.5f);
		life = new Life(
				
				new Vector(-5, -5, -15),
				10, 10, 10,
				1f
				
		);
		
		vertices.addAll(life.getAllCubeVertices());
		
		
		
		
		
		Camera camera = new Camera(keyboard, new Vector(0, 0, 0), new Vector(0, M.d2r(0), 0), 90);
		
		renderer = new Renderer(screen, camera, vertices);
		
		
		
	}
	
	public synchronized void start(){
		
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
		
	}
	
	public synchronized void stop(){
		
		running = false;
		try{
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
	public void keyInputs(){
		
		keyboard.keyInputs();
		
		if(keyboard.test){
			life.setRandom(0.2f);
			renderer.setVertices(life.next());
		}
		
		renderer.getCamera().keyInputs();
		
	}
	
	public void update(){
		
		//ArrayList<Vertex> transformedVertices = Vertex.shader(renderer.camera.getTransformationMatrix(), Vertex.vertices);
		renderer.update();
		
	}
	
	public void draw(){
		
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);	//triple buffering
			return;
		}
		
		screen.draw();
		renderer.render();
		//pixels = screen.toOneDimensionalArray();
		
		for(int y = 0; y < screen.getHeight(); y++){
			for(int x = 0; x < screen.getWidth(); x++){
				
				pixels[x + y * screen.getWidth()] = screen.pixels[x][y];
				
			}
		}
		
		Graphics g = bs.getDrawGraphics(); //create link between Graphics and BufferStrategy
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose(); //remove Graphics from each frame
		bs.show(); //make next available buffer visible
		
		
	}

	public void run(){
		
		while(running){
			
			now = System.currentTimeMillis();
			dt = Math.min((now - last)/1000f, 1);
			accumulation += dt;
			accumulation2 += dt;
			
			
			
			
			while(accumulation >= step){
				
				keyInputs();
				update();
				accumulation -= step;
				
			}
			
			if(accumulation2 >= step2){
				
				renderer.setVertices(life.next());
				accumulation2 %= step2;
				
			}
			
			draw();
			
			last = now;
			
			
		}
		
	}
	
	public static void main(String[] args){
		
		/*
		
		JFrame frame = new JFrame("Wireframe");
		Panel panel = new Panel(800, 800);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		*/
		
		
		
		
		Matrix m0 = new Matrix(new float[][]{
			
			{1, 2, 3, 4},
			{1, 2, 3, 4},
			{1, 2, 3, 4}

		});
		
		Matrix m1 = new Matrix(new float[][]{
			
			{200, 1, 3, 9},
			{12345, 1, 3, 4},
			{5, 200, 3, 4},
			{5, 1, 3, 4},

			
		});
		
		Matrix m2 = new Matrix(new float[][]{
			
			{1, 2, 3, 4},
			{1, 2, 3, 4},
			{1, 2, 3, 4}

			
		});
		
		Matrix m3 = new Matrix(new float[][]{
			
			{7},
			{7},
			{7},
			{7}
			
		});
		
		//System.out.println(m1.determinant());
		
		//camera.setRotation(new Vector(0, (float) (Math.PI/4), 0));
		
		//System.out.println( camera.getTransformationMatrix() );
		//System.out.println(  camera.getProjectionMatrix()  );
		
		//System.out.println(  camera.getTransformationMatrix().multiply(new Vector(0, 0, -5).toMatrix())  );
		
		//ArrayList<Vertex> transformedVertices = Vertex.shader(camera.getTransformationMatrix(), Vertex.vertices);
		//System.out.println(Vertex.perspectiveProjection(transformedVertices, camera));
		
		
		
		
		
		
		Game game = new Game(1f/60f, "Wireframe");
		game.start();
		/*
		Vertex v3 = new Vertex( new Vector(0, 0, 0), 0xff00 );
		Vertex v1 = new Vertex( new Vector(0, 3, 0), 0xff00 );
		Vertex v2 = new Vertex( new Vector(-20, 0, 0), 0xff00 );
		
		System.out.println(game.renderer.areaOfTriangle(v1, v2, v3));
		*/
	}
	
}
