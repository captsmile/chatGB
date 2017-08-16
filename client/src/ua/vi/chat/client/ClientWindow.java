package ua.vi.chat.client;

import ua.vi.network.TCPConn;
import ua.vi.network.TCPConnListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Vitalii on 15.08.2017.
 */
public class ClientWindow extends JFrame implements ActionListener, TCPConnListener {

    private  static final String IP_ADDR = "192.168.2.170";
    private  static final int PORT = 8189;
    private  static final int WIDTH = 600;
    private  static final int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldsNickName = new JTextField("alex");
    private final JTextField fieldsInput = new JTextField();

    private TCPConn conn;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        add(fieldsInput,BorderLayout.SOUTH);
        add(fieldsNickName, BorderLayout.NORTH);

        fieldsInput.addActionListener(this);

        setVisible(true);
        try {
            conn = new TCPConn(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldsInput.getText();
        if (msg.equals("")) return;
        fieldsInput.setText(null);
        conn.sendString(fieldsNickName.getText() + ": " + msg);
    }


    @Override
    public void onConnReady(TCPConn tcpconn) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConn tcpconn, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConn tcpconn) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConn tcpconn, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
