package ua.vi.network;

/**
 * Created by Vitalii on 15.08.2017.
 */
public interface TCPConnListener {
    void onConnReady(TCPConn tcpconn);
    void onReceiveString(TCPConn tcpconn, String value);
    void onDisconnect(TCPConn tcpconn);
    void onException (TCPConn tcpconn, Exception e);
}
