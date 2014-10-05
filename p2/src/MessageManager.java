/**
 * @author CGJ
 *
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class MessageManager {

	private String hostname;
	private int port;
	private Socket socket;
	
	public MessageManager (String hostname, int port) throws UnknownHostException, IOException {
		this.hostname = hostname;
        this.port = port;
	    this.socket = new Socket(this.hostname, this.port);
	}
	
	public MessageManager(Socket socket) {
		this.socket = socket;
	    this.hostname = socket.getInetAddress().getCanonicalHostName();
        this.port = socket.getPort();
	}


    public void sendMessage(RMIMessage message) throws IOException {
		ObjectOutputStream sockOut;
		sockOut = new ObjectOutputStream(socket.getOutputStream());
		sockOut.writeObject(message);
		sockOut.flush();
    }

    public RMIMessage receiveMessage() throws IOException, ClassNotFoundException {
		ObjectInputStream sockIn = null;
		sockIn = new ObjectInputStream(socket.getInputStream());
		return (RMIMessage) sockIn.readObject();
    }

    
    
	public void Sendreturnvalue(Object retvalue) throws IOException{
		RMIMessage message = new RMIMessage();
		message.setType(RMIMessage.Type.RETURN);
		message.setretvalue(retvalue);
		sendMessage(message);
	}
	public void SendInvokeMessage(String objectName, String method, Object[] args) throws IOException{
		RMIMessage message = new RMIMessage();
		message.setType(RMIMessage.Type.INVOKE);
		message.setobjectName(objectName);
		message.setmethod(method);
		message.setargs(args);
		sendMessage(message);
	}
	public void SendLookupMessage(String objectName) throws IOException{
		RMIMessage message = new RMIMessage();
		message.setType(RMIMessage.Type.LOOKUP);
		message.setobjectName(objectName);
		sendMessage(message);
	}
	public void SendListMessage() throws IOException{
		RMIMessage message = new RMIMessage();
		message.setType(RMIMessage.Type.LIST);
		sendMessage(message);
	}
	public void SendExpectionMessage(Exception exception) throws IOException{
		RMIMessage message = new RMIMessage();
		message.setType(RMIMessage.Type.EXCEPTION);
		message.setexception(exception);
		sendMessage(message);
	}
	
	
}
