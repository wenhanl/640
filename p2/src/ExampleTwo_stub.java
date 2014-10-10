import remote.Remote640Stub;

public class ExampleTwo_stub extends Remote640Stub {
	
	public String reverse (String s) {
        Object returnValue = null;
		try {
			returnValue = invokeMethod("reverse", new Object[]{s});
		} catch (Exception e) {
			System.out.println("_stub invoke method error!");
			e.printStackTrace();
		}
        return (String) returnValue;
        
    }

//    public String concat(String newStr){
//    	Object returnValue = invokeMethod("concat", new Object[]{pow});
//        return (double) returnValue;
//        return str.concat(newStr);
//    }
}
