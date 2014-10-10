
/**
 * @author CGJ
 *
 */
import msg.MessageManager;
import msg.RMIMessage;
import remote.Remote640Exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RMIDispathcer implements Runnable {

	private MessageManager msgManager;
    private RMIMessage inMsg;
	
	public RMIDispathcer (MessageManager msgManager, RMIMessage inMsg){
		this.msgManager = msgManager;
        this.inMsg = inMsg;
	}
	
	public Object invokeMethod (RMIMessage inMsg) throws Remote640Exception {
		
		Object object = RMIRegistry.lookupObject(inMsg.getObjectName());
        Object[] args = inMsg.getArgs();
        String methodName = inMsg.getMethod();
        
        Method method;
        
        try{
        	if(args == null){
        		method = object.getClass().getMethod(methodName);
        		return method.invoke(object);
        	}
        	else {
        		Class<?>[] argTypes = new Class[args.length];
        		for (int i = 0; i < args.length; i++) {
        			argTypes[i] = args[i].getClass();
        		}
        		method = object.getClass().getMethod(methodName, argTypes);
        		return method.invoke(object, args);
        	}
        } catch (NoSuchMethodException e) {
        	throw new Remote640Exception("NoSuchMethodException: " + e.getMessage());
        } catch (IllegalAccessException e) {
        	throw new Remote640Exception("IllegalAccessException: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new Remote640Exception("IllegalArgumentException: " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new Remote640Exception("InvocationTargetException: " + e.getMessage());
		}
        
	}
	
	@Override
	public void run() {
		try {
			msgManager.sendReturnValue(invokeMethod(inMsg));
		} catch (Remote640Exception e){
	        msgManager.sendExpectionMessage(e);
		} 
	}
}
