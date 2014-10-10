

/**
 * @author CGJ
 *
 */
import msg.MessageManager;
import msg.RMIMessage;
import net.NetObject;
import net.Server;

import java.io.IOException;


public class RMIServer {

	public final static int regPort = 15440;
	public final static int serverPort = 15640;
	
	
	public static void main(String[] args) throws IOException {
        Server server = new Server(serverPort);

        // Register remote objects
        RMIRegistry reg = new RMIRegistry(regPort);
        Thread registry = new Thread(reg);
        registry.start();


        // Create two remote objects
        ExampleOne one = new ExampleOne(2.0, "one");
        reg.rebind("ExampleOne", one);
        ExampleTwo two = new ExampleTwo();
        reg.rebind("ExampleTwo", two);


		while(true){
            NetObject rcv = server.listen();

            switch (rcv.type){
                case CONNECTION:
                    System.out.println("Connection Established: On server");
                    break;
                case DATA:
                    MessageManager msgManager = new MessageManager(server, rcv.sock);
                    RMIMessage inMsg = null;
                    try {
                        inMsg = (RMIMessage) RMIMessage.deserialize(rcv.data);
                    } catch (IOException e) {
                        System.out.println("RMIMessage receive failed: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println("RMIMessage deserialize failed: " + e.getMessage());
                    }

                    Thread dispather = new Thread(new RMIDispathcer(msgManager, inMsg));
                    dispather.start();
                    break;
                case EXCEPTION:
                    System.out.println("Connection reset");
                    break;
                default:
                    System.out.println("Type Error");
            }


		}				
	}
	
	
}
