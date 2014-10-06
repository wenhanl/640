import java.util.ArrayList;

/**
 * @author CGJ
 *
 */
public class RMIClient {
    private Client regClient;
    private static Client serverClient;
    private MessageManager regManager;

    public RMIClient(String hostname, int regPort, int serverPort){
    	System.out.println("0");
        regClient = new Client(hostname, regPort);
        System.out.println("11");
        serverClient = new Client(hostname, serverPort);
        System.out.println("1");
        regManager = new MessageManager(regClient);
        System.out.println("2");
    }

    /**
     * TODO
     * Use a console to control RMI object if wanted
     *
     */

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

	public static void main(String[] args) {
		RMIClient client = new RMIClient("localhost", 15440, 15640);
		RemoteObjectRef ref = null,ref2 = null;
		try {
			ref = client.regLookup("ExampleOne");
			ref2 = client.regLookup("ExampleTwo");
		} catch (Remote640Exception e) {
			System.out.println("object not found in registry!");
			e.printStackTrace();
			return;
		}
		Double t1 = 5.0;
		String t2 = "Distributed System: RMI";
		
		ExampleOne_stub Stub1 = null;
		ExampleTwo_stub Stub2 = null;
		
		Stub1 = (ExampleOne_stub) ref.localise();
		Stub1.setClient(serverClient);
		System.out.println(Stub1.pow(t1));
		
		Stub2 = (ExampleTwo_stub) ref2.localise();
		Stub2.setClient(serverClient);
		System.out.println(Stub2.reverse(t2));

		
	}

}
