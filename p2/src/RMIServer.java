

/**
 * @author CGJ
 *
 */
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
		ServerSocket server = new ServerSocket(serverPort);
		Thread registry = new Thread(new RMIRegistry(regPort));
		registry.start();
		
//		create remote object here
		
//		MathSequences maths = new MathSequencesImpl();
//        registry.bind("maths", maths);
		
		while(true){
			Socket socket = server.accept();
			Thread dispather = new Thread(new RMIDispathcer(socket));
			dispather.start();
		}
	}
	
	
}
