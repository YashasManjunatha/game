import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Block {
	private Rectangle myBlock;
	private Line[] mySides;
	private double[] myNormals;
	private char myType;
	
	public Block (double x, double y, double width, double height) {
		this(x, y, width, height, 'N');
	}
	
	public Block (double x, double y, double width, double height, char type) {
		myBlock = new Rectangle(x, y, width, height);
		
		mySides = new Line[4];
		double gap = 3;
		mySides[0] = new Line(x + gap, y, x + width - gap, y);
		mySides[1] = new Line(mySides[0].getEndX(), mySides[0].getEndY() + gap, x + width, y + height - gap);
		mySides[2] = new Line(mySides[1].getEndX() - gap, mySides[1].getEndY(), x + gap, y + height);
		mySides[3] = new Line(mySides[2].getEndX(), mySides[2].getEndY() - gap, mySides[0].getStartX(), mySides[0].getStartY() + gap);
		
		myNormals = new double[4];
		for (int i = 0; i < mySides.length; i++) {
			myNormals[i] = -90 + i * (360 / mySides.length);
		}
		
		myType = type;
	}
	
	public Rectangle getBlock() {
		return myBlock;
	}
	
	public Line[] getSides() {
		return mySides;
	}
	
	public double[] getNormals() {
		return myNormals;
	}
	
	public char getType() {
		return myType;
	}
}
