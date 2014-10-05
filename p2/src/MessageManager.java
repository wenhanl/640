/**
 * @author CGJ
 *
 */
import net.Client;
import net.NetObject;
import net.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;


public class MessageManager {
    // Type means this manager is created in server side or client side
    public static enum Type { SERVER, CLIENT }
    private Client client;
    private Server server;
    private SocketChannel socket;
    private Type type;
    
    public MessageManager(Client client) {
        this.client = client;
        this.type = Type.CLIENT;
    }

    public MessageManager(Server server, SocketChannel socket){
        this.server = server;
        this.socket = socket;
        this.type = Type.SERVER;
    }


    public void sendMessage(RMIMessage message) throws IOException {
        byte[] msg = RMIMessage.serialize(message);

        if(type == Type.SERVER){
            server.write(socket, msg);
        } else if (type == Type.CLIENT) {
            client.write(msg);
        }
    }


    /**
     * receiveMessage method abandoned in order to support concurrency
     *
     */

    public void sendReturnValue(Object retvalue) throws IOException{
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.RETURN);
        message.setRetValue(retvalue);
        sendMessage(message);
    }
    public void sendInvokeMessage(String objectName, String method, Object[] args) throws IOException{
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.INVOKE);
        message.setObjectName(objectName);
        message.setMethod(method);
        message.setArgs(args);
        sendMessage(message);
    }
    public void sendLookupMessage(String objectName) throws IOException{
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.LOOKUP);
        message.setObjectName(objectName);
        sendMessage(message);
    }
    public void sendListMessage() throws IOException{
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.LIST);
        sendMessage(message);
    }
    public void sendExpectionMessage(Exception exception) throws IOException{
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.EXCEPTION);
        message.setException(exception);
        sendMessage(message);
    }
	
	
}
