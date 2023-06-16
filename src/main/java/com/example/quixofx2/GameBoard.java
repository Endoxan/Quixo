package com.example.quixofx2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class GameBoard  extends GridPane {
    private QuixoApplication app;
    private static final int BOARD_SIZE = 5;
    private static final int CELL_SIZE = 50;
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color HIGHLIGHTED_COLOR = Color.GREEN;
    private static final Color WINNER_COLOR = Color.RED;
    private int turn;
    private Cube selected = null;
    private ArrayList<Cube> highlighted;

    public GameBoard(QuixoApplication app){
        super();
        this.app = app;
        turn = 0;
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
            app.sendMove(selected, cube);
            swapCubes(selected, cube);
            toggleCubes(false);
        }else if(cube.getState() == Cube.State.NONE && (cube.getCol() == 0 || cube.getCol() == 4 || cube.getRow() == 0 || cube.getRow() == 4)) {
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
            Cube tmp = new Cube(selected);
            getChildren().remove(selected);
            for(int i = selected.getRow() - 1; i >= 0; i--){
                Cube c = getCubeByRowCol(i, selected.getCol());
                getChildren().remove(c);
                setRowIndex(c, tmp.getRow());
                setColumnIndex(c, tmp.getCol());
                getChildren().add(c);
                int tmp_r = tmp.getRow();
                int tmp_c = tmp.getCol();
                tmp = new Cube(c);
                c.setPlacement(tmp_r, tmp_c);
            }
            setRowIndex(selected, tmp.getRow());
            setColumnIndex(selected, tmp.getCol());
            getChildren().add(selected);
            selected.setPlacement(tmp.getRow(), tmp.getCol());
        }else   //down
            if(selected.getRow() < cube.getRow()){
                Cube tmp = new Cube(selected);
                getChildren().remove(selected);
                for(int i = selected.getRow() + 1; i < BOARD_SIZE; i++){
                    Cube c = getCubeByRowCol(i, selected.getCol());
                    getChildren().remove(c);
                    setRowIndex(c, tmp.getRow());
                    setColumnIndex(c, tmp.getCol());
                    getChildren().add(c);
                    int tmp_r = tmp.getRow();
                    int tmp_c = tmp.getCol();
                    tmp = new Cube(c);
                    c.setPlacement(tmp_r, tmp_c);
                }
                setRowIndex(selected, tmp.getRow());
                setColumnIndex(selected, tmp.getCol());
                getChildren().add(selected);
                selected.setPlacement(tmp.getRow(), tmp.getCol());
        }else   //left
            if(selected.getCol() > cube.getCol()){
                Cube tmp = new Cube(selected);
                getChildren().remove(selected);
                for(int i = selected.getCol() - 1; i >= 0; i--){
                    Cube c = getCubeByRowCol(selected.getRow(), i);
                    getChildren().remove(c);
                    setRowIndex(c, tmp.getRow());
                    setColumnIndex(c, tmp.getCol());
                    getChildren().add(c);
                    int tmp_r = tmp.getRow();
                    int tmp_c = tmp.getCol();
                    tmp = new Cube(c);
                    c.setPlacement(tmp_r, tmp_c);
                }
                setRowIndex(selected, tmp.getRow());
                setColumnIndex(selected, tmp.getCol());
                getChildren().add(selected);
                selected.setPlacement(tmp.getRow(), tmp.getCol());
        }else   //right
            if(selected.getCol() < cube.getCol()){
                Cube tmp = new Cube(selected);
                getChildren().remove(selected);
                for(int i = selected.getCol() + 1; i < BOARD_SIZE; i++){
                    Cube c = getCubeByRowCol(selected.getRow(), i);
                    getChildren().remove(c);
                    setRowIndex(c, tmp.getRow());
                    setColumnIndex(c, tmp.getCol());
                    getChildren().add(c);
                    int tmp_r = tmp.getRow();
                    int tmp_c = tmp.getCol();
                    tmp = new Cube(c);
                    c.setPlacement(tmp_r, tmp_c);
                }
                setRowIndex(selected, tmp.getRow());
                setColumnIndex(selected, tmp.getCol());
                getChildren().add(selected);
                selected.setPlacement(tmp.getRow(), tmp.getCol());
        }
        if(turn%2 == 0) selected.setState(Cube.State.X);
        else selected.setState(Cube.State.O);
        turn++;

        clearSelectedCell();
        removeHighlights();

        if(gameWon()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WINNER");
            alert.setHeaderText("Player " + ((turn-1)%2 == 0 ? "X" : "O") + " is the winner");
            alert.setContentText("Press OK to see the result!");
            alert.showAndWait();

            for(Node n : getChildren()){
                Cube c = (Cube) n;
                c.setOnMouseClicked(null);
            }
        }


    }

    private boolean gameWon() {
        if(verticalSearch() || horizontalSearch() || diagonalSearchLeft() || diagonalSearchRight()) return true;
        return false;
    }

    private boolean diagonalSearchRight() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(int i = 1; i < BOARD_SIZE; i++){
            cubes.add(getCubeByRowCol(i-1, BOARD_SIZE - (i-1) - 1));
            if(getCubeByRowCol(i-1, BOARD_SIZE - (i-1) - 1).getState() == Cube.State.NONE ||
                    getCubeByRowCol(i-1, BOARD_SIZE- (i-1) - 1).getState() != getCubeByRowCol(i,BOARD_SIZE - i - 1).getState()) return false;
        }
        cubes.add(getCubeByRowCol(BOARD_SIZE - 1, 0));
        for(Cube c : cubes){
            c.getRectangle().setFill(WINNER_COLOR);
        }
        return true;
    }

    private boolean diagonalSearchLeft() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(int i = 1; i < BOARD_SIZE; i++){
            cubes.add(getCubeByRowCol(i-1, i-1));
            if(getCubeByRowCol(i-1, i-1).getState() == Cube.State.NONE ||
                    getCubeByRowCol(i-1, i-1).getState() != getCubeByRowCol(i,i).getState()) return false;
        }
        cubes.add(getCubeByRowCol(BOARD_SIZE - 1, BOARD_SIZE - 1));
        for(Cube c : cubes){
            c.getRectangle().setFill(WINNER_COLOR);
        }
        return true;
    }

    private boolean verticalSearch() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 1; j < BOARD_SIZE; j++){

                cubes.add(getCubeByRowCol(j-1, i));

                if(getCubeByRowCol(j-1, i).getState() == Cube.State.NONE ||
                        getCubeByRowCol(j-1, i).getState() != getCubeByRowCol(j, i).getState()){
                    cubes.clear();
                    break;
                }
                else if(j == 4){
                    cubes.add(getCubeByRowCol(BOARD_SIZE-1, i));
                    for(Cube c : cubes){
                        c.getRectangle().setFill(WINNER_COLOR);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean horizontalSearch() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 1; j < BOARD_SIZE; j++){

                cubes.add(getCubeByRowCol(i, j-1));

                if(getCubeByRowCol(i, j-1).getState() == Cube.State.NONE ||
                        getCubeByRowCol(i, j-1).getState() != getCubeByRowCol(i, j).getState()){
                    cubes.clear();
                    break;
                }
                else if(j == 4){
                    cubes.add(getCubeByRowCol(i, BOARD_SIZE - 1));
                    for(Cube c : cubes){
                        c.getRectangle().setFill(WINNER_COLOR);
                    }
                    return true;
                }
            }
        }
        return false;
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
                c.getRectangle().setFill(HIGHLIGHTED_COLOR);
                highlighted.add(c);
            }
        }
    }

    private void setSelectedCell(Cube cube) {
        selected = cube;
        selected.setSelected(true);
        selected.getRectangle().setFill(SELECTED_COLOR);
    }

    private void clearSelectedCell() {
        if(selected != null) {
            selected.setSelected(false);
            selected.setBasicColor();
        }
        selected = null;
    }

    private void removeHighlights() {
        for (Cube cube : highlighted) {
            cube.setHighlighted(false);
            cube.setBasicColor();
        }
        highlighted.clear();
    }

    public void startReceivingMove(){
        Thread t = new Thread(new Runnable() {
            int[] coordinates;
            @Override
            public void run() {
                while (true){
                    coordinates = app.receiveMove();
                    if(coordinates[0] != -1 && coordinates[1] != -1 && coordinates[2] != -1 && coordinates[3] != -1){
                        Platform.runLater(() -> swapCubes(getCubeByRowCol(coordinates[0], coordinates[1]), getCubeByRowCol(coordinates[2], coordinates[3])));
                        Platform.runLater(() -> toggleCubes(true));
                    }
                }
            }
        });
        t.start();
    }

    public void toggleCubes(boolean b) {
        for(Node n : getChildren()){
            Cube c = (Cube) n;
            if(b){
                c.setOnMouseClicked(event -> handleCellClicked(c));
            }else c.setOnMouseClicked(null);
        }
    }
}
