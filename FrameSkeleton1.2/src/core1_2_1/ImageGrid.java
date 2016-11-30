package core1_2_1;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
public class ImageGrid {
	
	
	
	private int[][] grid;
	private int[][] rotations; 
	
	private Map<Integer, Image> celltypeIMGs = new HashMap<Integer, Image>();
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ImageGrid(int w, int h) {
		grid = new int[w][h];
		rotations = new int[w][h];
		addNewCellTypeImage(0, (Image) null);
	}

	/**
	 * This method can be used to add a new type of cell.
	 * If the chosen int to represent the new type is 
	 * already in use this method will overwrite the older type.  
	 * 
	 * @param type the int representing the new type 
	 * @param absPath the absolute Path of the Image.png
	 */
	public void addNewCellTypeImage(int type, String absPath) {		
//		File sourceImage = new File("C:\\Users\\Anwender\\Pictures\\elements\\Guy1.png");
		
		if(absPath == null){
			addNewCellTypeImage(type, (Image) null); 
			return;
		}
		
		
		
		Image image = null;	
		try {
			File sourceImage = new File(absPath);
			image = ImageIO.read(sourceImage);
			System.out.println("DONE!");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Failed to load Image! Path: " + absPath);
			return;
		}
		
		addNewCellTypeImage(type, image);	
	}
	
	public void addNewCellTypeImage(int type, Image image){
		if (celltypeIMGs.containsKey(type))
			celltypeIMGs.remove(type);
		celltypeIMGs.put(type, image);	
	}
	
	public void addNewCellTypeImages(int startType, int maxIMGs, int subimg_w, int subimg_h, String absPath){
		
		int imgCounter = 0;
		
		try {
			File sourceImage = new File(absPath);
			BufferedImage image = ImageIO.read(sourceImage);
			for (int i = 0; i < image.getWidth()/subimg_w; i++){
				for (int j = 0; j < image.getHeight()/subimg_h;j++){
					addNewCellTypeImage(startType + imgCounter++, image.getSubimage(i*subimg_w, j*subimg_h, subimg_w, subimg_h));
					if(imgCounter == maxIMGs)
						return;
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Failed to load Image! Path: " + absPath);
			return;
		}
	}
	
	

	/**
	 * This method is used to fill a Cell on the grid with a specific celltype.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param type of the cell
	 */
	public void fillCell(int x, int y, int type) {
		grid[x][y] = type;
	}
	
	/**
	 * This method is used to fill a Cell on the grid with a specific celltype and rotate it.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param type of the cell
	 * @param rotation of the image in the cell
	 */
	public void fillCell(int x, int y, int type, int rotation) {
		grid[x][y] = type;
		rotations[x][y] = rotation;
	}
	
	/**
	 * This method is used to rotate the image in the specified cell.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param rotation of the image in the cell
	 */
	public void rotateCellImage(int x, int y, int rotation){
		rotations[x][y] = rotation;
	}

	/**
	 * This method is used to get the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the type of the cell
	 */
	public int getCellType(int x, int y) {
		return grid[x][y];
	}

	/**
	 * This method is used to get the image that is associated
	 * with the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the image of the cell
	 */
	public Image getCellImage(int x, int y) {
		
		return celltypeIMGs.get(getCellType(x, y));	
	}

	/**
	 * @return the width of the grid
	 */
	public int getWidth() {
		return grid.length;
	}

	/**
	 * @return the height of the grid
	 */
	public int getHeight() {
		return grid[0].length;
	}

	/**
	 * This method will reset all cells in the grid to the type 0.
	 */
	public void resetGrid() {

		for (int i = 0; i < grid.length; i++) {
			Arrays.fill(grid[i], 0);
		}

	}


	public double getCellImgRotation(int x, int y) {		
		return rotations[x][y];
	}

}