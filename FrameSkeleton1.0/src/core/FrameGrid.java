package core;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
* The FrameGrid class provides basic functionalities of a Grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.0
* @since   2016-06-22 
*/
public class FrameGrid {
	
	public static final boolean SHAPE_RECT = false;
	public static final boolean SHAPE_CIRCLE = true;
	
	private int[][] grid;
	private Color grid_bg;
	private Map<Integer, Color> celltypeColors = new HashMap<Integer, Color>();
	private Map<Integer, Boolean> celltypeShapes = new HashMap<Integer, Boolean>();
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public FrameGrid(int w, int h) {
		grid = new int[w][h];
		setGridBackground(Color.WHITE);
		newCellType(0, Color.WHITE, SHAPE_RECT); 	//AIR
		newCellType(1, Color.BLACK, SHAPE_RECT); 	//WALL
		newCellType(2, Color.RED, SHAPE_RECT);   	//TRAP
		newCellType(3, Color.GREEN, SHAPE_RECT); 	//TARGET
		newCellType(4, Color.BLUE,SHAPE_CIRCLE);	//PLAYER
		newCellType(5, Color.MAGENTA, SHAPE_CIRCLE);//PLAYER2
		newCellType(6, Color.YELLOW, SHAPE_CIRCLE); //PLAYER3 //COIN
		newCellType(7, Color.CYAN, SHAPE_CIRCLE);   //PLAYER4
		newCellType(8, Color.LIGHT_GRAY, SHAPE_RECT);//BLOCK
		newCellType(9, Color.DARK_GRAY, SHAPE_RECT); //BLOCK
	}

	/**
	 * @param color the background color for the Grid
	 */
	public void setGridBackground(Color color) {
		grid_bg = color;			
	}
	
	/**
	 * @return the background color for the Grid
	 */
	public Color getGridBackgroundColor(){
		return grid_bg;
	}

	/**
	 * This method can be used to add a new type of cell.
	 * If the chosen int to represent the new type is 
	 * already in use this method will overwrite the older type.  
	 * 
	 * @param type the int representing the new type 
	 * @param color the color associated with the new type
	 * @param shape the shape associated with the new type
	 */
	public void newCellType(int type, Color color, boolean shape) {
		if (celltypeColors.containsKey(type))
			celltypeColors.remove(type);
		celltypeColors.put(type, color);
		if (celltypeShapes.containsKey(type))
			celltypeShapes.remove(type);
		celltypeShapes.put(type, shape);		
	}

	/**
	 * This method is used to fill a Cell on the grid with a specific celltype.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @param type type of the cell
	 */
	public void setCellType(int x, int y, int type) {
		grid[x][y] = type;
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
	 * This method is used to get the color that is associated
	 * with the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the color of the cell
	 */
	public Color getCellColor(int x, int y) {
//		Color c = celltypeColors.get(getCellType(x, y));
//		if(c == null){
//			c = Color.PINK;
//		}
//		return c;
		return celltypeColors.get(getCellType(x, y));
		
	}
	
	/**
	 * This method is used to get the shape that is associated
	 * with the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the shape of the cell
	 */
	public boolean getCellShape(int x, int y) {
		return celltypeShapes.get(getCellType(x, y));
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

}