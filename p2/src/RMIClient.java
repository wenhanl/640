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
    private Client regClient;
    private Client serverClient;
    private MessageManager regManager;

    public RMIClient(String hostname, int regPort, int serverPort){
        regClient = new Client(hostname, regPort);
        serverClient = new Client(hostname, serverPort);
        regManager = new MessageManager(regClient);
    }

    /**
     * Lookup object on remote machine
     * @param objName
     * @return  Remote Object reference
     * @throws Remote640Exception
     */
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

    /**
     * List objects in remote registry
     * @return list of object names
     */
    public ArrayList<String> regList(){
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
 	    return list;
        
    }

    /**
     * Test case in main
     * @param args
     */
     public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Add hostname in the first parameter");
            return;
        }


        RMIClient client = new RMIClient(args[0], 15440, 15640);

        // Test 1: lookup existent object
        System.out.println("Start test1: Lookup");
        try {
         RemoteObjectRef ref = client.regLookup("ExampleOne");
         System.out.println("Remote Object Received: " + ref.getObjectName());
        } catch (Remote640Exception e) {
         e.printStackTrace();
        }

        System.out.println("Passed!");
        System.out.println("==========================================");

        // Test 2: Robustness test: lookup non-existent object
        // Excepted: A remote640 Exception with object not exist
        System.out.println("Start test2: Failed Lookup (Remote640Exception)");
        try {
         RemoteObjectRef ref = client.regLookup("Example");
         System.out.println("Remote Object Received: " + ref.getObjectName());
        } catch (Remote640Exception e) {
         System.out.println("Exception: " + e.getMessage());
        }

        System.out.println("Passed");
        System.out.println("==========================================");

        // Test 3: List Objects
         System.out.println("Start test3: List");
        ArrayList<String> list = client.regList();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
         System.out.println((String) iterator.next());
        }
        System.out.println("Passed!");
        System.out.println("==========================================");

        // Test 4: Remote Method Call
        // Expected: test1 output 2 ^ 5, test2 output reverse string
         System.out.println("Start test4: RPC");
        Double test1 = 5.0;
        String test2 = "Distributed System: RMI";

        RemoteObjectRef ref1 = null, ref2 = null;
        try {
             ref1 = client.regLookup("ExampleOne");
             ref2 = client.regLookup("ExampleTwo");
        } catch (Remote640Exception e) {
             e.printStackTrace();
             return;
        }

        ExampleOne_stub Stub1 = null;
        ExampleTwo_stub Stub2 = null;

        Stub1 = (ExampleOne_stub) ref1.localise();
        Stub1.setClient(client.serverClient);
        //result of test1
        System.out.println(Stub1.pow(test1));

        Stub2 = (ExampleTwo_stub) ref2.localise();
        Stub2.setClient(client.serverClient);
        //result of test2
        System.out.println(Stub2.reverse(test2));

        System.out.println("Passed!");


	}

}
