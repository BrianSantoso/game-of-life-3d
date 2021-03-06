import java.util.ArrayList;

import javax.swing.JFrame;

public class Vertex {
	
	//public static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	public static final int NUM_VERTICES_PER_TRI = 3;
	public static float RADIUS = 1;

	
	
	
	
	
	private Matrix pos;
	private int rgb;
	
	public Vertex(Vector pos, int rgb){
		
		this.pos = pos.toMatrix();
		this.rgb = rgb;
		
	}
	
	public Vertex(Vector pos){
	
		this.pos = pos.toMatrix();
		this.rgb = M.rgb2int(
				
				(int)(Math.random() * 255),
				(int)(Math.random() * 255),
				(int)(Math.random() * 255)
		
		);
	
	}
	
	public Vertex(Matrix pos, int rgb){
		
		this.pos = pos;
		this.rgb = rgb;
		
	}
	
	
	public Matrix getPos() {
		return pos;
	}



	public void setPos(Vector pos) {
		this.pos = pos.toMatrix();
	}
	
	public void setPos(Matrix mat){
		this.pos = mat;
	}



	public int getRGB() {
		return rgb;
	}



	public void setArgb(int rgb) {
		this.rgb = rgb;
	}

	public float getX(){
		
		return this.pos.m[0][0];
		
	}
	
	public float getY(){
		
		return this.pos.m[1][0];
		
	}
	
	public float getZ(){
		
		return this.pos.m[2][0];
		
	}
	
	
	public float getW(){
		
		return this.pos.m[3][0];
		
	}
	
	
	public int[] toScreenCoordinates(Screen screen){
		
		return new int[]{
				
				(int) (getX() * screen.getWidth()),
				(int) (getY() * screen.getHeight()),
				(int) (getW())
				
		};
		
	}
	
	
	public String toString(){
		
		return "\n" + this.getPos() + "\n";
		
	}
	
	

	
	
}
