package com;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	Text result, info1, info2;
	
	String mode;

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
		
		
		
		Text info1 = new Text("Instructions:");
		info1.setX(10);
		info1.setY(scene.getHeight()-80);
		info1.setFill(Color.LIGHTGRAY);
		info1.setFont(Font.font ("Verdana", 22));
		root.getChildren().add(info1);
		Text info2 = new Text("• Clic gauche pour le mode normal\n• Clic droit pour le mode expert");
		info2.setX(20);
		info2.setY(scene.getHeight()-50);
		info2.setFill(Color.LIGHTGRAY);
		info2.setFont(Font.font ("Verdana", 18));
		root.getChildren().add(info2);
		
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        if (info1!=null) info1.setY(scene.getHeight()-80);
		        if (info2!=null) info2.setY(scene.getHeight()-50);
		    }
		});
	}

	public void reveilNode(Tree<String> tree, double X, double Y) {
		if (tree.isRoot()) {
			tree.reveil(X, Y, 42, mode);
		} else {
			if (tree.centerY-tree.parent.centerY < 0) {
				tree.reveil(X, Y, Math.acos((tree.centerX-tree.parent.centerX)/distance(tree.centerX,tree.centerY,tree.parent.centerX,tree.parent.centerY)), mode);
			} else {
				tree.reveil(X, Y, -Math.acos((tree.centerX-tree.parent.centerX)/distance(tree.centerX,tree.centerY,tree.parent.centerX,tree.parent.centerY)), mode);
			}
		}
		currentTree = tree;
	}

	public void updateLines(double X, double Y) {
		mousePos.get(mousePos.size()-1).setEndX(X);
		mousePos.get(mousePos.size()-1).setEndY(Y);
		mousePos.get(mousePos.size()-1).toBack();

		if (this.distance(currentTree.centerX, currentTree.centerY, X, Y) < 65) {
			this.nextTree = null;
		} else if (!currentTree.getChildren().isEmpty()){
			this.nextTree = currentTree.getChildren().get(0);
			for (int i=1; i<currentTree.getChildren().size(); i++) {
				if (this.distance(this.nextTree.centerX, this.nextTree.centerY, X, Y) > this.distance(currentTree.getChildren().get(i).centerX, currentTree.getChildren().get(i).centerY, X, Y))
					this.nextTree = currentTree.getChildren().get(i);
			}
		}
		if (mode != "PRIMARY") return;
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
		if (mode != "PRIMARY") {
			line.setStroke(Color.WHITE);
			line.setStrokeWidth(2);
		}
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
		Tree<String> tree = new Tree<String>("", this);
		Tree<String> childNode1 = new Tree<String>("Add Mesh", tree, this);
		Tree<String> childNode2 = new Tree<String>("Selection", tree, this);
		Tree<String> childNode3 = new Tree<String>("Animation", tree, this);
		Tree<String> childNode4 = new Tree<String>("Apply", tree, this);
		Tree<String> childNode5 = new Tree<String>("Clear", tree, this);
		Tree<String> childNode6 = new Tree<String>("View", tree, this);
		Tree<String> childNode7 = new Tree<String>("Transform", tree, this);


		Tree<String> childNode11 = new Tree<String>("Lamp", childNode1, this);
		Tree<String> childNode12 = new Tree<String>("Mesh", childNode1, this);
		Tree<String> childNode13 = new Tree<String>("Curve", childNode1, this);
		
		Tree<String> childNode121 = new Tree<String>("Cube", childNode12, this);
		Tree<String> childNode132 = new Tree<String>("UV Sphere", childNode12, this);
		Tree<String> childNode123 = new Tree<String>("Cylinder", childNode12, this);
		Tree<String> childNode134 = new Tree<String>("Torus", childNode12, this);
		
		
		Tree<String> childNode21 = new Tree<String>("Inverse", childNode2, this);
		Tree<String> childNode22 = new Tree<String>("(De)select All", childNode2, this);
		
		
		Tree<String> childNode61 = new Tree<String>("Top", childNode6, this);
		Tree<String> childNode62 = new Tree<String>("Align View", childNode6, this);
		Tree<String> childNode63 = new Tree<String>("Right", childNode6, this);
		
		Tree<String> childNode621 = new Tree<String>("Center Cursor", childNode62, this);
		Tree<String> childNode622 = new Tree<String>("View Lock To Active", childNode62, this);
		Tree<String> childNode623 = new Tree<String>("View Selected", childNode62, this);

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
			if (ui.mode == "PRIMARY") {
				cd = 30;
			} else {
				cd = 0;
			}
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
