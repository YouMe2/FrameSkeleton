
package samples;

import java.awt.event.KeyEvent;

import core.FrameGrid;
import core.FrameSkeleton;





/**
 * The FrameSkeleton_Sample2 program is a simple example for a DLA cluster built with the FrameSkeleton.
 * 
 * @author Yannik Alexander Eikmeier
 * @version 1.1
 * @since   2016-07-15 
 * 
 */
public class FrameSkeleton_Sample2 extends FrameSkeleton {		/* STEP 1 - ERBEN */

	int x = 0;
	int y = 0;
	static int gridsize = 200;
	FrameGrid grid;
	
	int stuckCell = 1;		/*Wert/Type der Zellen im Grid*/
	int tempCell = 2;		/*Wert/Type der Zellen im Grid*/
	
	int cell_counter = 0;
	int move_counter = 0;
	int avgMpC = 0;
	
	public static void main(String[] args) {					/* STEP 2 - WERTE des Grids INITIALISIEREN*/
		new FrameSkeleton_Sample2(3 /*celsize*/, gridsize /*width*/, gridsize /*high*/);
	}
	
	public FrameSkeleton_Sample2(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH);	
		grid = getGrid();										/* STEP 3 - GRID GETen */
																/* STEP 4 - PROGRAMMIEREN :) */
		setTitle("Frame Skeleton Sample2 - DLA Cluster");
		
		setOutputEnabled(0, true);
		setOutputEnabled(1, true);
		setOutputEnabled(2, true);
	}
	
	@Override
	public void onNew() {	
		grid.resetGrid();
		
		cell_counter = 0;
		move_counter = 0;
		avgMpC = 0;
		setOutputText(0, "Cells: "+cell_counter);	/*Cells*/
		setOutputText(1, "MpC: "+move_counter);		/*Moves Per last Cell*/
		setOutputText(2, "AvgMpC: "+avgMpC);		/*Average Moves Per Cell*/
		
		grid.setCellType(gridsize/2, gridsize/2, stuckCell); /*setzt die Strat-Zelle in der Mitte*/
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
	
	public void move(){
		int r = (int) (Math.random()*4);
		switch (r) {
		case 0:
			if(x+1 < grid.getWidth()-1){
				x+=1;
				move_counter++;
			}
			break;
		case 1:
			if(y+1 < grid.getHeight()-1){
				y+=1;
				move_counter++;
			}
			break;
		case 2:
			if(x-1 > 0){
				x-=1;
				move_counter++;
			}
			break;
		case 3:
			if(y-1 > 0){
				y-=1;
				move_counter++;
			}
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
		x = (int) (Math.sin(r)*(gridsize*0.45)) + gridsize/2;
		y = (int) (Math.cos(r)*(gridsize*0.45)) + gridsize/2;
		
		if(grid.getCellType(x, y) == stuckCell){
			try {
				newRNDPos();
			} catch (StackOverflowError e) {
				on_btnStop();				/*kleiner Trick um den Thread von innerhalb des Programmes zu stoppen, wenn das DLA Cluster stagniert*/
			}
		}
	}

	private void step() {
		if(isStuck()){
			
			
			avgMpC = (avgMpC*cell_counter + move_counter)/(cell_counter+1);  /*Berchnung der durchschnittlichen "Moves"*/
			cell_counter +=1;
			
			setOutputText(0, "Cells: "+cell_counter);
			setOutputText(1, "MpC: "+move_counter);
			setOutputText(2, "AvgMpC: "+avgMpC);
			
			grid.setCellType(x, y, stuckCell);			
			printGrid();
			
			newRNDPos();
			move_counter = 0;
		} else{
			grid.setCellType(x, y, 0);
			move();
			grid.setCellType(x, y, tempCell);
			
			
		}
	}

}
