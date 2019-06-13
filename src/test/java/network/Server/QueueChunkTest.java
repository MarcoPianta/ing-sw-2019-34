package network.Server;

import network.Client.Socket.SocketClient;
import network.messages.GameSettingsResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import view.gui.MainGuiView;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class QueueChunkTest {
    private static QueueChunk queue;
    private static Server server;

    @BeforeAll
    public static void beforeAll() {
        server = new Server();
        queue = new QueueChunk(server);
    }

    @Test
    public void addPlayerTest() throws InterruptedException{
        for(int i = 0; i < 2; i++){
            queue.addPlayer(newClient());
        }
        assertEquals(2, queue.getQueueSize());
        //Thread.sleep(31000);
        assertEquals(2, queue.getQueueSize());

        for(int i = 0; i < 2; i++){
            Integer token = newClient();
            queue.addPlayer(token);
            assertEquals(3+i, queue.getQueueSize());
            queue.setPreferences(new GameSettingsResponse(token, 5, 1));
        }
        TimeUnit.MILLISECONDS.sleep(QueueChunk.QUEUEWAITTIME+1000);
        assertEquals(0, queue.getQueueSize());
    }

    private Integer newClient(){
        SocketClient s;
        synchronized (this) {
            MainGuiView mainGuiView = new MainGuiView();
            mainGuiView.setClient(s = new SocketClient("localhost", 10000, mainGuiView));
            mainGuiView.main(null);
        }
        while(s.getToken()==null){}
        return s.getToken();
    }
}
