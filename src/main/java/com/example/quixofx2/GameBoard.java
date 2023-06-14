package com.example.quixofx2;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GameBoard  extends GridPane {
    private static final int BOARD_SIZE = 5;
    private static final int CELL_SIZE = 50;
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color HIGHLIGHTED_COLOR = Color.GREEN;

    private Cube selected = null;
    private ArrayList<Cube> highlighted;

    public GameBoard(){
        super();
        highlighted = new ArrayList<>();
        setAlignment(Pos.CENTER);
        initialize();
    }

    private void initialize() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                Cube cube = new Cube(CELL_SIZE, row, column);
                cube.setOnMouseClicked(event -> handleCellClicked(cube));
                add(cube, column, row);
            }
        }

    }

    private void handleCellClicked(Cube cube){
        if(selected != null && cube.isHighlighted()){
            swapCubes(selected, cube);
        }else if(cube.getCol() == 0 || cube.getCol() == 4 || cube.getRow() == 0 || cube.getRow() == 4) {
            if (cube.isSelected()) {
                clearSelectedCell();
                removeHighlights();
            } else if (selected == null) {
                setSelectedCell(cube);
                highlightPotentials(cube);
            }
        }
    }

    private void swapCubes(Cube selected, Cube cube) {
        //up
        if(selected.getRow() > cube.getRow()) {

        }
        //down
        if(selected.getRow() < cube.getRow()){

        }
        //left
        if(selected.getCol() > cube.getCol()){

        }
        //right
        if(selected.getCol() < cube.getCol()){
            Cube tmp = new Cube(selected);
            getChildren().remove(selected);
            for(int i = selected.getCol() + 1; i < BOARD_SIZE; i++){
                Cube c = getCubeByRowCol(selected.getRow(), i);
                getChildren().remove(c);
                getChildren().add(tmp.getIndex(BOARD_SIZE), c);
                tmp = new Cube(c);
                c.setRow(tmp.getRow());
                c.setCol(tmp.getCol());
            }
            getChildren().add(tmp.getIndex(BOARD_SIZE), selected);
            selected.setCol(tmp.getCol());
            selected.setRow(tmp.getRow());
        }
        clearSelectedCell();
        removeHighlights();
    }

    private Cube getCubeByRowCol(int row, int col){
        for(Node n : getChildren()){
            Cube c = (Cube) n;
            if(c.getCol() == col && c.getRow() == row){
                return c;
            }
        }
        return null;
    }

    private void highlightPotentials(Cube cube) {
        for (int i = 0; i < getChildren().size(); i++) {
            Cube c = (Cube) getChildren().get(i);
            if (GridPane.getColumnIndex(c) == cube.getCol() && (GridPane.getRowIndex(c) == 0 || GridPane.getRowIndex(c) == 4) && GridPane.getRowIndex(c) != cube.getRow()
                    || (GridPane.getColumnIndex(c) == 0 || GridPane.getColumnIndex(c) == 4) && GridPane.getRowIndex(c) == cube.getRow() && GridPane.getColumnIndex(c) != cube.getCol()) {
                c.setHighlighted(true);
                c.setFill(HIGHLIGHTED_COLOR);
                highlighted.add(c);
            }
        }
    }

    private void setSelectedCell(Cube cube) {
        selected = cube;
        selected.setSelected(true);
        selected.setFill(SELECTED_COLOR);
    }

    private void clearSelectedCell() {
        selected.setSelected(false);
        selected.setBasicColor();
        selected = null;
    }

    private void removeHighlights() {
        for (Cube cube : highlighted) {
            cube.setHighlighted(false);
            cube.setBasicColor();
        }
        highlighted.clear();
    }
}
