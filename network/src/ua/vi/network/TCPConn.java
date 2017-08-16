package ua.vi.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Vitalii on 15.08.2017.
 */
public class TCPConn {
    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnListener eventListener;

    public TCPConn(TCPConnListener eventListener, String ipAddr, int port) throws IOException{
        this(eventListener, new Socket(ipAddr,port));
    }

    public TCPConn(TCPConnListener eventListener, Socket socket) throws IOException {
        this.socket=socket;
        this.eventListener=eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnReady(TCPConn.this);
                    while(!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConn.this,in.readLine());
                    }
                } catch (IOException e){
                    eventListener.onException(TCPConn.this, e);
                }finally {

                    eventListener.onDisconnect(TCPConn.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String value){
        try{
            out.write(value +"\r\n");
            out.flush();
        } catch (IOException e){
            eventListener.onException(TCPConn.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e){
            eventListener.onException(TCPConn.this, e);
        }

    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
