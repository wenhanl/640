package msg;
/**
 * @author CGJ
 *
 */
import net.Client;
import net.NetObject;
import net.Server;

import java.io.IOException;
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


    public void sendMessage(RMIMessage message){

        byte[] msg = new byte[0];
        try {
            msg = RMIMessage.serialize(message);
        } catch (IOException e) {
            System.err.println("Fail to serialize: " + e.getMessage());
        }

        if(type == Type.SERVER){
            server.write(socket, msg);
        } else if (type == Type.CLIENT) {
            client.write(msg);
        }
    }

    /**
     * Careful using this method. Only works for DATA received
     * New connection or closed connection packet will cause an error
     * @return RMIMessage received
     */
    public RMIMessage receiveOneMessage(){
        NetObject ret = null;
        if(type == Type.SERVER){
            ret = server.listen();
        } else if (type == Type.CLIENT) {
            ret = client.listen();
        }
        // Ignore not DATA network packet
        if(ret.type != NetObject.NetType.DATA){
            System.out.println("Not receiving data");
            return null;
        }

        RMIMessage msg = null;
        try {
            msg = (RMIMessage) RMIMessage.deserialize(ret.data);
        } catch (IOException e) {
            System.err.println("Fail to deserialize: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Fail find class: " + e.getMessage());
        }

        return msg;
    }

    /**
     * receiveMessage method abandoned in order to support concurrency
     *
     */

    public void sendReturnValue(Object retvalue){
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.RETURN);
        message.setRetValue(retvalue);
        sendMessage(message);
    }
    public void sendInvokeMessage(String objectName, String method, Object[] args){
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.INVOKE);
        message.setObjectName(objectName);
        message.setMethod(method);
        message.setArgs(args);
        sendMessage(message);
    }
    public void sendLookupMessage(String objectName){
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.LOOKUP);
        message.setObjectName(objectName);
        sendMessage(message);
    }
    public void sendListMessage(){
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.LIST);
        sendMessage(message);
    }
    public void sendExpectionMessage(Exception exception){
        RMIMessage message = new RMIMessage();
        message.setType(RMIMessage.Type.EXCEPTION);
        message.setException(exception);
        sendMessage(message);
    }
	
	
}
