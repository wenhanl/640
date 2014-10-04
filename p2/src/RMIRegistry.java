/**
 * 
 */

/**
 * @author CGJ
 *
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 */

/**
 * @author CGJ
 *
 */
public class RMIRegistry implements Runnable {
	
    private static Map<String, RemoteObjectRef> nameToRef;
    private static Map<String, Object> nameToObj;
    private ServerSocket regSocket;
    
    public RMIRegistry(int regPort) throws IOException {
    	nameToRef = Collections.synchronizedMap(new HashMap<String, RemoteObjectRef>());
    	nameToObj = Collections.synchronizedMap(new HashMap<String, Object>());
    	regSocket = new ServerSocket(regPort);
    }
    
	void bind(String objectName, Object object) throws UnknownHostException{
		RemoteObjectRef ref = new RemoteObjectRef(RMIServer.getHostname(), RMIServer.getserverPort(), objectName, object.getClass().getName());
		nameToObj.put(objectName, object);
		nameToRef.put(objectName, ref);
	}
	
	void rebind(String objectName, Object object) throws UnknownHostException{
		bind(objectName,object);
	}
	
	void unbind(String objectName, Object object){
		if(nameToObj.containsKey(objectName)){
			nameToObj.remove(objectName);
			nameToRef.remove(objectName);
		}
	}
	
	public RemoteObjectRef lookupRef (String objectname) throws Remote640Exception{
		if (nameToRef.containsKey(objectname)) {
            return nameToRef.get(objectname);
        } 
		else {
            throw new Remote640Exception("ObjectName" + objectname + "not exist in registry!");
        }
	}
	
	public static Object lookupObject (String objectname){
		return nameToObj.get(objectname);
	}
	
    public String[] listObjectName() {
    	Object[] arr = nameToRef.keySet().toArray();
    	String[] keys = new String[nameToRef.size()];
    	for(int i=0;i<arr.length;i++)
    		keys[i] = (String)arr[i];
    	return keys;
    }
	
	public void run() {
		Socket socket = null;
		try {
			socket = regSocket.accept();
		} catch (IOException e) {
			System.err.println("Registry received socket from client error!");
			e.printStackTrace();
		}
		MessageManager msgManger = new MessageManager(socket);
		RMIMessage inMsg = null;
		try {
			inMsg = msgManger.receiveMessage();
		} catch (ClassNotFoundException e) {
			System.err.println("Registry receive messge error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Registry receive messge error: " + e.getMessage());
			e.printStackTrace();
		}
		
		if(inMsg.getType() == RMIMessage.Type.LOOKUP){
			RemoteObjectRef ref = null;
			try {
				ref = lookupRef(inMsg.getobjectName());
				msgManger.Sendreturnvalue(ref);
			} catch (Remote640Exception e) {
				System.err.println("Registry LOOKUP reference error: " + e.getMessage());				
			} catch (IOException e) {
				System.err.println("Registry send messge error: " + e.getMessage());
			}
		} 
		else if (inMsg.getType() == RMIMessage.Type.LIST) {
			String[] resStrings = listObjectName();
			try {
				msgManger.Sendreturnvalue(resStrings);
			} catch (IOException e) {
				System.err.println("Registry send messge error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}