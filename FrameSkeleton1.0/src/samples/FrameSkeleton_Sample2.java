
package samples;

import java.awt.event.KeyEvent;

import core.FrameGrid;
import core.FrameSkeleton;





/**
 * The FrameSkeleton_Sample2 program is a simple example for a DLA cluster built with the FrameSkeleton.
 * 
 * @author Yannik Alexander Eikmeier
 * @version 1.0
 * @since   2016-06-27 
 * 
 */
public class FrameSkeleton_Sample2 extends FrameSkeleton {

	int x = 0;
	int y = 0;
	static int size = 200;
	FrameGrid grid;
	
	int stuckCell = 1;
	int tempCell = 2;
	
	public FrameSkeleton_Sample2() {
		super(3, size, size);
		grid = getGrid();
	}
	
	@Override
	public void onNew() {	
		grid.resetGrid();
		grid.setCellType(size/2, size/2, stuckCell);
		newRNDPos();
		grid.setCellType(x, y, tempCell);
		printGrid();
	}

	
	@Override
	public void onRun() {
		step();

	}

	
	@Override
	public void onStep() {
		
		step();	
		
		printGrid();

	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	
	public static void main(String[] args) {
		new FrameSkeleton_Sample2();

	}
	
	public void move(){
		int r = (int) (Math.random()*4);
		switch (r) {
		case 0:
			if(x+1 < grid.getWidth()-1 /*&& grid.getCellType(x+1, y) != stuckCell*/)
				x+=1;
			break;
		case 1:
			if(y+1 < grid.getHeight()-1 /*&& grid.getCellType(x, y+1) != stuckCell*/)
				y+=1;
			break;
		case 2:
			if(x-1 > 0					 /*&& grid.getCellType(x-1, y) != stuckCell*/)
				x-=1;
			break;
		case 3:
			if(y-1 > 0					 /*&& grid.getCellType(x, y-1) != stuckCell*/)
				y-=1;
			break;

		default:
			break;
		}
	}
	

	private boolean isStuck() {	
		return grid.getCellType(x+1, y) == stuckCell || grid.getCellType(x, y+1) == stuckCell 
				|| grid.getCellType(x-1, y) == stuckCell || grid.getCellType(x, y-1) == stuckCell;
	}

	private void newRNDPos() {
		double r = Math.random()*2*Math.PI;
		x = (int) (Math.sin(r)*(size*0.45)) + size/2;
		y = (int) (Math.cos(r)*(size*0.45)) + size/2;
	}

	private void step() {
		if(isStuck()){
			grid.setCellType(x, y, stuckCell);			
			printGrid();
			newRNDPos();
		} else{
			grid.setCellType(x, y, 0);
			move();
			grid.setCellType(x, y, tempCell);
			
			
		}
	}

}
