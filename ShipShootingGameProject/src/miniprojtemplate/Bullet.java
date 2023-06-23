package miniprojtemplate;

import javafx.scene.image.Image;

public class Bullet extends Sprite {

	private final static int BULLET_SPEED = 5;
	public final static Image BULLET_IMAGE = new Image("images/bullet.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static int BULLET_WIDTH = 100;

	public Bullet(int x, int y){
		super(x,y);
		this.loadImage(Bullet.BULLET_IMAGE);
	}

	//Method that will move/change the x position of the bullet
	void move(){
		/*
		 * TODO: Change the x position of the bullet depending on the bullet speed.
		 * 		 If the x position has reached the right boundary of the screen,
		 * 		 set the bullet's visibility to false.
		 */
		this.x += Bullet.BULLET_SPEED;	//Change the x position of the bullet depending on the bullet speed.
		if(this.x >= GameStage.WINDOW_WIDTH){				// if this bullet passes through the edge of the scene, set visible to false
			this.setVisible(false);
		}
	}
}