import java.awt.Color;

public class M {

	public static float d2r(float degrees){
	
		return (float) (degrees / 180f * Math.PI);
		
	}
	
	public static int rgb2int(int r, int g, int b){
		
		return (r << 16) | (g << 8) | b;
		
	}
	
	public static Color int2rgb(int rgb){
		
		int r = (rgb & 0xff0000) >> 16;
		int g = (rgb & 0xff00) >> 8;
		int b = (rgb & 0xff);
		
		return new Color(r, g, b);
		
	}
	
	public static int getRed(int rgb){
		
		return (rgb & 0xff0000) >> 16;
		
	}
	
	public static int getGreen(int rgb){
		
		return (rgb & 0xff00) >> 8;
		
	}
	
	public static int getBlue(int rgb){
		
		return rgb & 0xff;
		
	}
	
	public static int getRandomColor(){
		
		return rgb2int(
				
				(int)(Math.random() * 256),
				(int)(Math.random() * 256),
				(int)(Math.random() * 256)
		);
		
	}
	
}
