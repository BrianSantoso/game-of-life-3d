import java.util.ArrayList;

public class /*I have no*/Life {

	private int xSize;
	private int ySize;
	private int zSize;
	private boolean[][][] grid;
	private int[][] rules;
	private float tileSize;
	private Vector pos;
	
	public Life(Vector pos, int xSize, int ySize, int zSize, float tileSize){
		
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.tileSize = tileSize;
		this.pos = pos;
		
		grid = new boolean[xSize][ySize][zSize];
		
		rules = new int[27][2];
		
		rules[0] = new int[]{0, 0};
		rules[1] = new int[]{0, 0};
		rules[2] = new int[]{0, 0};
		rules[3] = new int[]{0, 0};
		rules[4] = new int[]{0, 0};
		
		rules[5] = new int[]{1, 0};
		rules[6] = new int[]{1, 1};
		rules[7] = new int[]{0, 1};
		
		rules[8] = new int[]{0, 0};
		rules[9] = new int[]{0, 0};
		rules[10] = new int[]{0, 0};
		rules[11] = new int[]{0, 0};
		rules[12] = new int[]{0, 0};
		rules[13] = new int[]{0, 0};
		rules[14] = new int[]{0, 0};
		rules[15] = new int[]{0, 0};
		rules[16] = new int[]{0, 0};
		rules[17] = new int[]{0, 0};
		rules[18] = new int[]{0, 0};
		rules[19] = new int[]{0, 0};
		rules[20] = new int[]{0, 0};
		rules[21] = new int[]{0, 0};
		rules[22] = new int[]{0, 0};
		rules[23] = new int[]{0, 0};
		rules[24] = new int[]{0, 0};
		rules[25] = new int[]{0, 0};
		rules[26] = new int[]{0, 0};
		
		
		setTile(4, 4, 4, true);
		setTile(5, 4, 4, true);
		setTile(4, 4, 5, true);
		setTile(5, 4, 5, true);
		setTile(4, 5, 4, true);
		setTile(5, 5, 4, true);
		setTile(4, 5, 5, true);
		
		//setRandom(0.2f);
		
	}
	
	public boolean allDead(){
		
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				for(int z = 0; z < zSize; z++){
				
					if(grid[x][y][z])
						return false;
					
				}
			}
		}
		
		return true;
	}
	
	public void setRandom(float percent){
		
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				for(int z = 0; z < zSize; z++){
				
					if(Math.random() < percent)
						setTile(x, y, z, true);
					
				}
			}
		}
		
	}
	
	public boolean inBounds(int x, int y, int z){
		
		return x >= 0 && x < xSize &&
				y >= 0 && y < ySize &&
				z >= 0 && z < zSize;
		
	}
	
	public void updateTile(int x, int y, int z){
		
		int howManyAround = howManyAround(x, y, z);
		int index = grid[x][y][z] ? 0 : 1;
		int whatToDo = rules[howManyAround][index];
		
		if(grid[x][y][z]){
			
			//if(whatToDo == -1)
			if(whatToDo == 0)
				setTile(x, y, z, false);
			
		} else {
			
			//if(howManyAround == 6 || howManyAround == 7)
			if(whatToDo == 1)
				setTile(x, y, z, true);
			
		}
		
	}
	
	public void setTile(int x, int y, int z, boolean bool){
		
		grid[x][y][z] = bool;
		
	}
	
	public int howManyAround(int x, int y, int z){
		
		int count = 0;
		
		for(int xt = x - 1; xt <= x + 1; xt++){
			for(int yt = y - 1; yt <= y + 1; yt++){
				for(int zt = z - 1; zt <= z + 1; zt++){
					
					if(!inBounds(xt, yt, zt))
						continue;
					
					if(xt == x && yt == y && zt == z)
						continue;
					
					if(grid[xt][yt][zt])
						count++;					
					
				}
			}
		}
		
		return count;
	}
	
	
	public ArrayList<Vertex> next(){
		
		//if(allDead())
		//	setRandom(0.2f);
		
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				for(int z = 0; z < zSize; z++){
				
					updateTile(x, y, z);
					
				}
			}
		}
		
		return getAllCubeVertices();
		
	}
	
	public ArrayList<Vertex> getAllCubeVertices(){
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				for(int z = 0; z < zSize; z++){
					
					if(grid[x][y][z]){
						vertices.addAll(getCubeVertices(x, y, z));
					}
				}
			}
		}
		
		return vertices;
		
	}
	
	public ArrayList<Vertex> getCubeVertices(int xt, int yt, int zt){
		
		// Cube Vertices
		ArrayList<Vertex> cv = new ArrayList<Vertex>();
		
		float t = tileSize;
		float s = 0.8f;
		float st = s * t;
		
		float x = xt * t;
		float y = yt * t;
		float z = -zt * t;
		
		Vertex[] corners = new Vertex[8];
		corners[0] = new Vertex(pos.plus(new Vector(x, y + st, z)));
		corners[1] = new Vertex(pos.plus(new Vector(x, y, z)));
		corners[2] = new Vertex(pos.plus(new Vector(x + st, y, z)));
		corners[3] = new Vertex(pos.plus(new Vector(x + st, y + st, z)));
		corners[4] = new Vertex(pos.plus(new Vector(x, y, z - st)));
		corners[5] = new Vertex(pos.plus(new Vector(x + st, y, z - st)));
		corners[6] = new Vertex(pos.plus(new Vector(x + st, y + st, z - st)));
		corners[7] = new Vertex(pos.plus(new Vector(x, y + st, z - st)));
		
		// FRONT
		//bottom left
		cv.add( corners[0] );
		cv.add( corners[1] );
		cv.add( corners[2] );
		
		//top right
		cv.add( corners[0] );
		cv.add( corners[2] );
		cv.add( corners[3] );
		
		// RIGHT
		//top left
		cv.add( corners[3] );
		cv.add( corners[2] );
		cv.add( corners[6] );
		
		//bottom right
		cv.add( corners[6] );
		cv.add( corners[2] );
		cv.add( corners[5] );
		
		// LEFT
		//bottom left
		cv.add( corners[7] );
		cv.add( corners[4] );
		cv.add( corners[1] );
		
		// top right
		cv.add( corners[0] );
		cv.add( corners[7] );
		cv.add( corners[1] );
		
		//BACK
		// top right
		cv.add( corners[6] );
		cv.add( corners[4] );
		cv.add( corners[7] );
		
		// bottom left
		cv.add( corners[6] );
		cv.add( corners[5] );
		cv.add( corners[4] );
		
		
		//TOP
		//bottom left
		cv.add( corners[7] );
		cv.add( corners[0] );
		cv.add( corners[3] );
		
		//top right
		cv.add( corners[7] );
		cv.add( corners[3] );
		cv.add( corners[6] );
		
		//BOTTOM
		//bottom left
		cv.add( corners[1] );
		cv.add( corners[4] );
		cv.add( corners[5] );
		
		//top right
		cv.add( corners[1] );
		cv.add( corners[5] );
		cv.add( corners[2] );
		
		return cv;
		
	}

	public int getxSize() {
		return xSize;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public int getzSize() {
		return zSize;
	}

	public void setzSize(int zSize) {
		this.zSize = zSize;
	}

	public boolean[][][] getGrid() {
		return grid;
	}

	public void setGrid(boolean[][][] grid) {
		this.grid = grid;
	}

	public int[][] getRules() {
		return rules;
	}

	public void setRules(int[][] rules) {
		this.rules = rules;
	}

	public float getTileSize() {
		return tileSize;
	}

	public void setTileSize(float tileSize) {
		this.tileSize = tileSize;
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}
	
}
