package samples;

import java.awt.event.KeyEvent;

import core.FrameGrid;
import core.FrameSkeleton;



/**
* The FrameSkeleton_Sample1 program is a simple example for a Game built with the FrameSkeleton.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.1
* @since   2016-07-15 
*/
public class FrameSkeleton_Sample1 extends FrameSkeleton {		/* STEP 1 - ERBEN */

	int x = 0;
	int y = 0;
	int player = 4;
	int wall = 1;
	int coin = 6;
	
	int coin_counter = 0;
	int move_counter = 0;
	
	int walls = 50;
	int coins = 5;
	
	FrameGrid grid;
	
	
															
	public static void main(String[] args) {					/* STEP 2 - WERTE des Grids INITIALISIEREN*/
		new FrameSkeleton_Sample1(30 /*celsize*/, 15 /*width*/, 15 /*high*/);
	}
	
	public FrameSkeleton_Sample1(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH);	
		grid = getGrid();										/* STEP 3 - GRID GETen */
																/* STEP 4 - PROGRAMMIEREN :) */
		setTitle("Frame Skeleton Sample1 - A Game");
		
		setOutputEnabled(0, true);
		setOutputEnabled(1, true);
		
		setInputEnabled(0, true);
		setInputEnabled(1, true);
		
		setInputText(0, "Walls: " + walls);
		setInputText(1, "Coins: " + coins);
		
	}

	@Override
	public void onNew() {
		
		grid.resetGrid();
		walls = Integer.valueOf(getInputText(0).replace("Walls: ", ""));
		coins = Integer.valueOf(getInputText(1).replace("Coins: ", ""));
		move_counter = -1;
		coin_counter = 0;
		setMessageText("Try your best to get all the coins!");
		
		
		
		for (int i = 0; i < walls; i++) {
			grid.setCellType((int)(Math.random()*15), (int)(Math.random()*15), wall);
		}
		for (int i = 0; i < coins; i++) {
			grid.setCellType((int)(Math.random()*15), (int)(Math.random()*15), coin);
		}

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
		if(e.getKeyCode() == KeyEvent.VK_UP){
			movePlayer(x, y-1);
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if(y+1 < grid.getHeight() && grid.getCellType(x, y+1) != wall)
				movePlayer(x, y+1);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(x != 0 && grid.getCellType(x-1, y) != wall)
			movePlayer(x-1, y);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
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
		
		if(grid.getCellType(nx, ny) == coin)
			coin_counter++;
		
		grid.setCellType(x = nx, y = ny, player);
		printGrid();
		setOutputText(1, "moves:"+ ++move_counter);
		setOutputText(0, "coins: " + coin_counter);
		
		if(coin_counter == coins)
			setMessageText("Good Job! You won with only "+move_counter+" moves!");
	}


}
