package com;

import javafx.application.Application;

import com.Tree;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Launch extends Application {

	Tree<String> tree;
	boolean mousePressed = false;
	UI ui;

	@Override
	public void start(Stage primaryStage) {
		ui = new UI(primaryStage);

		tree = ui.generateTree();

		ui.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println(e);
				if (!(e.getButton() == MouseButton.SECONDARY)) {
					return;
				}
				mousePressed = true;
				tree.dessine(0, 0, e.getSceneX(), e.getSceneY());
				ui.reveilNode(tree, e.getSceneX(), e.getSceneY());
				ui.addPoint(e.getSceneX(), e.getSceneY());
				ui.scene.setCursor(Cursor.CROSSHAIR);
			}
		});

		ui.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (!rightButtonOnly(e) && !(e.getButton() == MouseButton.PRIMARY)) {
					return;
				}

				if (ui.nextTree != null) {
					if (ui.nextTree.isLeaf()) {
						Tree<String> current = ui.nextTree;
						String s = ui.nextTree.getData();
						while (current.parent != null) {
							current = current.parent;
							s = current.getData() + " > " + s;
						}
						ui.result.setText("Result:" + s);
					} else {
						if (e.getButton() == MouseButton.PRIMARY) {
							ui.nextStep();
							ui.cd.stop();
							return;
						}
					}
				}
				ui.endortNode(tree);
				ui.root.getChildren().clear();
				ui.root.getChildren().add(ui.result);
				if (ui.cd != null)
					ui.cd.stop();
				ui.scene.setCursor(Cursor.DEFAULT);
				mousePressed = false;
			}
		});

		ui.scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (!rightButtonOnly(e)) {
					return;
				}

				ui.updateLines(e.getSceneX(), e.getSceneY());
				ui.refreshCd();
			}
		});

	}

	public boolean rightButtonOnly(MouseEvent e) {
		return (e.getButton() == MouseButton.SECONDARY) && mousePressed;
	}

	public static double distance(double x, double y, double x2, double y2) {
		return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
