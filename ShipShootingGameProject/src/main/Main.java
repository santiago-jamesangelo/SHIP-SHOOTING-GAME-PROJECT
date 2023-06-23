/****************************************************************************************************
 * CMSC 22 Object-Oriented Programming
 * Topic: Mini-Project
 *
 * This is a mini-ship shooting game application as a mini-project
 *
 * @author James Angelo L. Santiago
 * @date 2022-06-09 15:00
 *
 *
 * *************************************************************************************************/
package main;

import javafx.application.Application;
import javafx.stage.Stage;
import miniprojtemplate.GameStage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage){
		GameStage theGameStage = new GameStage();
		theGameStage.setStage(stage);
	}

}
