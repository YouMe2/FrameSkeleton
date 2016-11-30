package core1_2_4;

import java.awt.Image;
import java.util.Observable;

public class Texture extends Observable{
	
	private TextureType tt;
	
	private double x;
	private double y;
	
	private double w;
	private double h;
	
	
	private int rotation;
	
	public Texture(double x, double y, double w, double h, TextureType ct){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tt = ct;
	}
	
	public Texture(double x, double y, double w, double h, int rotation, TextureType ct){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
		this.tt = ct;
	}
	
	public Image getImage(){
		return tt.getImage();
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
		return tt.hasTrasparency();
	}
	
}
