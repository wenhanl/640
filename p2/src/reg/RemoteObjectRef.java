package reg;

/**
 * @author CGJ
 *
 */
import java.io.Serializable;
import lombok.*;
import remote640.Remote640Stub;

@Getter
public class RemoteObjectRef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostname;
	private int port;
    private String objectName;
    private String className;
    
    public RemoteObjectRef(String hostname, int port, String objectName, String className) {
        this.hostname = hostname;
        this.port = port;
        this.objectName = objectName;
        this.className = className;
    }
    
    public Object localise() {
    	Object stub = null;
		try {
			stub = Class.forName(className + "_stub").newInstance();
			((Remote640Stub)stub).setRemoteRef(this);
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stub;
		
    }
}