package core1_2_3;

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
	
	
	
//	private int[][] grid_celltypeID;
	private String[][] grid_celltypeNAME;
	private int[][] grid_rotation; 
	private transient boolean[][] grid_updateFlag; 
	
	private int gridW;
	private int gridH;
	
	private transient Map<String, CellType> cellTypes = new HashMap<String, CellType>();
	
	private Color bgColor = null;
	
	private transient BufferedImage gridImage;
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ImageGrid(int w, int h) { 
		grid_celltypeNAME = new String[w][h];
		grid_rotation = new int[w][h];
		grid_updateFlag = new boolean[w][h];
		this.gridW = w;
		this.gridH = h;
		
		
		
		addNewTextureCellType(null, (Image) null);
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
		for (int i = 0; i < gridW; i++) {
			for (int j = 0; j < gridH; j++) {
				repaintCell(i, j, gridImage.getWidth()/gridW, gridImage.getHeight()/gridH, false);
			}
		}
		grid_updateFlag = new boolean[gridW][gridH];
	}
	
	private void repaintCell(int x, int y, int cw, int ch, boolean clear) {
		Graphics2D gr = (Graphics2D) gridImage.getGraphics().create();
		Image img = getCellTexture(x, y);
		
		if(clear && hasCellTrasparency(x, y)){	//clearing old texture
			if(bgColor == null){
				
			
				gr.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));	
				gr.setColor(new Color(0,0,0,0));
				gr.fillRect(x*cw, y*ch, cw, ch);
				gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				
			} else{
				
				gr.setColor(bgColor);
				gr.fillRect(x*cw, y*ch, cw, ch);
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
			gr.fillRect(x*cw, y*ch, cw, ch);
		}
		gr.dispose();
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
	public CellType addNewTextureCellType(String name, String absImgPath) {
		
		if(absImgPath == null){
			return addNewTextureCellType(name, (Image) null); 
		}
		
		Image image = null;	
		try {
			File sourceImage = new File(absImgPath);
			image = ImageIO.read(sourceImage);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to load Image! Path: " + absImgPath);
			return null;
		}
		
		return addNewTextureCellType(name, image);	
	}
	
	public CellType addNewTextureCellType(String name, Image image){
		CellType ct = new TextureCell(image, true, name);		// TODO TRANSPARENCY
		addNewCellType(ct);			
		return ct;
	}
	
	public CellType[] addNewTextureCellTypes(String name, int maxIMGs, int subimg_w, int subimg_h, String absPath){
		
		CellType[] celltypes = new TextureCell[maxIMGs];
		int counter = 0;
		try {
			File sourceImage = new File(absPath);
			BufferedImage image = ImageIO.read(sourceImage);
			for (int i = 0; i < image.getWidth()/subimg_w; i++){
				for (int j = 0; j < image.getHeight()/subimg_h;j++){
					
					celltypes[counter] = addNewTextureCellType(name+"_" + counter++, image.getSubimage(i*subimg_w, j*subimg_h, subimg_w, subimg_h));

					if(counter == maxIMGs)
						return celltypes;
				}
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to load Images! Path: " + absPath);
			
		}
		return celltypes;
	}
	
	public CellType addNewColorCellType(String name, Color c){
		CellType ct = new ColorCell(c, name);
		addNewCellType(ct);
		return ct;
	}
	
	public void addNewCellType(CellType ct){
		cellTypes.put(ct.getNAME(), ct);
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
	public void fillCell(String ctName, int x, int y) {
		fillCell(ctName, x, y, 0);		//incl Updateflag
	}
	//TODO
	/**
	 * 
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param id of the cell
	 * @param rotation of the image in the cell
	 */
	public void fillCell(String ctName, int x, int y, int rotation) {
		if(!cellTypes.containsKey(ctName))
			throw new IllegalArgumentException("no "+ctName+" fitting CellType registered!");
		
		grid_celltypeNAME[x][y] = ctName;
		rotateCellTexture(x, y, rotation); //incl Updateflag
	}
	
	
	public void fillCell(CellType ct, int x, int y) {
		fillCell(ct, x, y, 0);	//incl Updateflag
	}
	
	public void fillCell(CellType ct, int x, int y, int rotation) {
		if(!cellTypes.containsValue(ct)){
			addNewCellType(ct);
		}
			
		grid_celltypeNAME[x][y] = ct.getNAME();
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
	public String getCellTypeNAME(int x, int y) {
		return grid_celltypeNAME[x][y];
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
		
		return cellTypes.get(getCellTypeNAME(x, y)).getTexture();	
	}
	
	public boolean hasCellTexture(int x, int y, int w, int h) {
		
		return cellTypes.get(getCellTypeNAME(x, y)).getTexture() != null;	
	}
	
	public boolean hasCellTrasparency(int x, int y) {
		
		return cellTypes.get(getCellTypeNAME(x, y)).hasTrasparency();	
	}
	

	/**
	 * @return the width of the grid
	 */
	public int getWidth() {
		return gridW;
	}

	/**
	 * @return the height of the grid
	 */
	public int getHeight() {
		return gridH;
	}

	/**
	 * This method will reset all cells in the grid to the type 0.
	 */
	public void resetGrid() {

		grid_rotation = new int[gridW][gridH];
		grid_celltypeNAME= new String[gridW][gridH];
		grid_updateFlag = new boolean[gridW][gridH];
		reloadFullGridImage();

	}


	public double getCellRotation(int x, int y) {		
		return grid_rotation[x][y];
	}

	
}

