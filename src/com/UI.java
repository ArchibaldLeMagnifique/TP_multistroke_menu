package com;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import com.Tree;

public class UI {
	
	int screenWidth = 1200;
	int screenHeight = 700;
	Stage stage;
	Group root;
	Scene scene;
	
	List<Line> positionSouris;

	
	public UI(Stage stage) {
		this.stage = stage;
		stage.setTitle("TP multistroke menu");
    	root = new Group();
        scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
		stage.show();
	}
	
    
    public void afficheEtage(Tree<String> tree, double X, double Y) {
    	String centre = tree.getData();
    	
    	for (int i=0; i<tree.getChildren().size(); i++) {
    		double angle = i * 2*Math.PI/tree.getChildren().size();
    		Text t;
    		if (tree.getChildren().get(i).isLeaf()) {
    			t = new Text(tree.getChildren().get(i).getData());
    		} else {
    			t = new Text(tree.getChildren().get(i).getData() + " ▾");
    		}
    		t.setFont(Font.font ("Verdana", 20));
    		double posX = X + Math.cos(angle)*90;
    		double posY = Y + Math.sin(angle)*70;
    		
    		t.setX(posX - t.getLayoutBounds().getWidth()/2);t.setY(posY + t.getLayoutBounds().getHeight()/4);
    		
    		Rectangle rect = new Rectangle(posX - t.getLayoutBounds().getWidth()/2-5, posY - t.getLayoutBounds().getHeight()/2, t.getLayoutBounds().getWidth()+10, t.getLayoutBounds().getHeight());
    		rect.setFill(Color.web("#dddddd"));
    		rect.setArcHeight(20);
    	    rect.setArcWidth(20);
    		
    	    Line l = new Line(posX, posY, X*2/3+posX/3, Y*2/3+posY/3);
    	    l.setStroke(Color.web("#dddddd"));
    	    
    	    root.getChildren().add(l);
    		root.getChildren().add(rect);
    		root.getChildren().add(t);

    	}
    	
    	if (centre == "") {
    		Circle cercle = new Circle(X, Y, 5);
    		root.getChildren().add(cercle);
    	}
    }
    
    public void updateLines() {
    	
    }
    
    public void addPoint(double X, double Y) {
		positionSouris.add(new Line(X, Y, X, Y));
    }
    
    public void resetPoints() {
    	positionSouris = null;
    }

}
