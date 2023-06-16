package com.example.quixofx2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class QuixoApplication extends Application {

    private GameBoard gameBoard;
    private ClientSideConnection csc;
    private int playerID;
    private int otherPlayer;

    @Override
    public void start(Stage stage) throws IOException {
        gameBoard = new GameBoard(this);
        Scene scene = new Scene(gameBoard, 400, 400);
        connectToServer();
        stage.setTitle("Player #" + playerID + " ("+ (playerID==1?"X":"O") +")");
        stage.setScene(scene);

        if(playerID == 1){
            System.out.println("You are Player  #" + playerID + ". You go first.");
            otherPlayer = 2;
        }else{
            System.out.println("You are Player  #" + playerID + ". Wait for your turn.");
            otherPlayer = 1;
            gameBoard.toggleCubes(false);
        }
        gameBoard.startReceivingMove();
        stage.show();
    }

    public void connectToServer(){
        csc = new ClientSideConnection();
    }

    public void sendMove(Cube selected, Cube cube){
        try {
            csc.dataOut.writeInt(selected.getRow());
            csc.dataOut.writeInt(selected.getCol());
            csc.dataOut.writeInt(cube.getRow());
            csc.dataOut.writeInt(cube.getCol());
            csc.dataOut.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public int[] receiveMove(){
        int[] coordinates = new int[4];
        coordinates[0] = -1;
        coordinates[1] = -1;
        coordinates[2] = -1;
        coordinates[3] = -1;
        try {
            coordinates[0] = csc.dataIn.readInt();
            coordinates[1] = csc.dataIn.readInt();
            coordinates[2] = csc.dataIn.readInt();
            coordinates[3] = csc.dataIn.readInt();
            System.out.println("Player #" + otherPlayer + ": " + coordinates[0] + " " + coordinates[1] + " " + coordinates[2] + " " + coordinates[3]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return coordinates;
    }

    private class ClientSideConnection{
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientSideConnection(){
            System.out.println("-----Client-----");
            try {
                socket = new Socket("localhost", 51734);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerID = dataIn.readInt();
                System.out.println("Connected to server as Player #" + playerID + ".");
            }catch (IOException e){
                System.out.println("IO Exception in ClientCon");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}