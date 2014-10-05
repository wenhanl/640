/**
 * 
 */

/**
 * @author CGJ
 *
 */

public class Remote640Stub implements Remote640 {
	private RemoteObjectRef ref;
	
	public void setRemoteRef(RemoteObjectRef ref) {
        this.ref = ref;
    }
	
	protected Object invokeMethod (String method, Object[] args) throws Exception {
		MessageManager messageManager = new MessageManager(ref.getHostname(), ref.getPort());
		messageManager.sendInvokeMessage(ref.getObjectName(), method, args);
		
		Object retvalue = null;
		RMIMessage resMessage = null;
		try {
			resMessage = messageManager.receiveMessage();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(resMessage.getType() == RMIMessage.Type.RETURN)
			retvalue = resMessage.getretvalue();
		else if(resMessage.getType() == RMIMessage.Type.EXCEPTION)
			throw resMessage.getexception();
		else 
			throw new Remote640Exception("RMIMessage Return Type Error!");
		
		
			
		return retvalue;
	}

	
}
