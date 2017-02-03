import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{

	private boolean[] keys;
	public boolean up, down, left, right, forward, backward, lookLeft, lookRight, lookUp, lookDown, test;
	
	public Keyboard(){
		
		keys = new boolean[130];
		
	}
	
	public void keyInputs(){
		
		forward = keys[KeyEvent.VK_W];
		backward = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		up = keys[KeyEvent.VK_SPACE];
		down = keys[KeyEvent.VK_SHIFT];
		
		lookLeft = keys[KeyEvent.VK_LEFT];
		lookRight = keys[KeyEvent.VK_RIGHT];
		lookUp = keys[KeyEvent.VK_UP];
		lookDown = keys[KeyEvent.VK_DOWN];
		
		test = keys[KeyEvent.VK_F12] || keys[KeyEvent.VK_R];
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		keys[e.getKeyCode()] = false;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	
	
}
