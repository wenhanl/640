import msg.MessageManager;
import msg.RMIMessage;
import net.Client;
import reg.RemoteObjectRef;
import remote640.Remote640Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
     * TODO
     * Use a console to control RMI object if wanted
     *
     */
    public void startConsole(){
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(System.in));
        String cmdInput = "";
        while(true) {
            System.out.print("--> ");
            try {
                cmdInput = buffInput.readLine();
                if (cmdInput == null) break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public RemoteObjectRef regLookup(String objName) throws Remote640Exception{

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

    public static void main(String[] args){

        final RMIClient client = new RMIClient("localhost", 15440, 15640);

        Thread console  = new Thread(new Runnable() {
            @Override
            public void run() {
                client.startConsole();
            }
        });

        //console.start();

        System.out.println("==========================================");

        // Test 1: existent object
        try {
            RemoteObjectRef ref = client.regLookup("ExampleOne");
            System.out.println("Remote Object Received: " + ref.getObjectName());
        } catch (Remote640Exception e) {
            e.printStackTrace();
        }

        System.out.println("==========================================");

        // Test 2: non-existent object
        // Excepted: A remote640 Exception with object not exist
        try {
            RemoteObjectRef ref = client.regLookup("Example");
            System.out.println("Remote Object Received: " + ref.getObjectName());
        } catch (Remote640Exception e) {
            e.printStackTrace();
        }

        System.out.println("==========================================");

        // Test 3: List
        ArrayList<String> list = client.regList();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            System.out.println((String) iterator.next());
        }


    }

}
