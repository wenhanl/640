/**
 * 
 */

/**
 * @author CGJ
 *
 */


public class RMIMessage {
	
	public static enum Type { LOOKUP, LIST, INVOKE, RETURN, EXCEPTION }
	private Type type;
	
	private String objectName;
	private String method;
	private Object[] args;
	private Exception exception;
	private Object retvalue;
	
	public RMIMessage(){}
	
	Type getType(){
		return type;
	}
	String getobjectName(){
		return objectName;
	}
	String getmethod(){
		return method;
	}
	Object[] getargs(){
		return args;
	}
	Exception getexception(){
		return exception;
	}
	Object getretvalue(){
		return retvalue;
	}
	
	void setType(Type type){
		this.type = type;
	}
	void setobjectName(String objectName){
		this.objectName = objectName;
	}
	void setmethod(String method){
		this.method = method;
	}
	void setargs(Object[] args){
		this.args = args;
	}
	void setexception(Exception exception){
		this.exception = exception;
	}
	void setretvalue(Object retvalue){
		this.retvalue = retvalue;
	}
	
//	//return value message
//	public RMIMessage(Type type, Object retvalue) {
//		this.type = type;
//        this.retvalue = retvalue;
//    }
//	//lookup message 
//	public RMIMessage(Type type, String ObjectName) {
//		this.type = type;
//        this.ObjectName = ObjectName;
//    }
//	//invoke method
//	public RMIMessage(Type type, String ObjectName, String method, Object[] args) {
//		this.type = type;
//        this.ObjectName = ObjectName;
//        this.method = method;
//        this.args = args;
//    }
//	//list remote object
//	public RMIMessage(Type type) {
//		this.type = type;
//	}
//	//exception message
//	public RMIMessage(Type type,Exception exception) {
//		this.type = type;
//		this.exception = exception;
//	}

}
