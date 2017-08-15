package ua.vi.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Vitalii on 15.08.2017.
 */
public class TCPConn {
    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConn(Socket socket) throws IOException {
        

    }
}
