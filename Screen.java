
public class Screen {

	private int width;
	private int height;
	public int[][] pixels;
	public int[][] depthBuffer;
	
	public Screen(int width, int height){
		
		this.width = width;
		this.height = height;
		pixels = new int[width][height];
		depthBuffer = new int[width][height];
		clear();
		
	}
	
	public void clear(){
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
			
				pixels[x][y] = 0x000000;
				depthBuffer[x][y] = Integer.MAX_VALUE;
				
			}
		}
		
	}
	
	public void draw(){
		
		clear();
		//setPixel(5, 5, 0xff0000);
		//setPixel(10, 10, 0x00ff00);
		
		
		/*
		for(int x = 0; x < width; x ++){
			
			for(int y = 0; y < height; y++){
				
				setPixel(x, y, (int) (Math.random() * (2<<24)));
				
			}
			
		}*/
		
	}
	
	public void setPixel(int x, int y, int depth, int color){
		
		int newY = getHeight() - y;
		
		if(x >= 0 && x < width && newY >= 0 && newY < height){
			
			if(depth < depthBuffer[x][newY]){
				pixels[x][newY] = color;
				depthBuffer[x][newY] = depth;
			}
			
		}
		
	}
	
	public void setPixel(int[] screenCoordinates, int color){
		
		int x = screenCoordinates[0] + width/2;
		int y = screenCoordinates[1] + height/2;
		int depth = screenCoordinates[2];
		
		//System.out.println(x + " , " + y);
		
		setPixel(x, y, depth, color);
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int[] toOneDimensionalArray(){
		
		int[] oneDim = new int[width * height];
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				
				oneDim[x + y * width] = pixels[x][y];
				
			}
		}
		
		return oneDim;
		
	}

	
}
