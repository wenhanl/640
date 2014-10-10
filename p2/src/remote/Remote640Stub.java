package remote;

import msg.*;
import net.Client;
import remote.Remote640;
import remote.Remote640Exception;

/**
 * @author CGJ
 *
 */

public class Remote640Stub implements Remote640 {
	private RemoteObjectRef ref;
    private Client client;
	
	public void setRemoteRef(RemoteObjectRef ref) {
        this.ref = ref;
    }
	public void setClient(Client client){
		this.client = client;
	}

    /**
     * Invoke a method remotely
     * @param method String - method name
     * @param args Object[] - args array
     * @return
     * @throws Exception
     */
	protected Object invokeMethod (String method, Object[] args) throws Exception {
		MessageManager messageManager = new MessageManager(client);

		messageManager.sendInvokeMessage(ref.getObjectName(), method, args);
		
		Object retvalue = null;
		RMIMessage resMessage = messageManager.receiveOneMessage();
		
		
		
		if(resMessage.getType() == RMIMessage.Type.RETURN)
			retvalue = resMessage.getRetValue();
		else if(resMessage.getType() == RMIMessage.Type.EXCEPTION)
			throw resMessage.getException();
		else 
			throw new Remote640Exception("RMIMessage Return Type Error!");

		return retvalue;
	}

	
}
