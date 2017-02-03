import java.util.ArrayList;

public class Renderer {

	private Camera camera;
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	private Screen screen;
	
	public Renderer(Screen screen, Camera camera, ArrayList<Vertex> vertices){
		
		this.screen = screen;
		this.camera = camera;
		this.vertices = vertices;
		/*
		for(int x = 0; x < screen.getWidth(); x ++){
			
			for(int y = 0; y < screen.getHeight(); y++){
				
				screen.setPixel(x, y, (int) Math.random() * (0xffffff + 1));
				
			}
			
		}
		*/
		
	}
	
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}
	
	public ArrayList<Vertex> vertexShader(Matrix mat, ArrayList<Vertex> vertices){
		
		ArrayList<Vertex> newVertices = new ArrayList<Vertex>();
		
		for(Vertex v : vertices){
			
			Matrix result = mat.multiply(v.getPos());
			newVertices.add(new Vertex(result, v.getRGB()));
			
		}
		
		return newVertices;
		
	}
	
	/*
	public ArrayList<Vertex> vertexShader(Matrix mat, ArrayList<Vertex> vertices, ArrayList<Vertex> mutatedList){
		
		for(Vertex v : vertices){
			
			Matrix result = mat.multiply(v.getPos());
			mutatedList.add(new Vertex(result, v.getRGB()));
			
		}
		
		return mutatedList;
		
	}*/
	
	
	public ArrayList<Vertex> perspectiveProjection(ArrayList<Vertex> vertices, Camera camera){
		
		Matrix projectionMatrix = camera.getProjectionMatrix();
		ArrayList<Vertex> projected = perspectiveDivision(vertexShader(projectionMatrix, vertices));
		
		return projected;
		
	}
	
	/*
	public ArrayList<Vertex> perspectiveProjection(ArrayList<Vertex> vertices, Camera camera, ArrayList<Vertex> mutatedList){
		
		Matrix projectionMatrix = camera.getProjectionMatrix();
		ArrayList<Vertex> homogenous = vertexShader(projectionMatrix, vertices, mutatedList);
		ArrayList<Vertex> projected = perspectiveDivision(homogenous, homogenous);
		
		return projected;
		
	}*/
	
	public ArrayList<Vertex> perspectiveDivision(ArrayList<Vertex> vertices){
		
		ArrayList<Vertex> newVertices = new ArrayList<Vertex>();
		
		for(Vertex v : vertices){
			
			float p = 1/v.getW();
			
			Matrix result = new Matrix(new float[][]{
				/*
				{p, 0, 0, 0},
				{0, p, 0, 0},
				{0, 0, p, 0},
				{0, 0, 0, p}*/
				
				{p, 0, 0, 0},
				{0, p, 0, 0},
				{0, 0, p, 0},
				{0, 0, 0, 1}
				
			}).multiply(v.getPos());
			
			newVertices.add(new Vertex(result, v.getRGB()));
			
		}
		
		return newVertices;
		
	}
	
	/*public ArrayList<Vertex> perspectiveDivision(ArrayList<Vertex> vertices, ArrayList<Vertex> homogenous){
		
		ArrayList<Vertex> newVertices = homogenous;
		
		for(Vertex v : vertices){
			
			float p = 1/v.getW();
			
			Matrix result = new Matrix(new float[][]{
				
				{p, 0, 0, 0},
				{0, p, 0, 0},
				{0, 0, p, 0},
				{0, 0, 0, p}
				
			}).multiply(v.getPos());
			
			newVertices.add(new Vertex(result, v.getRGB()));
			
		}
		
		return newVertices;
		
	}*/
	
	public ArrayList<Vertex> getTransformedVertices(){
		
		return vertexShader(camera.getTransformationMatrix(), vertices);
		
	}

	public void update(){
		
		camera.update();
		
	}
	
	public int[][] getPointsInTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3){
		
		ArrayList<int[]> pointsInTriangle = new ArrayList<int[]>();
		
		
		int[] v1 = vertex1.toScreenCoordinates(screen);
		int[] v2 = vertex2.toScreenCoordinates(screen);
		int[] v3 = vertex3.toScreenCoordinates(screen);
		
		//System.out.println(side3);
		
		int minX = Math.min(v1[0], Math.min(v2[0], v3[0]));
		int minY = Math.min(v1[1], Math.min(v2[1], v3[1]));
		
		int maxX = Math.max(v1[0], Math.max(v2[0], v3[0]));
		int maxY = Math.max(v1[1], Math.max(v2[1], v3[1]));
		
		
		Vector vert1 = new Vector(v1[0], v1[1], 0);
		Vector vert2 = new Vector(v2[0], v2[1], 0);
		Vector vert3 = new Vector(v3[0], v3[1], 0);
		
		Vector side1 = vert2.minus(vert1); //vector going from vert1 to vert 2
		Vector side2 = vert3.minus(vert2); //vector going from vert 2 to vert 3
		Vector side3 = vert1.minus(vert3); //vector going from vert 3 to vert 1
		
		
		
		for(int x = minX; x < maxX; x++){
			
			for(int y = minY; y < maxY; y++){
				
				Vector point = new Vector(x, y, 0);
				
				Vector vert1ToPoint = point.minus(vert1);
				
				//System.out.println(vert1ToPoint.cross(side1).z);
				
				if(vert1ToPoint.cross(side1).z > 0)
					continue;
				
				
				Vector vert2ToPoint = point.minus(vert2);
				if(vert2ToPoint.cross(side2).z > 0)
					continue;
				
				Vector vert3ToPoint = point.minus(vert3);
				if(vert3ToPoint.cross(side3).z > 0)
					continue;
				
				
				pointsInTriangle.add(new int[]{x, y, v1[2]
						
						//(int) (1/barycentrics(v1, v2, v3, new int[]{x, y})[2])
								
				});
				
			}
			
		}
		
		//System.out.println(pointsInTriangle);
		
		int[][] coords = new int[pointsInTriangle.size()][3];
		
		for(int rep = 0; rep < pointsInTriangle.size(); rep++){
			
			int[] pointInTriangle = pointsInTriangle.get(rep);
			coords[rep] = new int[]{
					pointInTriangle[0],
					pointInTriangle[1],
					pointInTriangle[2]
			};
			
		}
		
		return coords;
		
	}
	
	public int[][] getPointsOnTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3){
	
		ArrayList<int[]> tri = new ArrayList<int[]>();
		
		tri.addAll(bresenham(vertex1, vertex2));
		tri.addAll(bresenham(vertex1, vertex3));
		tri.addAll(bresenham(vertex2, vertex3));
		
		return tri.toArray(new int[0][0]);
		
	}
	
	public ArrayList<int[]> bresenham(Vertex vertex1, Vertex vertex2){
		/*
		int x0 = p1[0];
		int y0 = p1[1];
		int x1 = p2[0];
		int y1 = p2[1];
		
		int dy = y1 - y0;
		int dx = x1 - x0;
		int dy2 = 2 * dy;
		
		int decisionParameter = dy2 - dx;
		
		return null;
		*/
		
		//http://www.sanfoundry.com/java-program-bresenham-line-algorithm/
		ArrayList<int[]> line = new ArrayList<int[]>();
		
		int[] p1 = vertex1.toScreenCoordinates(screen);
		int[] p2 = vertex2.toScreenCoordinates(screen);
		
		int x0 = p1[0];
		int y0 = p1[1];
		int x1 = p2[0];
		int y1 = p2[1];
		
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
 
        int sx = x0 < x1 ? 1 : -1; 
        int sy = y0 < y1 ? 1 : -1; 
 
        int err = dx-dy;
        int e2;
 
        while (true) 
        {
            line.add(new int[]{x0, y0, p1[2]});
 
            if (x0 == x1 && y0 == y1) 
                break;
 
            //e2 = 2 * err;
            e2 = err << 1;
            if (e2 > -dy) 
            {
                err -= dy;
                x0 += sx;
            }
 
            if (e2 < dx) 
            {
                err += dx;
                y0 += sy;
            }
        }                                
        return line;
		
	}
	
	public float[] barycentrics(int[] v1, int[] v2, int[] v3, int[] point){
		
		float totalArea = areaOfTriangle(v1, v2, v3);
		
		float areaOfTriangle1 = areaOfTriangle(point, v2, v3);
		float areaOfTriangle2 = areaOfTriangle(point, v3, v1);
		//float areaOfTriangle3 = areaOfTriangle(point, v1, v2);
		float areaOfTriangle3 = totalArea - areaOfTriangle2 - areaOfTriangle1;
		
		
		return new float[]{
			
			areaOfTriangle1 / totalArea,
			areaOfTriangle2 / totalArea,
			areaOfTriangle3 / totalArea
			
		};
		
		
		
		
	}
	
	public int linearInterpolate(Vertex vertex1, Vertex vertex2, Vertex vertex3, int[] point){
		
		// triangles correspond to opposite vertex. Triangle 1 is opposite vertex 1 and shares that color.
		
		int[] v1 = vertex1.toScreenCoordinates(screen);
		int[] v2 = vertex2.toScreenCoordinates(screen);
		int[] v3 = vertex3.toScreenCoordinates(screen);
		
		/*float areaOfTriangle1 = areaOfTriangle(point, v2, v3);
		float areaOfTriangle2 = areaOfTriangle(point, v3, v1);
		float areaOfTriangle3 = areaOfTriangle(point, v1, v2);
		
		float totalArea = areaOfTriangle(v1, v2, v3);
		
		float[] a1 = new float[]{ 
				
				areaOfTriangle1 * M.getRed(vertex1.getRGB()),
				areaOfTriangle1 * M.getGreen(vertex1.getRGB()),
				areaOfTriangle1 * M.getBlue(vertex1.getRGB())
				
		};
		
		float[] a2 = new float[]{
				
				areaOfTriangle2 * M.getRed(vertex2.getRGB()),
				areaOfTriangle2 * M.getGreen(vertex2.getRGB()),
				areaOfTriangle2 * M.getBlue(vertex2.getRGB())
				
		};
		
		float[] a3 = new float[]{
				
				areaOfTriangle3 * M.getRed(vertex3.getRGB()),
				areaOfTriangle3 * M.getGreen(vertex3.getRGB()),
				areaOfTriangle3 * M.getBlue(vertex3.getRGB())
				
		};
		
		int averageRed = (int) ((a1[0] + a2[0] + a3[0])/totalArea);
		int averageGreen = (int) ((a1[1] + a2[1] + a3[1])/totalArea);
		int averageBlue = (int) ((a1[2] + a2[2] + a3[2])/totalArea);
		
		return M.rgb2int(averageRed, averageGreen, averageBlue);*/
		
		
		float[] barycentrics = barycentrics(v1, v2, v3, point);
		
		float[] a1 = new float[]{ 
				
				barycentrics[0] * M.getRed(vertex1.getRGB()),
				barycentrics[0] * M.getGreen(vertex1.getRGB()),
				barycentrics[0] * M.getBlue(vertex1.getRGB())
				
		};
		
		float[] a2 = new float[]{
				
				barycentrics[1] * M.getRed(vertex2.getRGB()),
				barycentrics[1] * M.getGreen(vertex2.getRGB()),
				barycentrics[1] * M.getBlue(vertex2.getRGB())
				
		};
		
		float[] a3 = new float[]{
				
				barycentrics[2] * M.getRed(vertex3.getRGB()),
				barycentrics[2] * M.getGreen(vertex3.getRGB()),
				barycentrics[2] * M.getBlue(vertex3.getRGB())
				
		};
		
		return M.rgb2int(
				
				(int)(a1[0] + a2[0] + a3[0]), 
				(int)(a1[1] + a2[1] + a3[1]),
				(int)(a1[2] + a2[2] + a3[2])
				
		);
		
		
	}
	
	public float areaOfTriangle(int[] v1, int[] v2, int[] v3){
		
		float determinant = new Matrix(new float[][]{
			
			{ v1[0], v1[1], 1},
			{ v2[0], v2[1], 1},
			{ v3[0], v3[1], 1},
			
			
		}).determinant();
		
		return Math.abs(determinant * 0.5f);
		
	}
	
	
	public void render() {
		
		ArrayList<Vertex> transformedVertices = getTransformedVertices();
		ArrayList<Vertex> projectedVertices = perspectiveProjection(transformedVertices, camera);

		//System.out.println(projectedVertices);
		
		/*
		for(Vertex v : projectedVertices){
			
			int[] coords = v.toScreenCoordinates(screen);
			//System.out.println(coords[0] + " , " + coords[1]);
			//screen.setPixel(coords[0], coords[1], v.getRGB());
			screen.setPixel(coords, v.getRGB());
			
		}*/
		
		for(int i = 0; i < projectedVertices.size(); i += 3){
			
			Vertex vertex1 = projectedVertices.get(i);
			Vertex vertex2 = projectedVertices.get(i + 1);
			Vertex vertex3 = projectedVertices.get(i + 2);
			
			if(vertex1.getX() < -1 || vertex1.getX() > 1 || vertex1.getY() < -1 || vertex1.getY() > 1 || vertex1.getW() < -1)
				continue;
			
			if(vertex2.getX() < -1 || vertex2.getX() > 1 || vertex2.getY() < -1 || vertex2.getY() > 1 || vertex2.getW() < -1)
				continue;
			
			if(vertex3.getX() < -1 || vertex3.getX() > 1 || vertex3.getY() < -1 || vertex3.getY() > 1 || vertex3.getW() < -1)
				continue;
			
			
			/*if(vertex1.getW() < 1 || vertex2.getW() < 1 || vertex3.getW() < 1)
				continue;
			*/
			
			/*
			int[][] pointsInTriangle = getPointsInTriangle(
					
					vertex1.toScreenCoordinates(screen),
					vertex2.toScreenCoordinates(screen),
					vertex3.toScreenCoordinates(screen)
					
			);
			*/
			
			
			
			
			
			/*
			int[][] pointsInTriangle = getPointsInTriangle(
					
					vertex1,
					vertex2,
					vertex3
					
			);*/
			
			int[][] pointsInTriangle = getPointsOnTriangle(
					
					vertex1,
					vertex2,
					vertex3
					
			);
			
			//System.out.println(pointsInTriangle.length);
			
			for(int[] coord : pointsInTriangle){
				
				//screen.setPixel(coord, M.getRandomColor());
				
				
				
				
				
				
				//screen.setPixel(coord, 0xff0000);
				
				screen.setPixel(coord, linearInterpolate(
						
						vertex1,
						vertex2,
						vertex3,
						coord
						
				));
				
				
			}
			
		}
		
	}
	
	
	//
	// System.out.println(Vertex.perspectiveProjection(transformedVertices, camera));
	//
	
	
	
}
