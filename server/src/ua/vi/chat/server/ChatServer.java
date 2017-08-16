package ua.vi.chat.server;

import ua.vi.network.TCPConn;
import ua.vi.network.TCPConnListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by Vitalii on 15.08.2017.
 */
public class ChatServer implements TCPConnListener {

    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConn> conns = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while(true){
                try {
                    new TCPConn(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCP Exception: " + e);
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public synchronized void onConnReady(TCPConn tcpconn) {
        conns.add(tcpconn);
        sentToAllConn("Client connected: " + tcpconn);
    }

    @Override
    public synchronized void onReceiveString(TCPConn tcpconn, String value) {
        sentToAllConn(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConn tcpconn) {
        conns.remove(tcpconn);
        sentToAllConn("Client disconnected: " + tcpconn);
    }

    @Override
    public synchronized void onException(TCPConn tcpconn, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sentToAllConn(String value){
        System.out.println(value);
        for (int i = 0; i < conns.size(); i++) {
            conns.get(i).sendString(value);
        }
    }
}
