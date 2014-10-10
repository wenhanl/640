	
import msg.MessageManager;
import msg.RMIMessage;
import net.NetObject;
import net.Server;
import remote.Remote640Exception;
import remote.RemoteObjectRef;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author CGJ
 *
 */
public class RMIRegistry implements Runnable {
	private String hostname;
    private int port;
    private static Map<String, RemoteObjectRef> nameToRef;
    private static Map<String, Object> nameToObj;
    private Server server;
    
    public final static int serverPort = 15640;
    
    public RMIRegistry(int regPort) throws IOException {
    	nameToRef = Collections.synchronizedMap(new HashMap<String, RemoteObjectRef>());
    	nameToObj = Collections.synchronizedMap(new HashMap<String, Object>());
        hostname = InetAddress.getLocalHost().getCanonicalHostName();
        port = regPort;
        server = new Server(regPort);
    }
    
	public void bind(String objectName, Object object) throws UnknownHostException{
		RemoteObjectRef ref = new RemoteObjectRef(hostname, serverPort, objectName, object.getClass().getName());
		nameToObj.put(objectName, object);
		nameToRef.put(objectName, ref);
	}
	
	public void rebind(String objectName, Object object) throws UnknownHostException{
		bind(objectName,object);
	}

    /**
     * Lookup from object name
     * @param objectname
     * @return
     * @throws Remote640Exception
     */
	public RemoteObjectRef lookupRef (String objectname) throws Remote640Exception {
		if (nameToRef.containsKey(objectname)) {
            return nameToRef.get(objectname);
        } else {
        	System.out.println("not fund obecjectname!");
            throw new Remote640Exception("ObjectName " + objectname + " not exist in registry!");
        }
	}

    /**
     * Get opject from name
     * @param objectname String
     * @return
     */
	public static Object lookupObject (String objectname){
		return nameToObj.get(objectname);
	}

    /**
     * List objects
     * @return
     */
    public ArrayList<String> listObjectName() {
    	Iterator iterator = nameToRef.keySet().iterator();
        ArrayList<String> arrayList = new ArrayList<>();
        while(iterator.hasNext()){
            arrayList.add((String) iterator.next());
        }
        return arrayList;
    }

    /**
     * Run a registry server
     */
	public void run() {

        while(true){
            NetObject obj = server.listen();

            switch (obj.type){
                case CONNECTION:
                    System.out.println("Connection Established: On RMIRegistry");
                    break;
                case DATA:
                    MessageManager msgManger = new MessageManager(server, obj.sock);
                    RMIMessage inMsg = null;
                    try {
                        inMsg = (RMIMessage) RMIMessage.deserialize(obj.data);
                        System.out.println("RMI Message recieved: " + inMsg.toString());
                    } catch (IOException e) {
                        System.err.println("RMIMessage receive failed: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.err.println("RMIMessage deserialize failed: " + e.getMessage());
                    }

                    if(inMsg.getType() == RMIMessage.Type.LOOKUP){
                        RemoteObjectRef ref = null;
                        try {
                            ref = lookupRef(inMsg.getObjectName());
                            msgManger.sendReturnValue(ref);
                        } catch (Remote640Exception e) {
                            System.err.println("Registry LOOKUP reference error: " + e.getMessage());
                            msgManger.sendExpectionMessage(e);
                        }
                    }
                    else if (inMsg.getType() == RMIMessage.Type.LIST) {
                        ArrayList<String> resStrings = listObjectName();
                        msgManger.sendReturnValue(resStrings);
                    }
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