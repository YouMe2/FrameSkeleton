package core1_2_4;

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
	private String[][] grid_texturetypeNAME;
	private int[][] grid_rotation; 
	private transient boolean[][] grid_updateFlag; 
	
	private int gridW;
	private int gridH;
	
	private transient Map<String, TextureType> textureTypes = new HashMap<String, TextureType>();
	
	private Color bgColor = null;
	
	private transient BufferedImage gridImage;
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ImageGrid(int w, int h) { 
		grid_texturetypeNAME = new String[w][h];
		grid_rotation = new int[w][h];
		grid_updateFlag = new boolean[w][h];
		this.gridW = w;
		this.gridH = h;
		
		addNewImageTextureType(null, (Image) null);
	}

	public Image getGridImage(int iw, int ih){
		
		if(gridImage == null){																	
			gridImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
			renderFullGridImage();
			
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


	public void renderFullGridImage() {	
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
		Image img = getCellImage(x, y);
		
		if(clear && hasCellImageTrasparency(x, y)){	//clearing old texture
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
			gr.rotate(Math.toRadians(getCellImageRotation(x, y)), x * cw + cw/2, y * ch + ch/2);
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
			
			if(getCellImageRotation(x, y) != 0){
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
	
	
	public TextureType addNewImageTextureType(String name, String absImgPath) {
		
		if(absImgPath == null){
			return addNewImageTextureType(name, (Image) null); 
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
		
		return addNewImageTextureType(name, image);	
	}
	
	public TextureType addNewImageTextureType(String name, Image image){
		TextureType ct = new ImageTexture(image, true, name);		// TODO TRANSPARENCY
		addNewTextureType(ct);			
		return ct;
	}
	
	public TextureType[] addNewImageTextureTypes(String name, int maxIMGs, int subimg_w, int subimg_h, String absPath){
		
		TextureType[] celltypes = new ImageTexture[maxIMGs];
		int counter = 0;
		try {
			File sourceImage = new File(absPath);
			BufferedImage image = ImageIO.read(sourceImage);
			for (int i = 0; i < image.getWidth()/subimg_w; i++){
				for (int j = 0; j < image.getHeight()/subimg_h;j++){
					
					celltypes[counter] = addNewImageTextureType(name+"_" + counter++, image.getSubimage(i*subimg_w, j*subimg_h, subimg_w, subimg_h));

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
	
	public TextureType addNewColorTextureType(String name, Color c){
		TextureType ct = new ColorTexture(c, name);
		addNewTextureType(ct);
		return ct;
	}
	
	public void addNewTextureType(TextureType ct){
		textureTypes.put(ct.getNAME(), ct);
	}
	
	public void setBackgroundColor(Color c){
		bgColor = c;
	}
	
	public Color getBackgroundColor(){
		return bgColor;
	}
	
	public void fillCell(String ttName, int x, int y) {
		fillCell(ttName, x, y, 0);		//incl Updateflag
	}
	
	public void fillCell(String ttName, int x, int y, int rotation) {
		if(!textureTypes.containsKey(ttName))
			throw new IllegalArgumentException("no "+ttName+" fitting TextureType registered!");
		
		grid_texturetypeNAME[x][y] = ttName;
		rotateCellImage(x, y, rotation); //incl Updateflag
	}
	
	
	public void fillCell(TextureType tt, int x, int y) {
		fillCell(tt, x, y, 0);	//incl Updateflag
	}
	
	public void fillCell(TextureType tt, int x, int y, int rotation) {
		if(!textureTypes.containsValue(tt)){
			addNewTextureType(tt);
		}
			
		grid_texturetypeNAME[x][y] = tt.getNAME();
		rotateCellImage(x, y, rotation); //incl Updateflag
	}
	
	public void rotateCellImage(int x, int y, int rotation){
		if(getCellImage(x, y) != null){
			grid_rotation[x][y] = rotation%360;
			setUpdateFlagForCell(x,y);
		}
		
	}

	public String getTextureTypeNAME(int x, int y) {
		return grid_texturetypeNAME[x][y];
	}

	public Image getCellImage(int x, int y) {		
		return textureTypes.get(getTextureTypeNAME(x, y)).getImage();	
	}

	public boolean hasCellImageTrasparency(int x, int y) {		
		return textureTypes.get(getTextureTypeNAME(x, y)).hasTrasparency();	
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
		grid_texturetypeNAME= new String[gridW][gridH];
		grid_updateFlag = new boolean[gridW][gridH];
		renderFullGridImage();

	}


	public double getCellImageRotation(int x, int y) {		
		return grid_rotation[x][y];
	}

	
}

