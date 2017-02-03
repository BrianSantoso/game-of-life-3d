
public class Camera {
	
	public static float time = 0;

	private Vector pos;
	private Vector rotation;
	private float aspectRatio;
	private float fov;
	private Keyboard input;
	private float speed;
	private float lookSpeed;
	
	public Camera(Keyboard input, Vector pos, Vector rotation, float fov){
		
		this.pos = pos;
		this.rotation = rotation;
		this.fov = fov;
		this.input = input;
		
		speed = 0.1f;
		lookSpeed = M.d2r(2f);
		
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public Vector getRotation() {
		return rotation;
	}

	public void setRotation(Vector rotation) {
		this.rotation = rotation;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}
	
	public Matrix getTransformationMatrix(){
		
		Matrix result;
		
		Matrix translation = Matrix.translationMatrix(-pos.x, -pos.y, -pos.z);
		Matrix xRotation = Matrix.xAxisRotationMatrix(-rotation.x);
		Matrix yRotation = Matrix.yAxisRotationMatrix(-rotation.y);
		Matrix zRotation = Matrix.zAxisRotationMatrix(-rotation.z);
		
		//result = translation.multiply(xRotation).multiply(yRotation).multiply(zRotation);
		result = xRotation.multiply(yRotation).multiply(zRotation).multiply(translation);
		
		return result;
	}
	
	public Matrix getProjectionMatrix(){
		
		return new Matrix(new float[][]{
			
			{1,  0,  0,  0},
			{0,  1,  0,  0},
			{0,  0,  1,  0},
			{0,  0,  -1, 0}
			
		});
		
	}
	
	public void keyInputs(){
		
		
		
		if(input.lookLeft)
			rotation = new Vector(rotation.x, rotation.y + lookSpeed, rotation.z);
		if(input.lookRight)
			rotation = new Vector(rotation.x, rotation.y - lookSpeed, rotation.z);
		if(input.lookUp)
			rotation = new Vector(rotation.x + lookSpeed, rotation.y, rotation.z);
		if(input.lookDown)
			rotation = new Vector(rotation.x - lookSpeed, rotation.y, rotation.z);
		
		if(input.forward)
			//pos = new Vector(Matrix.translationMatrix(pos.x, pos.y, pos.z - speed).multiply(Matrix.yAxisRotationMatrix(rotation.y)).multiply(pos.toMatrix()));
			pos = new Vector(pos.x, pos.y, pos.z - speed);
		if(input.backward)
			pos = new Vector(pos.x, pos.y, pos.z + speed);
		if(input.left)
			pos = new Vector(pos.x - speed, pos.y, pos.z);
		if(input.right)
			pos = new Vector(pos.x + speed, pos.y, pos.z);
		if(input.up)
			pos = new Vector(pos.x, pos.y + speed, pos.z);
		if(input.down)
			pos = new Vector(pos.x, pos.y - speed, pos.z);
		
	}
	
	public void update(){
		
		//time+=1;
		
		//System.out.println(time);
		
		//setPos(new Vector(0, 0, time));
		//setRotation(new Vector(0, M.d2r(time), 0));
		//setRotation(new Vector(0, 0, M.d2r(time)));
		
	}
	
	
}
