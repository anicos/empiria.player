package eu.ydp.empiria.player.client.util.geom;

public class Size {

	private int width;
	private int height;

	public Size(){
		width = 0;
		height = 0;
	}

	public Size(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}