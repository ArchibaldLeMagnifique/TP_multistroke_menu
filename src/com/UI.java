package com;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;

import com.Tree;

public class UI {

	int screenWidth = 1200;
	int screenHeight = 700;
	Stage stage;
	Group root;
	Scene scene;

	ArrayList<Line> mousePos;

	Tree<String> currentTree;
	Tree<String> nextTree; //le noeud en gras
	
	Countdown cd;


	public UI(Stage stage) {
		this.stage = stage;
		stage.setTitle("TP multistroke menu");
		root = new Group();
		scene = new Scene(root, screenWidth, screenHeight);
		stage.setScene(scene);
		stage.show();
		mousePos = new ArrayList<Line>();
	}

	public void reveilNode(Tree<String> tree, double X, double Y) {
		tree.reveil(X, Y);
		currentTree = tree;
	}

	public void endortNode(Tree<String> tree) {
		if (tree.isLeaf()) {
			tree.endort();
			return;
		}
		for(int i=0; i<tree.getChildren().size(); i++) {
			endortNode(tree.getChildren().get(i));
		}
	}

	public void updateLines(double X, double Y) {
		mousePos.get(mousePos.size()-1).setEndX(X);
		mousePos.get(mousePos.size()-1).setEndY(Y);
		mousePos.get(mousePos.size()-1).toBack();

		if (this.distance(currentTree.centerX, currentTree.centerY, X, Y) < 20) {
			this.nextTree = null;
		} else if (!currentTree.getChildren().isEmpty()){
			this.nextTree = currentTree.getChildren().get(0);
			for (int i=1; i<currentTree.getChildren().size(); i++) {
				if (this.distance(this.nextTree.centerX, this.nextTree.centerY, X, Y) > this.distance(currentTree.getChildren().get(i).centerX, currentTree.getChildren().get(i).centerY, X, Y))
					this.nextTree = currentTree.getChildren().get(i);
			}
		}
		for (int i=0; i<currentTree.getChildren().size(); i++) {
			if (currentTree.getChildren().get(i) == this.nextTree){
				currentTree.getChildren().get(i).rect.setStrokeWidth(3);
			} else {
				currentTree.getChildren().get(i).rect.setStrokeWidth(0);
			}
		}
	}

	public void nextStep() {
		if (nextTree != null) {
			double tmpX = this.mousePos.get(mousePos.size()-1).getEndX();
			double tmpY = this.mousePos.get(mousePos.size()-1).getEndY();

			this.mousePos.get(mousePos.size()-1).setEndX(nextTree.centerX);
			this.mousePos.get(mousePos.size()-1).setEndY(nextTree.centerY);
			this.addPoint(nextTree.centerX,nextTree.centerY);
			reveilNode(nextTree, nextTree.centerX,nextTree.centerY);
			
			if (nextTree.isLeaf()) {
				nextTree.rect.setStroke(Color.ORANGE);
			}
			
			currentTree = nextTree;
			updateLines(tmpX, tmpY);
			this.refreshCd();
		}
	}

	public void addPoint(double X, double Y) {
		Line line = new Line(X, Y, X, Y);
		root.getChildren().add(line);
		mousePos.add(line);
	}

	public void resetPoints() {
		mousePos = new ArrayList<Line>();
	}

	public double distance(double x, double y, double x2, double y2) {
		return Math.sqrt((x-x2)*(x-x2) + (y-y2)*(y-y2));
	}

	public Tree<String> generateTree() {
		Tree<String> tree = new Tree<String>("", root);
		Tree<String> childNode1 = new Tree<String>("tools", tree, root);
		Tree<String> childNode2 = new Tree<String>("weapons", tree, root);
		Tree<String> childNode3 = new Tree<String>("spells", tree, root);
		Tree<String> childNode4 = new Tree<String>("menu", tree, root);


		Tree<String> childNode11 = new Tree<String>("pickaxe", childNode1, root);
		Tree<String> childNode12 = new Tree<String>("hammer", childNode1, root);
		Tree<String> childNode13 = new Tree<String>("shovel", childNode1, root);

		return tree;
	}
	
	public void refreshCd() {
		if (cd!=null) cd.stop();
		cd = new Countdown(this);
	}
	
	class Countdown {

    	double cd;
    	UI ui;
    	private AnimationTimer anim;
    	
    	public Countdown(UI ui) {
    		this.ui=ui;
    		start();
    	}
    	
    	public void stop () {
    		anim.stop();
    	}
    	
    	public void start () {
    		cd = 60/2;
    		anim = new AnimationTimer() {
    			public void handle(long arg0) {
    				cd--;
    				if (cd < 0) {
    					ui.nextStep();
    					this.stop();
    				}
    			}
    		};
    		anim.start();
    	}
    }

}
