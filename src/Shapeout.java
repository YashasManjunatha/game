import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Shapeout extends Application {
	public static final String TITLE = "Shapeout";
    public static final int SIZE = 500;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    
    private static final int PADDLE_WIDTH = 50;
    private static final int PADDLE_HEIGHT = 10;
    private double paddle_speed = 10;
    private static final double BALL_SIZE = 7.5;
    private int ball_speed = 200;
    private double ball_angle = Math.random() * 120 + 210;
    private boolean ball_launched = false;
    private boolean ball_destroyed = false;
    private boolean game_begun = false;
    private int myLives = 3;
    private int myScore = 0;
    
    private Stage myStage;
	private Rectangle myPaddle;
	private Circle myBall;
	private Level myLevel;
	private Text level_text;
	private Text lives_text;
	private Text score_text;

	@Override
	public void start(Stage stage) throws Exception {
		myStage = stage;
		
		// attach scene to the stage and display it
		Scene scene = setupSplash(SIZE, SIZE, BACKGROUND);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
        
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}
	
	private Scene setupSplash (int width, int height, Paint background) {
		Group root = new Group();
		Scene scene = new Scene(root, width, height, Color.WHITE);
		
		Text[] instructions = new Text[9];
		
		instructions[0] = new Text(180, 80, "Welcome to Shapeout");
		instructions[1] = new Text(50, 150, "Use the Up Arrow to launch the ball.");
		instructions[2] = new Text(50, 180, "Control the paddle with the Left and Right Arrows.");
		instructions[3] = new Text(50, 210, "All blocks are destroyed with one hit.");
		instructions[4] = new Text(50, 240, "Purple blocks drop power ups and green blocks give you an extra life.");
		instructions[5] = new Text(50, 270, "Power Ups (last for 20 seconds): 'F' flips paddle control;");
		instructions[6] = new Text(80, 300, "'L' makes paddle larger; 'S' speeds up paddle");
		instructions[7] = new Text(50, 330, "There are 3 Levels. Destroy all the blocks to win.");
		instructions[8] = new Text(180, 400, "Press SPACE to start!");
		
		for (int i = 0; i < instructions.length; i++)
			root.getChildren().add(instructions[i]);
		
		scene.setOnKeyPressed(e -> handleKeyInputSplash(e.getCode()));
		
		return scene;
	}
	
	private Scene setupLevel (int level, int width, int height, Paint background) {
		ball_launched = false;
		myLevel = new Level (level, width, height, background);
		
		myPaddle = new Rectangle((width - PADDLE_WIDTH)/2, height - PADDLE_HEIGHT - 5, PADDLE_WIDTH, PADDLE_HEIGHT);
        myPaddle.setFill(Color.BLACK);
        
        myBall = new Circle(myPaddle.getX() + myPaddle.getWidth()/2, myPaddle.getY() - BALL_SIZE - 5, BALL_SIZE);
        myBall.setFill(Color.DEEPSKYBLUE);
        
        drawStatus();
        
        myLevel.drawLevel();
        myLevel.addRoot(myPaddle);
        myLevel.addRoot(myBall);
		
        myLevel.getScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        
		return myLevel.getScene();
	}
	
	private void drawStatus() {
		level_text = new Text (10, 20, "Level #" + myLevel.getLevel());
		myLevel.addRoot(level_text);
		
		lives_text = new Text (10, 40, "Lives Left: " + myLives);
		myLevel.addRoot(lives_text);
		
		score_text = new Text (10, 60, "Score: " + myScore);
		myLevel.addRoot(score_text);
	}
	
	private void updateStatus() {
		level_text.setText("Level #" + myLevel.getLevel());
		lives_text.setText("Lives Left: " + myLives);
		score_text.setText("Score: " + myScore);
	}
	
	private void handleKeyInputSplash(KeyCode code) {
		if (code == KeyCode.SPACE) {
			game_begun = true;
			myStage.setScene(setupLevel(1, SIZE, SIZE, BACKGROUND));
		}
	}

	private void handleKeyInput(KeyCode code) {
		if (code == KeyCode.UP) {
			ball_launched = true;
		}
		if (code == KeyCode.RIGHT) {
			movePaddle(1);
        }
        else if (code == KeyCode.LEFT) {
        		movePaddle(-1);
        }
		
		if (code == KeyCode.R) {
			resetPaddle();
		}
		if (code == KeyCode.DIGIT1) {
			myStage.setScene(setupLevel(1, SIZE, SIZE, BACKGROUND));
		}
		if (code == KeyCode.DIGIT2) {
			myStage.setScene(setupLevel(2, SIZE, SIZE, BACKGROUND));
		}
		if (code == KeyCode.DIGIT3) {
			myStage.setScene(setupLevel(3, SIZE, SIZE, BACKGROUND));
		}
	}
	
	private void movePaddle (int direction) {
		myPaddle.setX(myPaddle.getX() + direction*paddle_speed);
		if (!ball_launched)
			myBall.setCenterX(myBall.getCenterX() + direction*paddle_speed);
	}
	
	private void resetPaddle() {
		myBall.setCenterX(myPaddle.getX() + myPaddle.getWidth()/2);
		myBall.setCenterY(myPaddle.getY() - BALL_SIZE - 5);
		ball_angle = Math.random() * 120 + 210;
		ball_launched = false;
		if (ball_destroyed) {
			myLevel.addRoot(myBall);
			ball_destroyed = false;
		}
	}
	
	private void ensurePaddleInStage() {
		if (myPaddle.getX() <= myLevel.getSides()[0].getEndX()) {
			myPaddle.setX(myLevel.getSides()[0].getEndX());
			if (!ball_launched)
				myBall.setCenterX(myLevel.getSides()[0].getEndX() + myPaddle.getWidth()/2);
		}
		if (myPaddle.getX() + myPaddle.getWidth() >= myLevel.getSides()[0].getStartX()) {
			myPaddle.setX(myLevel.getSides()[0].getStartX() - myPaddle.getWidth());
			if (!ball_launched)
				myBall.setCenterX(myLevel.getSides()[0].getStartX() - myPaddle.getWidth()/2);
		}
	}
	
	private void handleBallFallingOff() {
		if(myBall.getCenterY() + myBall.getRadius() > (myPaddle.getY() + 5)) {
			myLevel.removeFromRoot(myBall);
			ball_destroyed = true;
			if (myLives > 0) {
				myLives --;
				resetPaddle();
			} else {
				endGame("You Lost :(");
			}
			//System.out.println("Ball Destroyed");
		}
	}
	
	
	private void endGame (String text) {
		Group root = new Group();
		Scene scene = new Scene(root, SIZE, SIZE, Color.WHITE);
		
		Text end_text = new Text(200, 250, text);
		
		root.getChildren().add(end_text);
		
		myStage.setScene(scene);
	}
	
	private void ensureBallInStage() {
		for (int i = 1; i < myLevel.getSides().length; i++) {
			Shape intersect = Shape.intersect(myBall, myLevel.getSides()[i]);
			if (intersect.getBoundsInLocal().getWidth() != -1) {
				ball_angle = 2 * myLevel.getNormals()[i] - 180 - ball_angle;
				//System.out.println("Intersection detected on side:" + i);
			}
		}
	}
	
	private void handleBallBouncingOnBlock() {
		for (Block block: myLevel.getBlocks()) {
			for(int i = 0; i < block.getSides().length; i++) {
				Shape intersect_block = Shape.intersect(myBall, block.getSides()[i]);
				if (intersect_block.getBoundsInLocal().getWidth() != -1) {
					Platform.runLater(new Runnable() {
		                 @Override public void run() {
		                	 	myLevel.removeFromRoot(block.getBlock());
		                	 	myLevel.removeBlock(block);
		                 }
		             });
					myScore += 10;
					//System.out.println("Intersection with Side" + i + " Normal Angle:" + block.getNormals()[i]);
					ball_angle =  2 * block.getNormals()[i] - 180 - ball_angle;
					if(block.getType() == '+')
						myLives++;
					if(block.getType() == 'P') {
						//paddle_speed = 15;
					}
				}
			}
		}
	}
	
	private void checkForNextLevel() {
		if(myLevel.getBlocks().size() == 0) {
			if (myLevel.getLevel() < 3)
				myStage.setScene(setupLevel(myLevel.getLevel()+1, SIZE, SIZE, BACKGROUND));
			else
				endGame("You Won!!");
		}
	}

	private void step(double elapsedTime) {
		if(game_begun) {
			updateStatus();
			
			ensurePaddleInStage();
			
			if (ball_launched && !ball_destroyed) {
				checkForNextLevel();
				
				handleBallFallingOff();
				
				ensureBallInStage();
				
				//Bounce Ball off Paddle
				Shape intersect = Shape.intersect(myBall, myPaddle);
				if (intersect.getBoundsInLocal().getWidth() != -1)
					ball_angle =  2 * (-90) - 180 - ball_angle;
				
				handleBallBouncingOnBlock();
				
				//Update Ball Position
				myBall.setCenterX(myBall.getCenterX() + elapsedTime * ball_speed * Math.cos(Math.toRadians(ball_angle)));
				myBall.setCenterY(myBall.getCenterY() + elapsedTime * ball_speed * Math.sin(Math.toRadians(ball_angle)));
			}
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
