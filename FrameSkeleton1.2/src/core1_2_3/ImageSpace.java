package core1_2_3;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

/**
* The FrameGrid class provides basic functionalities of a Grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.2
* @since   2016-
*/
//TODO
public class ImageSpace extends Observable implements Observer{
	
	
	private ArrayList<Texture> cellspace = new ArrayList<Texture>(); 	
	private Map<String, CellType> cellTypes = new HashMap<String, CellType>();
	
	private int spaceW;	//w in grid rows
	private int spaceH; //h in grid lines
	
	private int gridLineH; // in pixels
	private int gridColumnW; // in pixels
	
	private Color bgColor = null;
	
	private transient BufferedImage spaceImage;
	
	private transient boolean updateFlag;
	
	/**
	 * @param w the width of the grid
	 * @param h the height of the grid
	 */
	public ImageSpace(int w, int h) {
		spaceH = h;
		spaceW = w;
	}

	public Image getSpaceImage(int iw, int ih){
		
		if(spaceImage == null || spaceImage.getHeight() != ih || spaceImage.getWidth() != iw){
			spaceImage = new BufferedImage(iw, ih, BufferedImage.TYPE_4BYTE_ABGR);
			gridLineH = ih/spaceH;
			gridColumnW = iw/spaceW;
			
//			System.out.println("GRID W: "+gridColumnW);
//			System.out.println("GRID H: "+gridLineH);
			
			updateFlag = true;
		}
		
		if(updateFlag){			
			reloadFullSpaceImage();
			updateFlag = false;
		}
		
		return spaceImage;
	}
	


	private void reloadFullSpaceImage() {
		clearArea(0, 0, spaceImage.getWidth(), spaceImage.getHeight());
		
		Graphics2D gr = (Graphics2D) spaceImage.getGraphics().create();
		
		for (Iterator iterator = cellspace.iterator(); iterator.hasNext();) {
			Texture texture = (Texture) iterator.next();
			
			Image img = texture.getTexture();
			double x = texture.getX();
			double y = texture.getY();
			double tw = texture.getW();
			double th = texture.getH();
			int rot = texture.getRotation();
			
//			System.out.println("X: "+x);
			
			gr.rotate(Math.toRadians(rot), x*gridColumnW + (tw*gridColumnW)/2, y*gridLineH + (th*gridLineH)/2);
			
			gr.drawImage(img, (int) (x*gridColumnW) , (int) (y*gridLineH) , (int) (tw*gridColumnW), (int) (th*gridLineH), null);
			
			gr.rotate(-Math.toRadians(rot), x*gridColumnW + (tw*gridColumnW)/2, y*gridLineH + (th*gridLineH)/2);
			
		}
		gr.dispose();
	}

	private void clearArea(int ix, int iy, int width, int height) {
		Graphics2D gr = (Graphics2D) spaceImage.getGraphics().create();
		
		if(bgColor == null){
			//clearing old texture
		
			gr.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));	
			gr.setColor(new Color(0,0,0,0));
			gr.fillRect(ix, iy, width, height);
			gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			
		} else{
			gr.setColor(bgColor);
			gr.fillRect(ix, iy, width, height);
		}
		gr.dispose();
	}

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
	

	public void putTexture(Texture t){
		t.addObserver(this);
		
		
		int i = 0;
		for(; i < cellspace.size(); i++){
			if(cellspace.get(i).getY()+cellspace.get(i).getH() <= t.getY() + t.getH())
				continue;
			else
				break;
		}
		cellspace.add(i, t);
		
		
//		cellspace.add(t);
		
		
		
		
		updateFlag = true;
	}
	
	public void updateTexturePosition(Texture t){
		
		cellspace.remove(t);
		putTexture(t);
		
		
	}
	
	public Texture putTexture(CellType ct, double x, double y, double w, double h, int rotation) {
		Texture t = new Texture(x, y, w, h, rotation, ct);
		putTexture(t);
		return t;
	}
	
	public Texture putTexture(CellType ct, double x, double y, double w, double h) {
		return putTexture(ct, x, y, w, h, 0);	
	}
	
	public Texture putTexture(String ctName, double x, double y, double w, double h, int rotation) {
		if(!cellTypes.containsKey(ctName))
			throw new IllegalArgumentException("no '"+ctName+"' fitting CellType registered!");
		return putTexture(cellTypes.get(ctName), x, y, w, h, rotation);	
	}
	
	public Texture putTexture(String ctName, double x, double y, double w, double h) {
		return putTexture(ctName, x, y, w, h, 0);	
	}
	
	public void removeTexture(Texture t){
		if(!cellspace.contains(t))
			throw new IllegalArgumentException("no '"+t+"' fitting Texture!");
		cellspace.remove(t);	
	}

	/**
	 * @return the width of the grid
	 */
	public int getWidth() {
		return spaceW;
	}

	/**
	 * @return the height of the grid
	 */
	public int getHeight() {
		return spaceH;
	}

	/**
	 * This method will reset all cells in the grid to the type 0.
	 */
	public void resetSpace() {
		cellspace.clear();
		updateFlag = true;
	}

	@Override
	public void update(Observable o, Object arg) {
		
		updateFlag = true;
		updateTexturePosition((Texture) arg);
		setChanged();
		notifyObservers(this);
	}		
	
	
	public class Texture extends Observable{
		
		private CellType ct;
		
		private double x;
		private double y;
		
		private double w;
		private double h;
		
		
		private int rotation;
		
		public Texture(double x, double y, double w, double h, CellType ct){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.ct = ct;
		}
		
		public Texture(double x, double y, double w, double h, int rotation, CellType ct){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.rotation = rotation;
			this.ct = ct;
		}
		
		public Image getTexture(){
			return ct.getTexture();
		}
		
		public void setSize(int w, int h){
			setChanged();
			this.w = w;
			this.h = h;
			notifyObservers(this);
		}
		
		public void movePosition(double nx, double ny){
			setChanged();
			x = nx;
			y = ny;
			notifyObservers(this);
		}
		
		public double getH() {
			return h;
		}
		
		public double getW() {
			return w;
		}
		
		public double getY() {
			return y;
		}
		
		public double getX() {
			return x;
		}
		
		public int getRotation() {
			return rotation;
		}
		
		public void setRotation(int rotation) {
			setChanged();
			this.rotation = rotation;
			notifyObservers(this);
		}
		
		public boolean hasTrasparency(){
			return ct.hasTrasparency();
		}
		
	}


}