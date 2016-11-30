package samples;

import java.awt.Color;
import java.awt.event.KeyEvent;

import core.FrameGrid;
import core.FrameSkeleton;

/**
* The FrameSkeleton_MethodTest program provides a test for all methods of
* the FrameSkeleton(v1.2) and FrmaeGrid(v1.1)  classes.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.0
* @since   2016-07-15 
*/
public class FrameSkeleton_MethodTest extends FrameSkeleton {		/* STEP 1 - ERBEN */
 

	FrameGrid grid;
	
	public static void main(String[] args) {						/* STEP 2 - WERTE des Grids INITIALISIEREN*/
		new FrameSkeleton_MethodTest(20, 12, 12);
	}
	
	public FrameSkeleton_MethodTest(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH);
		grid = getGrid();											/* STEP 3 - GRID GETen */
	}

	@Override
	public void onNew() {
		System.out.println("NEW");
		
		this.setTitle("TEST TITLE");	
		
		setGridlinesVisible(true);						/* Gitterlinien aktivieren */
		
		setKeyListenerEnabled(true);					/* Keylistener aktivieren */
		System.out.println( isKeyListenerEnabled() );	/* Keylistener checken */
		
		System.out.println( isStarted() );				/* auf laufenden Thread checken */
		
		grid.setCellType(0, 0, 0);						/* "leere"/Hintergrunds Zelle setzten*/
		grid.setCellType(1, 1, 1);						/* gefüllte Zelle setzten*/
		grid.setCellType(2, 2, 2);						/* gefüllte Zelle setzten*/
		grid.setCellType(3, 3, 3);						/* gefüllte Zelle setzten*/
		grid.setCellType(4, 4, 4);						/* gefüllte Zelle setzten*/
		grid.setCellType(5, 5, 5);						/* gefüllte Zelle setzten*/
		grid.setCellType(6, 6, 6);						/* gefüllte Zelle setzten*/
		grid.setCellType(7, 7, 7);						/* gefüllte Zelle setzten*/
		grid.setCellType(8, 8, 8);						/* gefüllte Zelle setzten*/
		grid.setCellType(9, 9, 9);						/* gefüllte Zelle setzten*/
		
		grid.newCellType(10, new Color((int) (Math.random()*256*256*256)), FrameGrid.SHAPE_CIRCLE);		/* neuen Zelltyp erstellen */
		grid.setCellType(10,10,10);																		/* neuen Zelltyp setzten*/
		
		grid.newCellType(11, Color.WHITE, FrameGrid.SHAPE_RECT);										/* neuen Zelltyp erstellen */
		grid.setCellType(11,11,11);																		/* neuen Zelltyp setzten*/
		
		System.out.println( grid.getCellType(10, 10));			/* Zelltyp checken*/
		System.out.println( grid.getCellType(11, 11));			/* Zelltyp checken*/
		
		grid.setGridBackground(Color.ORANGE);						/* BG Color verändern (Standard: WHITE)*/
		
		printGrid();									/* Grid anzeigen */
		
		setOutputEnabled(0, true);						/*Output Nr.0 aktivieren */
		setOutputEnabled(1, true);
		setOutputEnabled(2, true);
		
		setOutputText(0, "O TEST 0");					/*Output Nr.1 setten */
		setOutputText(1, "O TEST 1");
		setOutputText(2, "O TEST 2");
		
		setInputEnabled(0, true);						/*Input Nr.0 aktivieren*/
		setInputEnabled(1, true);
		setInputEnabled(2, true);
		
		setInputText(0, "I TEST 0");					/*Input Nr.0 setten*/
		setInputText(1, "I TEST 1");
		setInputText(2, "I TEST 2");
		
		System.out.println(getInputText(0));			/*Input Nr.0 getten*/
		System.out.println(getInputText(1));
		System.out.println(getInputText(2));
		
		setMessageText("This is a Message.");			/* Message setzten */
		
		
	}

	@Override
	public void onRun() {
		System.out.println("RUN");

	}

	@Override
	public void onStep() {
		System.out.println("STEP");

	}

	@Override
	public void onStop() {
		System.out.println("STOPED");

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		System.out.println("P: "+ e.getKeyCode() );

	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		System.out.println("R: "+ e.getKeyCode() );

	}

	

}
