package samples;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import core1_2_2.ColorGrid;
import core1_2_2.FrameSkeleton;
import core1_2_2.ImageGrid;

/**
* The FrameSkeleton_MethodTest program provides a test for all methods of
* the FrameSkeleton(v1.2) and FrmaeGrid(v1.1)  classes.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.0
* @since   2016-07-15 
*/
public class FrameSkeleton_MethodTest1_2_2 extends FrameSkeleton {		/* STEP 1 - ERBEN */


	ColorGrid cGrid;
	ImageGrid iGrid;
	
	public static void main(String[] args) {						/* STEP 2 - WERTE des Grids INITIALISIEREN*/
		new FrameSkeleton_MethodTest1_2_2(50, 12, 12);
	}
	
	public FrameSkeleton_MethodTest1_2_2(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH, 1);
		cGrid = getColorGrid();											/* STEP 3 - GRID GETen */
		iGrid = getImageGrid(0);
	}

	@Override
	public void onNew() {
		System.out.println("NEW");
		
		this.setTitle("TEST TITLE");
		
		setGridlinesVisible(true);
		
		setKeyListenerEnabled(true);
		System.out.println( isKeyListenerEnabled() );
		
		setMouseListenerEnabled(true);
		System.out.println( isMouseListenerEnabled() );
		
		System.out.println( isStarted() );
		
		cGrid.fillCell(0, 0, 0);
		cGrid.fillCell(1, 1, 1);
		cGrid.fillCell(2, 2, 2);
		cGrid.fillCell(3, 3, 3);
		cGrid.fillCell(4, 4, 4);
		cGrid.fillCell(5, 5, 5);
		cGrid.fillCell(6, 6, 6);
		cGrid.fillCell(7, 7, 7);
		cGrid.fillCell(8, 8, 8);
		cGrid.fillCell(9, 9, 9);
		
		cGrid.addNewCellType(10, new Color((int) (Math.random()*256*256*256)), ColorGrid.SHAPE_CIRCLE);		
		cGrid.fillCell(10,10,10);
		
		cGrid.addNewCellType(11, Color.WHITE, ColorGrid.SHAPE_RECT);		
		cGrid.fillCell(11,11,11);
		
		System.out.println( cGrid.getCellType(10, 10));
		System.out.println( cGrid.getCellType(11, 11));
		
		cGrid.setGridBackground(Color.PINK);
		cGrid.reloadFullGridImage();	// MUSS SEIN NACH DEM MAN DEN BG VERÄNDERT //TODO
		
		
		iGrid.addNewCellTextures(1, 5, 16, 16, "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\cell_res1.png");
		
		iGrid.addNewCellTexture(6, "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\Dude.png");
		
		iGrid.fillCell(0, 0, 0); iGrid.fillCell(1, 0, 0); iGrid.fillCell(2, 0, 0);
		iGrid.fillCell(0, 1, 1); iGrid.fillCell(1, 1, 1); iGrid.fillCell(2, 1, 1);
		iGrid.fillCell(0, 2, 2); iGrid.fillCell(1, 2, 2); iGrid.fillCell(2, 2, 2);
		iGrid.fillCell(0, 3, 3); iGrid.fillCell(1, 3, 3); iGrid.fillCell(2, 3, 3);
		iGrid.fillCell(0, 4, 4); iGrid.fillCell(1, 4, 4); iGrid.fillCell(2, 4, 4);
		iGrid.fillCell(0, 5, 5); iGrid.fillCell(1, 5, 5); iGrid.fillCell(2, 5, 5);
		iGrid.fillCell(0, 6, 6, 45);
		
		printGrid();
		
		setOutputEnabled(0, true);
		setOutputEnabled(1, true);
		setOutputEnabled(2, true);
		
		setOutputText(0, "O TEST 0");
		setOutputText(1, "O TEST 1");
		setOutputText(2, "O TEST 2");
		
		setInputEnabled(0, true);
		setInputEnabled(1, true);
		setInputEnabled(2, true);
		
		setInputText(0, "I TEST 0");
		setInputText(1, "I TEST 1");
		setInputText(2, "I TEST 2");
		
		System.out.println(getInputText(0));
		System.out.println(getInputText(1));
		System.out.println(getInputText(2));
		
		setMessageText("This is a Message.");
		
		
	}

	@Override
	public void onRun() {
		System.out.println("RUN");
		
	}

	@Override
	public void onStep() {
		System.out.println("STEP");
		
		iGrid.fillCell(3, 3, 3, 45);
		printGrid();
		
		
		
		
		
		
	}

	@Override
	public void onStop() {
		System.out.println("STOPED");

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		System.out.println("P: "+ e.getKeyCode() );

		
		if(e.getKeyCode() == KeyEvent.VK_S){
			try {
		         FileOutputStream fileOut =
		         new FileOutputStream("cgrid.ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(cGrid);
		         out.close();
		         fileOut.close();
		         System.out.println("SERED grid");
		    }
			catch(IOException i) {
				i.printStackTrace();
		    }
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R){
			try
		      {
		         FileInputStream fileIn = new FileInputStream("cgrid.ser");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         cGrid = (ColorGrid) in.readObject();
		         in.close();
		         fileIn.close();
		         System.out.println("DE-SERED grid");
		         this.colorGrid = cGrid; //TODO
		         printGrid();
		      }catch(IOException i)
		      {
		         i.printStackTrace();
		         return;
		      }catch(ClassNotFoundException c)
		      {
		         System.out.println("iGrid class not found");
		         c.printStackTrace();
		         return;
		      }
		}
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		System.out.println("R: "+ e.getKeyCode() );

	}

	@Override
	public void onMousePressed(MouseEvent e) {
		
		iGrid.rotateCellTexture(e.getX()/50, e.getY()/50, 45);
		System.out.println("MouseP");
		printGrid();
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseDraged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
