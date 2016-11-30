package core1_2_4;

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
	private Map<String, TextureType> textureTypes = new HashMap<String, TextureType>();
	
	private int spaceW;	//w in grid rows
	private int spaceH; //h in grid lines
	
	private int gridLineH; // in pixels
	private int gridColumnW; // in pixels
	
	private Color bgColor = null;
	
	private transient BufferedImage spaceImage;
	
	private transient boolean updateFlag;
	
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
			
			Image img = texture.getImage();
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
		TextureType tt = new ImageTexture(image, true, name);		// TODO TRANSPARENCY
		addNewTextureType(tt);			
		return tt;
	}
	
	public TextureType[] addNewImageTextureTypes(String name, int maxIMGs, int subimg_w, int subimg_h, String absPath){
		
		TextureType[] texturetypes = new ImageTexture[maxIMGs];
		int counter = 0;
		try {
			File sourceImage = new File(absPath);
			BufferedImage image = ImageIO.read(sourceImage);
			for (int i = 0; i < image.getWidth()/subimg_w; i++){
				for (int j = 0; j < image.getHeight()/subimg_h;j++){
					
					texturetypes[counter] = addNewImageTextureType(name+"_" + counter++, image.getSubimage(i*subimg_w, j*subimg_h, subimg_w, subimg_h));

					if(counter == maxIMGs)
						return texturetypes;
				}
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Failed to load Images! Path: " + absPath);
			
		}
		return texturetypes;
	}
	
	public TextureType addNewColorTextureType(String name, Color c){
		TextureType ct = new ColorTexture(c, name);
		addNewTextureType(ct);
		return ct;
	}
	
	public void addNewTextureType(TextureType tt){

		textureTypes.put(tt.getNAME(), tt);
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
	
	public Texture putTexture(TextureType tt, double x, double y, double w, double h, int rotation) {
		Texture t = new Texture(x, y, w, h, rotation, tt);
		putTexture(t);
		return t;
	}
	
	public Texture putTexture(TextureType tt, double x, double y, double w, double h) {
		return putTexture(tt, x, y, w, h, 0);	
	}
	
	public Texture putTexture(String ttName, double x, double y, double w, double h, int rotation) {
		if(!textureTypes.containsKey(ttName))
			throw new IllegalArgumentException("no '"+ttName+"' fitting TextureType registered!");
		return putTexture(textureTypes.get(ttName), x, y, w, h, rotation);	
	}
	
	public Texture putTexture(String ttName, double x, double y, double w, double h) {
		return putTexture(ttName, x, y, w, h, 0);	
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

	public void resetSpace() {
		cellspace.clear();
		updateFlag = true;
	}

	@Override
	public void update(Observable o, Object texture) {
		
		updateFlag = true;
		updateTexturePosition((Texture) texture);
		setChanged();
		notifyObservers(this);
	}		
	
	
	


}