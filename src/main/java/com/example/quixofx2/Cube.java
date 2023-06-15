package com.example.quixofx2;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Cube extends StackPane {
    private State state;
    private Rectangle rectangle;
    private Text text;
    private int row;
    private int col;
    private boolean selected;
    private boolean highlighted;
    private static final Color BASIC_COLOR = Color.WHITE;

    public Cube(int size){
        rectangle = new Rectangle(size, size);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(BASIC_COLOR);
//        text = new Text("" + getIndex(5));
        text = new Text();
        getChildren().addAll(rectangle, text);
        state = State.NONE;
    }

    public Cube(int size, int row, int col){
        rectangle = new Rectangle(size, size);
        this.row = row;
        this.col = col;
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(BASIC_COLOR);
//        text = new Text("" + getIndex(5));
        text = new Text();
        getChildren().addAll(rectangle, text);
        state = State.NONE;
    }

    public Cube(Cube c){
        rectangle = new Rectangle(c.getRectangle().getWidth(), c.getRectangle().getHeight());
        this.row = c.getRow();
        this.col = c.getCol();
        rectangle.setStroke(c.getRectangle().getStroke());
        rectangle.setFill(c.getRectangle().getFill());
        this.selected = c.isSelected();
        this.highlighted = c.isHighlighted();
//        text = new Text("" + getIndex(5));
        text = new Text();
        getChildren().addAll(rectangle, text);
        state = c.getState();
    }

    public int getIndex(int boardSize){
        return row * boardSize + col;
    }
    public void setBasicColor(){
        rectangle.setFill(BASIC_COLOR);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Text getText() {
        return text;
    }

    public int getRow() {
        return row;
    }


    public int getCol() {
        return col;
    }


    public void setPlacement(int row, int col){
        this.row = row;
        this.col = col;
//        text.setText("" + getIndex(5));
    }

    public void correctPlacement(){
//        text.setText("" + getIndex(5));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public void setState(State state){
        this.state = state;
        text.setText(state.toString());
    }

    public State getState() {
        return state;
    }

    public enum State{
        NONE,
        X,
        O
    }
}
