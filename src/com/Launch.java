package com;
import javafx.application.Application;

import java.util.List;

import com.Tree;
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
        
        tree = generateTree();
        
        ui.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				ui.addPoint(e.getSceneX(), e.getSceneY());
				ui.afficheEtage(tree,e.getSceneX(),e.getSceneY());
			}
        });
        
        ui.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				ui.resetPoints();
				ui.root.getChildren().clear();	
			}
        });
        
        ui.scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				ui.updateLines();
			}
        });
       
    }

    
    
    public Tree<String> generateTree() {
    	Tree<String> tree = new Tree<String>("");
    	Tree<String> childNode1 = new Tree<String>("tools", tree);
    	Tree<String> childNode2 = new Tree<String>("weapons", tree);
    	Tree<String> childNode3 = new Tree<String>("spells", tree);
    	Tree<String> childNode4 = new Tree<String>("menu", tree);

    	
    	Tree<String> childNode11 = new Tree<String>("pickaxe", childNode1);
    	Tree<String> childNode12 = new Tree<String>("hammer", childNode1);
    	Tree<String> childNode13 = new Tree<String>("shovel", childNode1);

    	return tree;
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
