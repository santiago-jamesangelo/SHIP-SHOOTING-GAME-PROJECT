package miniprojtemplate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameStage {
	public static final int WINDOW_HEIGHT = 720;
	public static final int WINDOW_WIDTH = 1280;
	private Scene splashScene;	// the splash scene
	private Scene gameScene;		// the game scene
	private Scene aboutScene;	// the about scene
	private Scene instructionScene;		// the instruction scene

	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;

	private final static Image SPLASH = new Image("images/splashscene.png");
	private final static Image ABOUT = new Image("images/aboutscene.png");
	private final static Image INSTRUCTIONS = new Image("images/instructionsscene.png");

	//the class constructor
	public GameStage() {
		this.root = new Group();
		this.gameScene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.CADETBLUE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.root.getChildren().add(this.canvas);
	}

	//method to add the stage elements
	public void setStage(Stage stage) {
		this.stage = stage;

		this.stage.setTitle("Mini Ship Shooting Game");
		this.initSplash(stage);
		this.initInstructions(stage);
		this.initAbout(stage);

		this.stage.setScene( this.splashScene );
        this.stage.setResizable(false);

		this.stage.show();
	}

	//Setting the properties and elements for the Splash Scene
	private void initSplash(Stage stage) {
		StackPane root = new StackPane();
		VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(30);

        Button b1 = new Button("Start New Game");
        Button b2 = new Button("Instructions");
        Button b3 = new Button("About");

        Font btnFont = Font.font("Open sans",FontWeight.BOLD,30); //customizing the font of the text in the buttons
		b1.setFont(btnFont);
		b2.setFont(btnFont);
		b3.setFont(btnFont);
		b1.setPrefSize(350, 90);
		b2.setPrefSize(350, 90);
		b3.setPrefSize(350, 90);
		b1.setStyle("-fx-focus-color: transparent;");
		b2.setStyle("-fx-focus-color: transparent;");
		b3.setStyle("-fx-focus-color: transparent;");

        vbox.getChildren().addAll(b1,b2,b3);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setGame(stage);		// changes the scene into the game scene
            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setInstructions(stage);		// changes the scene into the instruction scene
            }
        });

        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	setAbout(stage);		// changes the scene into the about scene
            }
        });
        root.getChildren().addAll(this.createCanvas(GameStage.SPLASH),vbox);
        this.splashScene = new Scene(root);
	}

	//Setting the properties and elements for the Instructions Scene
	private void initInstructions(Stage stage) {
		StackPane root = new StackPane();
		VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setSpacing(10);

        Button b1 = new Button("Back to Main Menu");

        Font btnFont = Font.font("Open sans",FontWeight.BOLD,30); //customizing the font of the text in the buttons
		b1.setFont(btnFont);
		b1.setPrefSize(350, 60);
		b1.setStyle("-fx-focus-color: transparent;");

        vbox.getChildren().add(b1);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setSplash(stage);		// changes the scene into the splash
            }
        });

        root.getChildren().addAll(this.createCanvas(GameStage.INSTRUCTIONS),vbox);
        this.instructionScene = new Scene(root);
	}

	//Setting the properties and elements for the About Scene
	private void initAbout(Stage stage) {
		StackPane root = new StackPane();
		VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setSpacing(10);

        Button b1 = new Button("Back to Main Menu");

        Font btnFont = Font.font("Open sans",FontWeight.BOLD,30); //customizing the font of the text in the buttons
		b1.setFont(btnFont);
		b1.setPrefSize(350, 60);
		b1.setStyle("-fx-focus-color: transparent;");

        vbox.getChildren().add(b1);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setSplash(stage);		// changes the scene into the splash
            }
        });

        root.getChildren().addAll(this.createCanvas(GameStage.ABOUT),vbox);
        this.aboutScene = new Scene(root);
	}

	//method for creating a canvas
	private Canvas createCanvas(Image img) {
    	Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img, 0, 0);
        return canvas;
    }

	//method for setting the scene into the game scene
    private void setGame(Stage stage) {
        this.stage.setScene( this.gameScene );
        GraphicsContext gc = canvas.getGraphicsContext2D();

        GameTimer gameTimer = new GameTimer(gc, this.gameScene);
        gameTimer.start();

	}

    //method for setting the scene into the splash scene
    private void setSplash(Stage stage) {
    	this.stage.setScene( this.splashScene );
	}

    //method for setting the scene into the instructions scene
    private void setInstructions(Stage stage) {
        this.stage.setScene( this.instructionScene );
	}

    //method for setting the scene into the about scene
    private void setAbout(Stage stage) {
        this.stage.setScene( this.aboutScene );
	}

    public Stage getStage(){
    	return this.stage;
    }
}

