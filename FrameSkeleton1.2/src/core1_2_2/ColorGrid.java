package core1_2_2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
* The FrameGrid class provides basic functionalities of a Grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.2
* @since   2016-
*/
//TODO moded
public class ColorGrid implements Serializable{
	
	public static final boolean SHAPE_RECT = false;
	public static final boolean SHAPE_CIRCLE = true;
	
	private int[][] colorGrid;
	
	private int gridW;
	private int gridH;
	
	private Color grid_bg;
	private Map<Integer, Color> celltypeColors = new HashMap<Integer, Color>();
	private Map<Integer, Boolean> celltypeShapes = new HashMap<Integer, Boolean>();
	
	private transient BufferedImage gridImage;
	private transient ArrayList<Point> changedCells;
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ColorGrid(int gw, int gh) {
		colorGrid = new int[gw][gh];
		changedCells = new ArrayList<Point>();
		
		gridW = gw;
		gridH = gh;
		
		setGridBackground(Color.WHITE);
		addNewCellType(0, grid_bg, SHAPE_RECT); 	//BG
		addNewCellType(1, Color.BLACK, SHAPE_RECT); 	//WALL
		addNewCellType(2, Color.RED, SHAPE_RECT);   	//TRAP
		addNewCellType(3, Color.GREEN, SHAPE_RECT); 	//TARGET
		addNewCellType(4, Color.BLUE,SHAPE_CIRCLE);	//PLAYER
		addNewCellType(5, Color.MAGENTA, SHAPE_CIRCLE);//PLAYER2
		addNewCellType(6, Color.YELLOW, SHAPE_CIRCLE); //PLAYER3 //COIN
		addNewCellType(7, Color.CYAN, SHAPE_CIRCLE);   //PLAYER4
		addNewCellType(8, Color.LIGHT_GRAY, SHAPE_RECT);//BLOCK
		addNewCellType(9, Color.DARK_GRAY, SHAPE_RECT); //BLOCK
	}

	/**
	 * @param color the background color for the Grid
	 */
	public void setGridBackground(Color color) {
		grid_bg = color;
		addNewCellType(0, grid_bg, SHAPE_RECT);
		
	}
	
	/**
	 * @return the background color for the Grid
	 */
	public Color getBackgroundColor(){
		return grid_bg;
	}
	
	
	
	public void reloadFullGridImage(){
		if(changedCells == null)
			changedCells = new ArrayList<Point>();
		synchronized(changedCells){
			for (int i = 0; i < colorGrid.length; i++) {
				for (int j = 0; j < colorGrid[0].length; j++) {
					repaintCell(i, j, gridImage.getWidth()/gridW, gridImage.getHeight()/gridH);
				}
			}
		}
		changedCells.clear();
		
	}
	
	private void repaintCell(int x, int y, int cw, int ch){
		Graphics gr = gridImage.getGraphics();
		
		if(getCellColor(x, y).getAlpha() != 255 || getCellShape(x, y) == SHAPE_CIRCLE ){
			gr.setColor(getBackgroundColor());
			gr.fillRect(x * cw, y * ch, cw, ch);
		}

		gr.setColor(getCellColor(x, y));
		
		if(getCellShape(x, y) == ColorGrid.SHAPE_RECT)
			gr.fillRect(x * cw, y * ch, cw, ch);
		else
			gr.fillOval(x * cw, y * ch, cw, ch);	
		gr.dispose();
	}
	
	public Image getGridImage(int w, int h){
		
		if(gridImage == null){													System.out.println("FULL NEW COLORIMG");
			gridImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			reloadFullGridImage();
			
		}
		else{
			if (gridImage.getHeight() != h || gridImage.getWidth() != w) {
				gridImage = (BufferedImage) gridImage.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
			}
			
			Point changedCellPos;		
			while((changedCellPos = getNextChangedCell()) != null){				
				repaintCell(changedCellPos.x, changedCellPos.y, w/gridW, h/gridH);		System.out.println("UPDATED COLOR CELL");
			}
		}
		return gridImage;
		
	}

	private Point getNextChangedCell() {
		synchronized (changedCells) {
			if(changedCells.isEmpty())
				return null;
			
			Point p = changedCells.get(changedCells.size()-1);
			changedCells.remove(changedCells.size()-1);
			return p;
		}
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
	public void addNewCellType(int type, Color color, boolean shape) {
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
	public void fillCell(int x, int y, int type) {
		if(changedCells == null)
			changedCells = new ArrayList<Point>();
		colorGrid[x][y] = type;
		changedCells.add(new Point(x,y));
	}	
	
	/**
	 * This method is used to get the type of a specific cell in the grid.
	 * 
	 * @param x cord of the cell
	 * @param y cord of the cell
	 * @return the type of the cell
	 */
	public int getCellType(int x, int y) {
		return colorGrid[x][y];
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

		if(changedCells == null)
			changedCells = new ArrayList<Point>();
		for (int i = 0; i < colorGrid.length; i++) {
			Arrays.fill(colorGrid[i], 0);
		}
		gridImage = null;
		changedCells.clear();
	}
	
	
	
	//TODO
	@Override
	public String toString(){
		String str = "";
		for (int i = 0; i < colorGrid.length; i++) {
			for (int j = 0; j < colorGrid[0].length; j++) {
				str += colorGrid[i][j] + " ";
			}
			str += "::";
		}
		return str;
		
	}
	
	public void readFromString(String str){
		System.out.println(str);
		String[] col = str.split("::");
		String[][] g = new String[col.length][];
		
		for (int i = 0; i < col.length; i++) {
			
			System.out.println(i + " " + col[i]);
			g[i] = col[i].split(" ");
			
			for (int j = 0; j < g[0].length; j++) {
				colorGrid[i][j] = Integer.valueOf(g[i][j]);
			}
		}
		
		
	}

}