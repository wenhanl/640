import msg.MessageManager;
import msg.RMIMessage;
import net.Client;
import remote.Remote640Exception;
import remote.RemoteObjectRef;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * @author CGJ
 *
 */
public class RMIClient {
    private static Client regClient;
    private static Client serverClient;
    private static MessageManager regManager;

    public RMIClient(String hostname, int regPort, int serverPort){
        regClient = new Client(hostname, regPort);
        serverClient = new Client(hostname, serverPort);
        regManager = new MessageManager(regClient);
    }

    public RemoteObjectRef regLookup(String objName) throws Remote640Exception {

        regManager.sendLookupMessage(objName);

        RMIMessage inMsg = regManager.receiveOneMessage();
        System.out.println("RMI Message recieved: " + inMsg.toString());
        RMIMessage.Type type = inMsg.getType();
        RemoteObjectRef ref = null;
        if(type == RMIMessage.Type.EXCEPTION){
            throw (Remote640Exception) inMsg.getException();
        } else if (type == RMIMessage.Type.RETURN){
            ref = (RemoteObjectRef) inMsg.getRetValue();
        } else {
            System.err.println("Unexcepted packet type");
        }

        return ref;
    }

    public void regList(){
        regManager.sendListMessage();

        RMIMessage inMsg = regManager.receiveOneMessage();
        System.out.println("RMI Message recieved: " + inMsg.toString());
        RMIMessage.Type type = inMsg.getType();
        ArrayList<String> list = null;
        if (type == RMIMessage.Type.RETURN){
            list = (ArrayList<String>) inMsg.getRetValue();
        } else {
            System.err.println("Unexcepted packet type");
        }
 	    Iterator iterator = list.iterator();
        while(iterator.hasNext())
            System.out.println((String) iterator.next());
	
	System.out.println();
        
    }

     public static void main(String[] args) {
   	 if(args.length != 1){
   	     System.out.println("Add hostname in the first parameter");
       	     return;
   	 }
		
		RMIClient client = new RMIClient(args[0], 15440, 15640);
		client.regList();
		RemoteObjectRef ref = null,ref2 = null;
		try {
			ref = client.regLookup("ExampleOne");
			ref2 = client.regLookup("ExampleTwo");
		} catch (Remote640Exception e) {
			System.out.println("object not found in registry!");
			e.printStackTrace();
			return;
		}
		//This is the test cases. Feel free to modify them 
		Double test1 = 5.0;
		String test2 = "Distributed System: RMI";

		ExampleOne_stub Stub1 = null;
		ExampleTwo_stub Stub2 = null;

		Stub1 = (ExampleOne_stub) ref.localise();
		Stub1.setClient(serverClient);
		//result of test1
		System.out.println(Stub1.pow(test1));

		Stub2 = (ExampleTwo_stub) ref2.localise();
		Stub2.setClient(serverClient);
		//result of test2
		System.out.println(Stub2.reverse(test2));


	}

}
