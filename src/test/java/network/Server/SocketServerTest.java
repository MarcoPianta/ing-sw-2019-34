package network.Server;

import network.Client.Socket.SocketClient;
import network.Server.Socket.SocketServer;
import network.messages.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SocketServerTest {
    private static final int PORT = 10000;
    private static Server server;

    @BeforeAll
    public static void beforeAll() throws IOException{
        server = new Server();
    }

    /**
     * This method tests if the server can accept connection from new clients
     * */
    @Test
    public void runTest() throws IOException{
        SocketServer socketServer = new SocketServer(server, PORT);
        new Thread(() -> {
            try {
                socketServer.run();
            }catch (IOException e){
                //Do nothing
            }
        }).start();
        assertNotNull(new Socket("localhost",10000));
    }

    @Test
    public void onReceiveTest() throws IOException{
        SocketClient socketClient;
        SocketServer socketServer = new SocketServer(server, PORT);
        new Thread(() -> {
            try {
                socketServer.run();
            }catch (IOException e){
                //Do nothing
            }
        }).start();
        socketClient = new SocketClient("localhost", PORT);
        socketClient.init();
        socketClient.send(new Message());
    }
}
