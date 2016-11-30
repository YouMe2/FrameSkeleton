package core1_2_2;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
* The FrameGrid class provides basic functionalities of a Grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.2
* @since   2016-
*/
//TODO
public class ImageGrid implements Serializable{
	
	
	
	private int[][] grid_textureID;
	private int[][] grid_rotation; 
	private transient boolean[][] grid_updateFlag; 
	
	private int gridW;
	private int gridH;
	
	private transient Map<Integer, Image> cellTextures = new HashMap<Integer, Image>();
	
	private Color bgColor = null;
	
	private transient BufferedImage gridImage;
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ImageGrid(int w, int h) { 
		grid_textureID = new int[w][h];
		grid_rotation = new int[w][h];
		grid_updateFlag = new boolean[w][h];
		this.gridW = w;
		this.gridH = h;
		addNewCellTexture(0, (Image) null);
	}

	public Image getGridImage(int iw, int ih){
		
		if(gridImage == null){																	
			gridImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
			reloadFullGridImage();
			
		}
		else{
			if (gridImage.getHeight() != ih || gridImage.getWidth() != iw) {				
				gridImage = (BufferedImage) gridImage.getScaledInstance(iw, ih, BufferedImage.SCALE_SMOOTH);
			}
			
			for (int i = 0; i < gridW; i++) {
				for (int j = 0; j < gridH; j++) {
					if(grid_updateFlag[i][j] == false)			//nur true flags werden gecleart! 
						continue;
					clearCell(i, j, iw/gridH, ih/gridH);											
				}
			}
			
			for (int i = 0; i < gridW; i++) {
				for (int j = 0; j < gridH; j++) {
					if(grid_updateFlag[i][j] == false)			//nur true flags werden repainted! 
						continue;
					repaintCell(i, j, iw/gridW, ih/gridH, false);										
					grid_updateFlag[i][j]=false;
				}
			}
			
		}
		return gridImage; 
	}


	public void reloadFullGridImage() {	
		clearCell(0, 0, gridImage.getWidth(), gridImage.getHeight());  //clear all
		for (int i = 0; i < grid_textureID.length; i++) {
			for (int j = 0; j < grid_textureID[0].length; j++) {
				repaintCell(i, j, gridImage.getWidth()/gridW, gridImage.getHeight()/gridH, false);
			}
		}
		grid_updateFlag = new boolean[gridW][gridH];
	}
	
	private void repaintCell(int x, int y, int cw, int ch, boolean clear) {
		Graphics2D gr = (Graphics2D) gridImage.getGraphics().create();
		Image img = getCellTexture(x, y);
		
		if(clear){
			if(bgColor == null){
				//clearing old texture
			
				gr.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));	
				gr.setColor(new Color(0,0,0,0));
				gr.fillRect(x*cw, y*ch, cw, ch);
				gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				
			} else{
				gr.setColor(bgColor);
				gr.fillRect(x, y, cw, ch);
			}
		}
		
		
		
		if(img  != null){
			gr.rotate(Math.toRadians(getCellRotation(x, y)), x * cw + cw/2, y * ch + ch/2);
			gr.drawImage(img, x * cw, y * ch, cw, ch, null);
		}
		gr.dispose();
	}
	
	public void clearCell(int x, int y, int cw, int ch){
		Graphics2D gr = (Graphics2D) gridImage.getGraphics().create();
		
		if(bgColor == null){
			//clearing old texture
		
			gr.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));	
			gr.setColor(new Color(0,0,0,0));
			gr.fillRect(x*cw, y*ch, cw, ch);
			gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			
		} else{
			gr.setColor(bgColor);
			gr.fillRect(x, y, cw, ch);
		}
	}
	
	
	public void setUpdateFlagForCell(int x, int y){
		
		if(grid_updateFlag[x][y] == false){
			
			grid_updateFlag[x][y] = true;
			
			if(getCellRotation(x, y) != 0){
				if(x > 0)
					setUpdateFlagForCell(x-1, y);
				if(x < gridW-1)
					setUpdateFlagForCell(x+1, y);
				if(y > 0)
					setUpdateFlagForCell(x, y-1);
				if(y < gridH-1)
					setUpdateFlagForCell(x, y+1);
			}
		}
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param id the int representing the new type 
	 * @param absPath the absolute Path of the Image.png
	 */
	public void addNewCellTexture(int id, String absPath) {		
//		File sourceImage = new File("C:\\Users\\Anwender\\Pictures\\elements\\Guy1.png");
		
		if(absPath == null){
			addNewCellTexture(id, (Image) null); 
			return;
		}
		
		
		
		Image image = null;	
		try {
			File sourceImage = new File(absPath);
			image = ImageIO.read(sourceImage);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to load Image! Path: " + absPath);
			return;
		}
		
		addNewCellTexture(id, image);	
	}
	
	public void addNewCellTexture(int id, Image image){
		if (cellTextures.containsKey(id))
			cellTextures.remove(id);
		cellTextures.put(id, image);	
	}
	
	public void addNewCellTextures(int startID, int maxIMGs, int subimg_w, int subimg_h, String absPath){
		
		int imgCounter = 0;
		
		try {
			File sourceImage = new File(absPath);
			BufferedImage image = ImageIO.read(sourceImage);
			for (int i = 0; i < image.getWidth()/subimg_w; i++){
				for (int j = 0; j < image.getHeight()/subimg_h;j++){
					addNewCellTexture(startID + imgCounter++, image.getSubimage(i*subimg_w, j*subimg_h, subimg_w, subimg_h));
					if(imgCounter == maxIMGs)
						return;
				}
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to load Images! Path: " + absPath);
			return;
		}
	}
	
	
	public void setBackgroundColor(Color c){
		bgColor = c;
	}
	
	public Color getBackgroundColor(){
		return bgColor;
	}
	
	/**
	 * 
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param id of the cell
	 */
	public void fillCell(int x, int y, int textureId) {
		grid_textureID[x][y] = textureId;
		rotateCellTexture(x, y, 0); //incl Updateflag
		
	}
	
	/**
	 * 
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param id of the cell
	 * @param rotation of the image in the cell
	 */
	public void fillCell(int x, int y, int textureId, int rotation) {
		grid_textureID[x][y] = textureId;
		rotateCellTexture(x, y, rotation); //incl Updateflag
	}
	
	/**
	 * This method is used to rotate the image in the specified cell.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param rotation of the image in the cell
	 */
	public void rotateCellTexture(int x, int y, int rotation){
		if(getCellTexture(x, y) != null){
			grid_rotation[x][y] = rotation%360;
			setUpdateFlagForCell(x,y);
		}
		
	}

	/**
	 * 
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the type of the cell
	 */
	public int getCellTextureID(int x, int y) {
		return grid_textureID[x][y];
	}

	/**
	 * This method is used to get the image that is associated
	 * with the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the image of the cell
	 */
	public Image getCellTexture(int x, int y) {
		
		return cellTextures.get(getCellTextureID(x, y));	
	}

	/**
	 * @return the width of the grid
	 */
	public int getWidth() {
		return grid_textureID.length;
	}

	/**
	 * @return the height of the grid
	 */
	public int getHeight() {
		return grid_textureID[0].length;
	}

	/**
	 * This method will reset all cells in the grid to the type 0.
	 */
	public void resetGrid() {

		grid_rotation = new int[gridW][gridH];
		grid_textureID= new int[gridW][gridH];
		grid_updateFlag = new boolean[gridW][gridH];
		reloadFullGridImage();

	}


	public double getCellRotation(int x, int y) {		
		return grid_rotation[x][y];
	}

}