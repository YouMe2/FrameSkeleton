package samples;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;

import core1_2_4.TextureType;
import core1_2_4.FrameSkeleton;
import core1_2_4.ImageGrid;
import core1_2_4.ImageSpace;
import core1_2_4.Texture;

/**
* The FrameSkeleton_MethodTest program provides a test for all methods of
* the FrameSkeleton(v1.2) and FrmaeGrid(v1.1)  classes.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.0
* @since   2016-07-15 
*/
public class FrameSkeleton_MethodTest1_2_4 extends FrameSkeleton implements Observer{		/* STEP 1 - ERBEN */


	ImageGrid iGrid;
	ImageSpace iSpace;
	
	public static void main(String[] args) {						/* STEP 2 - WERTE des Grids INITIALISIEREN*/
		new FrameSkeleton_MethodTest1_2_4(50, 12, 12);
	}
	
	public FrameSkeleton_MethodTest1_2_4(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH, 1);										
		iGrid = getImageGrid(0);									/* STEP 3 - GRID GETen */
		iSpace = getImageSpace();
		iSpace.addObserver(this);
	}

	@Override
	public void onNew() {
		System.out.println("NEW");
		
		this.setTitle("TEST TITLE");
		
//		setGridlineVisible(true);
		
		setKeyInputEnabled(true);
		System.out.println( isKeyInputEnabled() );
		
		setMouseListenerEnabled(true);
		System.out.println( isMouseListenerEnabled() );
		
		System.out.println( isStarted() );
		
		
		iGrid.setBackgroundColor(Color.WHITE);
		
		
		TextureType[] ground = iGrid.addNewImageTextureTypes("ground", 5, 16, 16, "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\cell_res1.png");
		
		TextureType[] ent = iSpace.addNewImageTextureTypes("ent", 3, 32, 32, "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\cell_res2.png");
		TextureType tree = ent[2];
		TextureType player0 = ent[0];
		TextureType player1 = ent[1];
		
		TextureType[] item = iSpace.addNewImageTextureTypes("item", 3, 32, 32, "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\cell_res3.png");
		TextureType shild0 = item[0];
		TextureType shild1 = item[1];
		TextureType sword = item[2];
		
		TextureType dude = iSpace.addNewImageTextureType("dude", "C:\\Users.\\Anwender\\Google Drive\\devel\\workspace-jee\\FrameSkeleton1.2\\res\\Dude.png");
		
		for (int i = 0; i <iGrid.getWidth(); i++) {
			for (int j = 0; j < iGrid.getHeight(); j++) {
				
				iGrid.fillCell(ground[1], i, j);
				
			}
		}
		
		
		iGrid.fillCell("ground_0", 0, 0); iGrid.fillCell("ground_0", 1, 0); iGrid.fillCell(ground[0], 2, 0);
		iGrid.fillCell("ground_1", 0, 1); iGrid.fillCell("ground_1", 1, 1); iGrid.fillCell(ground[1], 2, 1);
		iGrid.fillCell("ground_2", 0, 2); iGrid.fillCell("ground_2", 1, 2); iGrid.fillCell(ground[2], 2, 2);
		iGrid.fillCell("ground_3", 0, 3); iGrid.fillCell("ground_3", 1, 3); iGrid.fillCell(ground[3], 2, 3);
		iGrid.fillCell("ground_4", 0, 4); iGrid.fillCell("ground_4", 1, 4); iGrid.fillCell(ground[4], 2, 4);

		
		
		
		Texture player0T = iSpace.putTexture(player1, 6, 6, 1.5, 1.5);
		Texture sword0T = iSpace.putTexture(sword, 6, 6, 1.5, 1.5);
		Texture shild0T = iSpace.putTexture(shild1, 6, 6, 1.5, 1.5);
		
		iSpace.putTexture(tree, 3, 8, 2, 2);
		iSpace.putTexture(tree, 8, 3, 1, 1);
		
		
		renderGrid();
		
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
		
		addKeyInputAction("pressed D", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				enableKeyAction("pressed D", false);		//ONLY ONCE per key press fired action!
				System.out.println("   D");
			}
		});
		
		addKeyInputAction("pressed S", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("   S");
				
			}
		});
		
		addKeyInputAction("released D", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				enableKeyAction("pressed D", true);		// reenable the action
				System.out.println("Re D");
				
			}
		});
		
		addKeyInputAction("released S", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Re S");
				
			}
		});
		
		addKeyInputAction("UP", new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {			
				player0T.movePosition(player0T.getX(), player0T.getY()-0.3);
				sword0T.movePosition(player0T.getX(), player0T.getY());
				shild0T.movePosition(player0T.getX(), player0T.getY());
			}
		});
		addKeyInputAction("DOWN", new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {			
				player0T.movePosition(player0T.getX(), player0T.getY()+0.3);
				sword0T.movePosition(player0T.getX(), player0T.getY());
				shild0T.movePosition(player0T.getX(), player0T.getY());
			}
		});
		addKeyInputAction("LEFT", new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {			
				player0T.movePosition(player0T.getX()-0.3, player0T.getY());
				sword0T.movePosition(player0T.getX(), player0T.getY());
				shild0T.movePosition(player0T.getX(), player0T.getY());
			}
		});
		addKeyInputAction("RIGHT", new AbstractAction() {		
			@Override
			public void actionPerformed(ActionEvent e) {			
				player0T.movePosition(player0T.getX()+0.3, player0T.getY());
				sword0T.movePosition(player0T.getX(), player0T.getY());
				shild0T.movePosition(player0T.getX(), player0T.getY());
			}
		});
		
		
	}

	@Override
	public void onRun() {
		System.out.println("RUN");
		renderGrid();
	}

	@Override
	public void onStep() {
		System.out.println("STEP");
		
		
		setGridlineVisible(!isGridlineVisible());
		
//		setKeyInputEnabled(!isKeyInputEnabled());
		
		
		
	}

	@Override
	public void onStop() {
		System.out.println("STOPED");

	}

	@Override
	public void onMousePressed(MouseEvent e) {
		
		iGrid.rotateCellImage(e.getX()/50, e.getY()/50, 45);
		System.out.println("MouseP");
		renderGrid();
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

	@Override
	public void update(Observable o, Object arg) {
		renderGrid();
		
	}

	

}
