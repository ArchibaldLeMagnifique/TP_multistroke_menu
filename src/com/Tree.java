package com;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tree<T> {
	public List<Tree<T>> children = new ArrayList<Tree<T>>();
	public Tree<T> parent = null;
	public T data = null;

	boolean visible;

	Group root;

	Text t;
	Rectangle rect;

	double centerX, centerY;

	public Tree(T data, Group root) {
		visible = false;
		this.data = data;
		this.root = root;	
	}

	public Tree(T data, Tree<T> parent, Group root) {
		this(data, root);
		this.parent = parent;
		parent.children.add(this);
	}

	public void reveil(double X, double Y, double theta) {
		//dessine ces enfants avec amour et compassion
		if (theta == 42){
			for (int i=0; i<this.getChildren().size(); i++) {
				double angle = i * 2*Math.PI/this.getChildren().size();
				double posX = X + Math.cos(angle)*140;
				double posY = Y + Math.sin(angle)*80;
				this.getChildren().get(i).dessine(X, Y, posX, posY);
			}
		} else{
			for (int i=0; i<this.getChildren().size(); i++) {
				double angle = 0.7*(i * Math.PI/(this.getChildren().size()-1)-Math.PI/2)-theta*0.92;
				double posX = X + Math.cos(angle)*140;
				double posY = Y + Math.sin(angle)*80;
				this.getChildren().get(i).dessine(X, Y, posX, posY);
			}
		}


	}

	public void dessine(double X, double Y, double posX, double posY) {
		this.centerX = posX; this.centerY = posY;
		if (isRoot()) {
			Circle cercle = new Circle(posX, posY, 5);
			root.getChildren().add(cercle);
		} else {
			if (this.isLeaf()) {
				t = new Text(this.getData().toString());
			} else {
				t = new Text(this.getData() + " ▾");
			}
			t.setFont(Font.font ("Verdana", 18));

			t.setX(posX - t.getLayoutBounds().getWidth()/2);t.setY(posY + t.getLayoutBounds().getHeight()/4);

			rect = new Rectangle(posX - t.getLayoutBounds().getWidth()/2-10, posY - t.getLayoutBounds().getHeight()/2, t.getLayoutBounds().getWidth()+20, t.getLayoutBounds().getHeight());
			rect.setFill(Color.web("#bbbbbb"));
			rect.setArcHeight(20);
			rect.setArcWidth(20);
			rect.setStroke(Color.BLACK);
			rect.setStrokeWidth(1);

			Line l = new Line(posX, posY, X*2/3+posX/3, Y*2/3+posY/3);
			l.toBack();
			l.setStroke(Color.web("#dddddd"));

			root.getChildren().add(l);
			root.getChildren().add(rect);
			root.getChildren().add(t);

			this.visible=true;
		}


	}

	public void endort() {
		visible=false;
	}


	public List<Tree<T>> getChildren() {
		return children;
	}

	public void setParent(Tree<T> parent) {
		parent.addChild(this);
		this.parent = parent;
	}

	public void addChild(T data) {
		Tree<T> child = new Tree<T>(data, root);
		child.setParent(this);
		this.children.add(child);
	}

	public void addChild(Tree<T> child) {
		child.setParent(this);
		this.children.add(child);
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isRoot() {
		return (this.parent == null);
	}

	public boolean isLeaf() {
		if(this.children.size() == 0) 
			return true;
		else 
			return false;
	}

	public void removeParent() {
		this.parent = null;
	}


}