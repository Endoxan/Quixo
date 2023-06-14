package com.example.quixofx2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cube extends Rectangle {
    private int row;
    private int col;
    private boolean selected;
    private boolean highlighted;
    private static final Color BASIC_COLOR = Color.WHITE;

    public Cube(int size){
        super(size, size);
        this.setStroke(Color.BLACK);
        setFill(BASIC_COLOR);
    }

    public Cube(int size, int row, int col){
        super(size, size);
        this.row = row;
        this.col = col;
        this.setStroke(Color.BLACK);
        setFill(BASIC_COLOR);
    }

    public Cube(Cube c){
        super(c.getWidth(), c.getHeight());
        this.row = c.getRow();
        this.col = c.getCol();
        this.setStroke(c.getStroke());
        this.setFill(c.getFill());
        this.selected = c.isSelected();
        this.highlighted = c.isHighlighted();
    }

    public int getIndex(int boardSize){
        return row * boardSize + col;
    }
    public void setBasicColor(){
        setFill(BASIC_COLOR);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
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
}
