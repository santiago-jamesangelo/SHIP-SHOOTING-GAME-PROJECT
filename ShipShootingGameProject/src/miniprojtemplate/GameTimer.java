package miniprojtemplate;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method.
 */

public class GameTimer extends AnimationTimer{

	private int gameElapsedTime;
	private long startCatSpawn;
	private long startCatBossSpawn;
	private long startPowerupSpawn;
	private long startGameTimer;

	private GraphicsContext gc;
	private Scene theScene;
	private Canvas canvas;

	private Dog myDog;
	private CatBoss catBoss;
	private ArrayList<Cat> cats;
	private ArrayList<Powerup> powerups;

	//Some of the essential constants
	public static final int NUM_CATS_INITIAL = 7;
	public static final int NUM_CATS_SPAWN = 3;
	public final static double CAT_SPAWN_DELAY = 5;
	public final static double CATBOSS_SPAWN_DELAY = 30;
	public final static double POWERUP_SPAWN_DELAY = 10;
	public final static double WIN_SURVIVAL_TIME = 60;

	private static final Image GAMEBG = new Image("images/gamescene.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,false,false);
	private final static Image GAMEOVER_LOSE = new Image("images/gameoverlose.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,false,false);
	private final static Image GAMEOVER_WIN = new Image("images/gameoverwin.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,false,false);

	GameTimer(GraphicsContext gc, Scene theScene){
		this.startCatSpawn = System.nanoTime();
		this.startCatBossSpawn = System.nanoTime();
		this.startPowerupSpawn = System.nanoTime();
		this.startGameTimer = System.nanoTime();

		this.gc = gc;
		this.theScene = theScene;

		//instantiating Doug, the dog
		Random r = new Random();
		this.myDog = new Dog("Doug",0,r.nextInt((GameStage.WINDOW_HEIGHT - Dog.DOG_WIDTH)),0);

		//instantiating the cat boss
    	int x = (GameStage.WINDOW_WIDTH/2) + r.nextInt(GameStage.WINDOW_WIDTH/2);
		int y = r.nextInt(GameStage.WINDOW_HEIGHT - CatBoss.CATBOSS_WIDTH);
		this.catBoss = new CatBoss(x,y);

		//instantiate the ArrayList of Cats
		this.cats = new ArrayList<Cat>();
		this.powerups = new ArrayList<Powerup>();

		//call the spawnInitialCats method
		this.spawnInitialCats();

		//call method to handle key press event
		this.handleKeyPressEvent();
	}

	@Override
	public void handle(long currentNanoTime) {

		gameElapsedTime = (int) ((currentNanoTime - this.startGameTimer) / 1000000000.0);

		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(GameTimer.GAMEBG, 0, 0);
		this.autoSpawn(currentNanoTime);	//calls the method responsible for spawning the cats, cat boss, and powerups

		this.myDog.move();

		//render the ship
		this.myDog.render(this.gc);

		/*
		 * TODO: Call the necessary move methods
		 */

        this.moveCats();
        this.moveBullets();

		/*
		 * TODO: Call the necessary render methods
		 */

		this.renderCats();
		this.renderBullets();
		this.renderPowerups();
		this.drawGameStatusBar(gameElapsedTime); //draws/updates the game status bar every frame
		this.checkPowerups();					//checks the status of the spawned powerups

		if(!this.myDog.isAlive()) {
        	this.stop();				// stops this AnimationTimer (handle will no longer be called) so all animations will stop
        	this.drawGameOver(0);		// draw Game Over Scene for losing the game
        }else if(gameElapsedTime == GameTimer.WIN_SURVIVAL_TIME){
        	this.stop();				// stops this AnimationTimer (handle will no longer be called) so all animations will stop
        	this.drawGameOver(1);		// draw Game Over Scene for winning the game
        }
	}

	//method for drawing the Game Status bar
	private void drawGameStatusBar(int gameElapsedTime){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText("TIMER: ", 20, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);

		//showing the timer in a minute-format
		if (gameElapsedTime<10){
			this.gc.fillText("0:0"+gameElapsedTime+"", 150, 30);

		}else if(gameElapsedTime >= 10 && gameElapsedTime < 60){
			this.gc.fillText("0:"+gameElapsedTime+"", 150, 30);

		}else if(gameElapsedTime == 60){
			this.gc.fillText("1:00"+"", 150, 30);
		}

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.MIDNIGHTBLUE);
		this.gc.fillText("SCORE: ", 280, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.MIDNIGHTBLUE);
		this.gc.fillText(this.myDog.getScore()+"", 410, 30);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.SADDLEBROWN);
		this.gc.fillText("STRENGTH: ", 490, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.SADDLEBROWN);
		this.gc.fillText(this.myDog.getStrength()+"", 690, 30);

	}

	//method for drawing the Game Over scene depending if the user win or lose, 0 = lose, 1 = win
	private void drawGameOver(int result){
		if(result == 0){
			this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
			this.gc.drawImage(GameTimer.GAMEOVER_LOSE, 0, 0);
			this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
			this.gc.setFill(Color.WHITE);
			this.gc.setTextAlign(TextAlignment.CENTER);
			this.gc.fillText("TOTAL SCORE: "+this.myDog.getScore(), GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT-200);

		}else if(result == 1){
			this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
			this.gc.drawImage(GameTimer.GAMEOVER_WIN, 0, 0);
			this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
			this.gc.setFill(Color.WHITE);
			this.gc.setTextAlign(TextAlignment.CENTER);
			this.gc.fillText("TOTAL SCORE: "+this.myDog.getScore(), GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT-200);
		}
	}

	//method that will render/draw the fishes to the canvas
	private void renderCats() {
		for (Cat c : this.cats){
			c.render(this.gc);
		}
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		/*
		 *TODO: Loop through the bullets arraylist of myShip
		 *				and render each bullet to the canvas
		 */
		//testing
		for (Bullet b : this.myDog.getBullets())
        	b.render( this.gc );
	}

	//method that will render/draw the powerup to the canvas
	private void renderPowerups() {
		for (Powerup p : this.powerups){
			p.render(this.gc);
		}
	}

	//method that is responsible for spawning the cats, cat boss, and powerups
	private void autoSpawn(long currentNanoTime) {
		//keeps track of the elapsed time
    	double catSpawnElapsedTime = (currentNanoTime - this.startCatSpawn) / 1000000000.0;
    	double catBossSpawnElapsedTime = (currentNanoTime - this.startCatBossSpawn) / 1000000000.0;
    	double powerupSpawnElapsedTime = (currentNanoTime - this.startPowerupSpawn) / 1000000000.0;

        // spawn cats
        if(catSpawnElapsedTime > GameTimer.CAT_SPAWN_DELAY) {
        	this.spawnCats();
        	this.startCatSpawn = System.nanoTime();
        }

        //spawn cat boss and keeps track of the cat boss status while cat boss is alive
        if(catBossSpawnElapsedTime > GameTimer.CATBOSS_SPAWN_DELAY) {
        	if(this.catBoss.isVisible()){
        		this.catBoss.render(this.gc);
				this.catBoss.move();
				this.catBoss.checkCollision(this.myDog);

				this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
	    		this.gc.setFill(Color.RED);
	    		this.gc.fillText("BOSS HEALTH: ", 800, 30);
	    		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
	    		this.gc.setFill(Color.RED);
	    		this.gc.fillText(this.catBoss.getHealth()+"", 1060, 30);
			}
        }

        //delete powerup after 5 seconds
        if(powerupSpawnElapsedTime >= Powerup.POWERUP_TIMER) {
        	for(int i = 0; i < this.powerups.size(); i++){
    			Powerup p = this.powerups.get(i);
				p.setVisible(false);
        	}
        }

        //spawn powerup
        if(powerupSpawnElapsedTime > GameTimer.POWERUP_SPAWN_DELAY) {
        	this.spawnPowerups();
        	this.startPowerupSpawn = System.nanoTime();
        }

    }

	//method that will spawn/instantiate 7 cats at a random x,y location
	private void spawnInitialCats(){
		Random r = new Random();
		for(int i=0;i<GameTimer.NUM_CATS_INITIAL;i++){
			int x = (GameStage.WINDOW_WIDTH/2) + r.nextInt(GameStage.WINDOW_WIDTH/2);
			int y = r.nextInt(GameStage.WINDOW_HEIGHT - Cat.CAT_WIDTH);
			this.cats.add(new Cat(x, y));
		}
	}

	//method that will spawn/instantiate 3 cats at a random x,y location
	private void spawnCats(){
		Random r = new Random();
		for(int i=0;i<GameTimer.NUM_CATS_SPAWN;i++){
			int x = (GameStage.WINDOW_WIDTH/2) + r.nextInt(GameStage.WINDOW_WIDTH/2);
			int y = r.nextInt(GameStage.WINDOW_HEIGHT - Cat.CAT_WIDTH);
			/*
			 *TODO: Add a new object Cat to the cats arraylist
			 */
			this.cats.add(new Cat(x, y));
		}
	}

	//method that will spawn a random powerup on the left side of the screen
	private void spawnPowerups(){
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2);
		int y = r.nextInt(GameStage.WINDOW_HEIGHT-Powerup.POWERUP_WIDTH);
		this.powerups.add(new Powerup(x, y)); //Add a new object Powerup to the powerups arraylist
	}

	//method that will move the bullets shot by the dog
	private void moveBullets(){
		//create a local arraylist of Bullets for the bullets 'shot' by the dog
		ArrayList<Bullet> bList = this.myDog.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			/*
			 * TODO:  If a bullet is visible, move the bullet, else, remove the bullet from the bullet array list.
			 */
			if(b.isVisible())
				b.move();
			else bList.remove(i);
		}
	}

	//method that will move the cats
	private void moveCats(){
		//Loop through the cats arraylist
		for(int i = 0; i < this.cats.size(); i++){
			Cat c = this.cats.get(i);
			/*
			 * TODO:  *If a cat is alive, move the cat. Else, remove the cat from the cats arraylist.
			 */
			if(c.isVisible()){
				c.move();
				c.checkCollision(this.myDog);
			}
			else this.cats.remove(i);
		}
	}

	private void checkPowerups(){
		//Loop through the powerup arraylist
		for(int i = 0; i < this.powerups.size(); i++){
			Powerup p = this.powerups.get(i);
			if(p.isVisible()){
				p.checkCollision(this.myDog);
			}
			else this.powerups.remove(i);
		}
	}

	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveMyDog(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopMyDog(code);
		            }
		        });
    }

	//method that will move the ship depending on the key pressed
	private void moveMyDog(KeyCode ke) {
		if(ke==KeyCode.W) this.myDog.setDY(-3);

		if(ke==KeyCode.A) this.myDog.setDX(-3);

		if(ke==KeyCode.S) this.myDog.setDY(3);

		if(ke==KeyCode.D) this.myDog.setDX(3);

		if(ke==KeyCode.SPACE) this.myDog.shoot();

		System.out.println(ke+" key pressed.");
   	}

	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopMyDog(KeyCode ke){
		this.myDog.setDX(0);
		this.myDog.setDY(0);
	}

}
