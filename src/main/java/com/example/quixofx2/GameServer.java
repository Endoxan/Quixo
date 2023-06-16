package com.example.quixofx2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turn;

    public GameServer(){
        System.out.println("-----Game Server-----");
        turn = 0;
        numPlayers = 0;
        try {
            ss = new ServerSocket(51734);
        }catch (IOException e){
            System.out.println("IO Exception const");
        }
    }

    public void acceptConnections(){
        try {
            System.out.println("Waiting for connections...");
            while(numPlayers < 2){
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected.");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                if(numPlayers == 1){
                    player1 = ssc;
                }else{
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("We now have 2 players, no longer accepting cons.");
        }catch (IOException e){
            System.out.println("IO Exception acceptCon");
        }
    }

    private class ServerSideConnection implements Runnable{
        private Socket socket;
        private DataOutputStream dataOut;
        private DataInputStream dataIn;
        private int playerID;

        private int p1_c1_row;
        private int p1_c1_col;
        private int p1_c2_row;
        private int p1_c2_col;
        private int p2_c1_row;
        private int p2_c1_col;
        private int p2_c2_row;
        private int p2_c2_col;

        public ServerSideConnection(Socket s, int id){
            socket = s;
            playerID = id;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            }catch (IOException e){
                System.out.println("IO Exception SSC const");
            }
        }

        public void run(){
            try {
                dataOut.writeInt(playerID);
                dataOut.flush();
                while (true){
                    if(playerID == 1){
                        p1_c1_row = dataIn.readInt();
                        p1_c1_col = dataIn.readInt();
                        p1_c2_row = dataIn.readInt();
                        p1_c2_col = dataIn.readInt();
                        player2.sendMove(p1_c1_row,p1_c1_col,p1_c2_row,p1_c2_col);
                    }else{
                        p2_c1_row = dataIn.readInt();
                        p2_c1_col = dataIn.readInt();
                        p2_c2_row = dataIn.readInt();
                        p2_c2_col = dataIn.readInt();
                        player1.sendMove(p2_c1_row,p2_c1_col,p2_c2_row,p2_c2_col);
                    }
                }
            }catch (IOException e){
                System.out.println("IO Exception in run SSC");
            }
        }

        public void sendMove(int c1_row, int c1_col, int c2_row, int c2_col) throws IOException {
            dataOut.writeInt(c1_row);
            dataOut.writeInt(c1_col);
            dataOut.writeInt(c2_row);
            dataOut.writeInt(c2_col);
            dataOut.flush();
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
