package com;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
	
	Text result;


	public UI(Stage stage) {
		this.stage = stage;
		stage.setTitle("TP multistroke menu");
		root = new Group();
		scene = new Scene(root, screenWidth, screenHeight);
		scene.setFill(Color.web("#3b3b3b"));
		stage.setScene(scene);
		stage.show();
		mousePos = new ArrayList<Line>();
		result = new Text("Result: ");
		result.setFont(Font.font ("Verdana", 25));
		result.setFill(Color.WHITE);
		result.setX(10);
		result.setY(result.getLayoutBounds().getHeight());
		root.getChildren().add(result);
	}

	public void reveilNode(Tree<String> tree, double X, double Y) {
		if (tree.isRoot()) {
			tree.reveil(X, Y, 42);
		} else {
			if (tree.centerY-tree.parent.centerY < 0) {
				tree.reveil(X, Y, Math.acos((tree.centerX-tree.parent.centerX)/distance(tree.centerX,tree.centerY,tree.parent.centerX,tree.parent.centerY)));
			} else {
				tree.reveil(X, Y, -Math.acos((tree.centerX-tree.parent.centerX)/distance(tree.centerX,tree.centerY,tree.parent.centerX,tree.parent.centerY)));
			}
		}
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
				currentTree.getChildren().get(i).rect.setFill(new LinearGradient(0, 0, 0, 0.8, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.web("#5b370b")), new Stop(1, Color.web("#ff9f2b"))}));
			} else {
				currentTree.getChildren().get(i).rect.setFill(Color.web("#dddddd"));
			}
		}
	}

	public void nextStep() {
		if (nextTree != null) {
			if (nextTree.isLeaf()) return;
			double tmpX = this.mousePos.get(mousePos.size()-1).getEndX();
			double tmpY = this.mousePos.get(mousePos.size()-1).getEndY();

			this.mousePos.get(mousePos.size()-1).setEndX(nextTree.centerX);
			this.mousePos.get(mousePos.size()-1).setEndY(nextTree.centerY);
			this.addPoint(nextTree.centerX,nextTree.centerY);
			reveilNode(nextTree, nextTree.centerX,nextTree.centerY);
			currentTree = nextTree;
			updateLines(tmpX, tmpY);
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

	@SuppressWarnings("unused")
	public Tree<String> generateTree() {
		Tree<String> tree = new Tree<String>("", root);
		Tree<String> childNode1 = new Tree<String>("Add Mesh", tree, root);
		Tree<String> childNode2 = new Tree<String>("Selection", tree, root);
		Tree<String> childNode3 = new Tree<String>("Animation", tree, root);
		Tree<String> childNode4 = new Tree<String>("Apply", tree, root);
		Tree<String> childNode5 = new Tree<String>("Clear", tree, root);
		Tree<String> childNode6 = new Tree<String>("View", tree, root);
		Tree<String> childNode7 = new Tree<String>("Transform", tree, root);


		Tree<String> childNode11 = new Tree<String>("Lamp", childNode1, root);
		Tree<String> childNode12 = new Tree<String>("Mesh", childNode1, root);
		Tree<String> childNode13 = new Tree<String>("Curve", childNode1, root);
		
		Tree<String> childNode121 = new Tree<String>("Cube", childNode12, root);
		Tree<String> childNode132 = new Tree<String>("UV Sphere", childNode12, root);
		Tree<String> childNode123 = new Tree<String>("Cylinder", childNode12, root);
		Tree<String> childNode134 = new Tree<String>("Torus", childNode12, root);
		
		
		Tree<String> childNode21 = new Tree<String>("Inverse", childNode2, root);
		Tree<String> childNode22 = new Tree<String>("(De)select All", childNode2, root);
		
		
		Tree<String> childNode61 = new Tree<String>("Top", childNode6, root);
		Tree<String> childNode62 = new Tree<String>("Align View", childNode6, root);
		Tree<String> childNode63 = new Tree<String>("Right", childNode6, root);
		
		Tree<String> childNode621 = new Tree<String>("Center Cursor", childNode62, root);
		Tree<String> childNode622 = new Tree<String>("View Lock To Active", childNode62, root);
		Tree<String> childNode623 = new Tree<String>("View Selected", childNode62, root);

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
