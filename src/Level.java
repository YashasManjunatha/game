import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Level {
	private Group myRoot;
	private Scene myScene;
	private int numSides;
	private Line[] mySides;
	private double[] myNormals;
	private int numBlocks;
	private HashSet<Block> myBlocks;
	private int myLevel;
	
	public Scene getScene() {
		return myScene;
	}
	
	public Level (int level, int stage_width, int stage_height, Paint background) {
		myRoot = new Group();
		myScene = new Scene(myRoot, stage_width, stage_height, background);
		
		myLevel = level;
		myBlocks = new HashSet<Block>();
		readLevelFile(level);
		
		mySides = new Line[numSides];
		myNormals = new double[numSides];
		generateSides(numSides, stage_width, stage_height);
		generateNormals();
	}
	
	public int getLevel() {
		return myLevel;
	}
	
	public void addRoot (Shape shape) {
		myRoot.getChildren().add(shape);
	}
	
	public void removeFromRoot (Shape shape) {
		myRoot.getChildren().remove(shape);
	}
	
	public void removeBlock (Block block) {
		myBlocks.remove(block);
	}
	
	public void drawLevel() {
		for (int i = 0; i < numSides; i++) {
			mySides[i].setFill(Color.BLUE);
    			myRoot.getChildren().add(mySides[i]);
		}
    	
		for (Block block: myBlocks) {
    			if(block.getType() == 'N')
    				block.getBlock().setFill(Color.CHOCOLATE);
    			if(block.getType() == '+')
    				block.getBlock().setFill(Color.GREENYELLOW);
    			if(block.getType() == 'P')
    				block.getBlock().setFill(Color.PLUM);
    			myRoot.getChildren().add(block.getBlock());
		}
	}
	
	private void readLevelFile(int level) {
		File file = new File("src/Level " + level + ".txt");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    sc.useDelimiter("\\s");
	 
	    numSides = Integer.valueOf(sc.next());
	    numBlocks = Integer.valueOf(sc.next());
	    
	    readBlocks(numBlocks, sc);
	}
	
	private void readBlocks (int numBlocks, Scanner sc) {
	    for (int i = 0; i < numBlocks; i++)
	    		myBlocks.add(new Block (Integer.valueOf(sc.next()), Integer.valueOf(sc.next()), Integer.valueOf(sc.next()), Integer.valueOf(sc.next()), sc.next().charAt(0)));
	}
	
	public HashSet<Block> getBlocks() {
		return myBlocks;
	}
	
	private void generateSides (int sides, int stage_width, int stage_height) {
		switch (sides) {
			case 3: mySides[0] = new Line (stage_width, stage_height, 0, stage_height);
					mySides[1] = new Line (0, stage_height, stage_width/2, 0);
					mySides[2] = new Line (stage_width/2, 0, stage_width, stage_height);
					break;
			case 4: mySides[0] = new Line (stage_width, stage_height, 0, stage_height);
					mySides[1] = new Line (0, stage_height, 0, 0);
					mySides[2] = new Line (0, 0, stage_width, 0);
					mySides[3] = new Line (stage_width, 0, stage_width, stage_height);
					break;
			case 5: double side_length = Math.sin(Math.toRadians(36)) * stage_width / Math.sin(Math.toRadians(108));
					mySides[0] = new Line ((stage_width + side_length)/2, stage_height, (stage_width - side_length)/2, stage_height);
					mySides[1] = new Line ((stage_width - side_length)/2, stage_height, 0, stage_height - (Math.sin(Math.toRadians(72)) * side_length));
					mySides[2] = new Line (mySides[1].getEndX(), mySides[1].getEndY(), stage_width/2, stage_height - ((Math.sin(Math.toRadians(72)) * side_length) + (Math.tan(Math.toRadians(36)) * stage_width/2)));
					mySides[3] = new Line (mySides[2].getEndX(), mySides[2].getEndY(), stage_width, stage_height - (Math.sin(Math.toRadians(72)) * side_length));
					mySides[4] = new Line (mySides[3].getEndX(), mySides[3].getEndY(), mySides[0].getStartX(), mySides[0].getStartY());
			default: break;
		}
	}
	
	private void generateNormals () {
		if(mySides.length == 3) {
			myNormals[0] = -90;
			myNormals[1] = 90 - Math.toDegrees(Math.atan(2));
			myNormals[2] = 90 + Math.toDegrees(Math.atan(2));
		} else {
			for (int i = 0; i < mySides.length; i++) {
				myNormals[i] = -90 + i * (360 / mySides.length);
			}
		}
	}
	
	public Line[] getSides () {
		return mySides;
	}
	
	public double[] getNormals () {
		return myNormals;
	}
}
