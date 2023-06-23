package miniprojtemplate;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;

public class Dog extends Sprite{
	private String name;
	private int strength, type, score;

	private boolean alive;
	private boolean immortal;
	private boolean bossDamageDelayed;
	private ImmortalTimer timer;
	private BossDamageTimer delay;
	public final static int IMMORTAL_TIMER = 5;

	private ArrayList<Bullet> bullets;
	private final static Image ORDINARY_DOG_IMAGE = new Image("images/ordinarydog.png",Dog.DOG_WIDTH,Dog.DOG_WIDTH,false,false);
	private final static Image UPGRADED_DOG_IMAGE = new Image("images/upgradeddog.png",Dog.DOG_WIDTH,Dog.DOG_WIDTH,false,false);
	public final static int DOG_WIDTH = 100;

	public final static int ORDINARY_DOG = 0;
	public final static int UPGRADED_DOG = 1;

	public static final int MIN_DOG_STRENGTH = 100;
	public static final int MAX_DOG_STRENGTH = 150;

	Dog(String name, int x, int y, int type){
		super(x,y);
		this.name = name;
		this.immortal = false;
		this.bossDamageDelayed = false;
		this.alive = true;

		Random r = new Random();
		this.strength = Dog.MIN_DOG_STRENGTH + r.nextInt(Dog.MAX_DOG_STRENGTH - MIN_DOG_STRENGTH + 1);

		this.bullets = new ArrayList<Bullet>();
		this.x = x; //spawn at the left most part
		this.y = y; //random y position at the start
		this.type = type;
		this.loadImage(this.type==Dog.ORDINARY_DOG?Dog.ORDINARY_DOG_IMAGE:Dog.UPGRADED_DOG_IMAGE); //changes the image of the dog depending if he is immortal or not
	}

	void getHit(int damage){
		this.strength-=damage;
		if(this.strength<=0){
			this.die();
		}
	}

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}

	public String getName(){
		return this.name;
	}

	public int getStrength(){
		return this.strength;
	}

	public void setStrength(int strength){
		this.strength = strength;
	}

	public boolean getImmortal(){
		return this.immortal;
	}

	public void setImmortal(boolean value){
		this.immortal = value;
	}

	//makes use of the BossDamageDelay class to delay the damage when continuously colliding with the cat boss to prevent instant death
	void tempImmortality(){
    	if(this.immortal == false){
    		this.immortal = true;
			this.setType(1);
	    	this.timer = new ImmortalTimer(this);
	    	this.timer.start();
	    }else this.timer.refresh();
    }

	public boolean getbossDamageDelayed(){
		return this.bossDamageDelayed;
	}

	public void setbossDamageDelayed (boolean value){
		this.bossDamageDelayed = value;
	}

	//makes use of the BossDamageTimer class to delay the damage when continuously colliding with the cat boss to prevent instant death
	void delayBossDamage(){
    	if(this.bossDamageDelayed == false){
    		this.bossDamageDelayed = true;
	    	this.delay = new BossDamageTimer(this);
	    	this.delay.start();
	    }else this.delay.refresh();
    }

	public void setType(int type){
		this.type = type;
		this.loadImage(this.type==Dog.ORDINARY_DOG?Dog.ORDINARY_DOG_IMAGE:Dog.UPGRADED_DOG_IMAGE);
	}

	public void die(){
    	this.alive = false;
    }

	//method that will get the bullets 'shot' by the ship
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	//method called if spacebar is pressed
	public void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + Dog.DOG_WIDTH);
		int y = (int) (this.y);
		/*
		 * TODO: Instantiate a new bullet and add it to the bullets arraylist of ship
		 */

		//testing
		this.bullets.add(new Bullet(x,y));
    }

	//updates the kill counter
	void gainScore(){
    	this.score+=1;
    	System.out.println("Score: "+ this.score);
    }

	//doubles the strength when meat poweup is picked up
	void gainStrength(){
    	this.strength = this.strength*2;
    	System.out.println("Strength: "+ this.strength);
    }

	public int getScore(){
		return this.score;
	}


	//method called if up/down/left/right arrow key is pressed.
	void move() {
		/*
		 *TODO: 		Only change the x and y position of the ship if the current x,y position
		 *				is within the gamestage width and height so that the ship won't exit the screen
		 */

		//changing the x and y position of the ship so that the ship will just be within the boundaries of the screen
		if(this.x + this.dx >= 0 && this.x + this.dx <= GameStage.WINDOW_WIDTH - this.width){
			this.x += this.dx;
		}

		if(this.y + this.dy >= 0 && this.y + this.dy <= GameStage.WINDOW_HEIGHT - this.height){
			this.y += this.dy;
		}
	}
}
