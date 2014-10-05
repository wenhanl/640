

/**
 * @author CGJ
 *
 */
import net.NetObject;
import net.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class RMIServer {

	public final static int regPort = 15440;
	public final static int serverPort = 15640;

	public static String getHostname() throws UnknownHostException{
		return InetAddress.getLocalHost().getCanonicalHostName();
	}
	public static int getserverPort(){
		return serverPort;
	}
	public static int getregPort(){
		return regPort;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		//ServerSocket server = new ServerSocket(serverPort);
        Server server = new Server(serverPort);
		Thread registry = new Thread(new RMIRegistry(regPort));
		registry.start();
		
//		create remote object here
		
//		MathSequences maths = new MathSequencesImpl();
//        registry.bind("maths", maths);
		
		while(true){
            NetObject rcv = server.listen();

            switch (rcv.type){
                case CONNECTION:
                    System.out.println("New Connection Established");
                    break;
                case DATA:
                    MessageManager msgManager = new MessageManager(server, rcv.sock);
                    RMIMessage inMsg = null;
                    try {
                        inMsg = (RMIMessage) RMIMessage.deserialize(rcv.data);
                    } catch (IOException e) {
                        System.err.println("RMIMessage receive failed: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.err.println("RMIMessage deserialize failed: " + e.getMessage());
                    }

                    Thread dispather = new Thread(new RMIDispathcer(msgManager, inMsg));
                    dispather.start();
                    break;
                case EXCEPTION:
                    System.out.println("Connection reset");
                    break;
                default:
                    System.err.println("Type Error");
            }


		}
	}
	
	
}
