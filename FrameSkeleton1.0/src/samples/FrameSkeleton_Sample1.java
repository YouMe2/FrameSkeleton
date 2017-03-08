package samples;

import java.awt.event.KeyEvent;

import core.FrameGrid;
import core.FrameSkeleton;



/**
* The FrameSkeleton_Sample1 program is a simple example for a Game built with the FrameSkeleton.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.0
* @since   2016-06-22 
*/
public class FrameSkeleton_Sample1 extends FrameSkeleton {

	int x = 0;
	int y = 0;
	int player = 4;
	int wall = 1;
	int coin = 6;
	
	FrameGrid grid;
	
	public static void main(String[] args) {
		new FrameSkeleton_Sample1();
	}
	
	public FrameSkeleton_Sample1() {
		super(30, 15, 15);
		grid = getGrid();
	}

	@Override
	public void onNew() {
		
		grid.resetGrid();
		
		for (int i = 0; i < 50; i++) {
			grid.setCellType((int)(Math.random()*15), (int)(Math.random()*15), wall);
		}
		for (int i = 0; i < 5; i++) {
			grid.setCellType((int)(Math.random()*15), (int)(Math.random()*15), coin);
		}
		
		setKeyListenerEnabled(true);

		movePlayer(5,5);
	}

	@Override
	public void onRun() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onKeyPressed(KeyEvent e) {
		if(y != 0 && grid.getCellType(x, y-1) != wall)
		if(e.getKeyCode() == KeyEvent.VK_W){
			movePlayer(x, y-1);
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			if(y+1 < grid.getHeight() && grid.getCellType(x, y+1) != wall)
				movePlayer(x, y+1);
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			if(x != 0 && grid.getCellType(x-1, y) != wall)
			movePlayer(x-1, y);
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			if(x+1 < grid.getWidth() && grid.getCellType(x+1, y) != wall)
			movePlayer(x+1, y);
		}

	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void movePlayer(int nx, int ny){
		grid.setCellType(x, y, 0);
		grid.setCellType(x = nx, y = ny, player);
		printGrid();
	}


}
