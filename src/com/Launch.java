package com;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import java.util.List;

import com.Tree;
import com.UI.Countdown;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Launch extends Application {
	
	Tree<String> tree;
		
	UI ui;
	    
    @Override
    public void start(Stage primaryStage) {
        ui = new UI(primaryStage);
        
        tree = ui.generateTree();
        
        ui.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				tree.dessine(0, 0, e.getSceneX(), e.getSceneY());
				ui.reveilNode(tree, e.getSceneX(), e.getSceneY());
				ui.addPoint(e.getSceneX(), e.getSceneY());
			}
        });
        
        ui.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				ui.endortNode(tree);
				ui.root.getChildren().clear();
				if (ui.cd != null) ui.cd.stop();
			}
        });
        
        ui.scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				ui.updateLines(e.getSceneX(), e.getSceneY());
				ui.refreshCd();
			}
        });
       
    }


    
    public static double distance(double x, double y, double x2, double y2) {
    	return Math.sqrt((x-x2)*(x-x2) + (y-y2)*(y-y2));
    }

    
    public static void main(String[] args) {
        launch(args);
    }

}
