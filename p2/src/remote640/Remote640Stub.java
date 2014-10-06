package remote640; /**
 * 
 */

import msg.MessageManager;
import msg.RMIMessage;
import net.Client;
import reg.RemoteObjectRef;
import remote640.Remote640;

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
