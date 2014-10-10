import remote.Remote640Stub;

public class ExampleOne_stub extends Remote640Stub {
	
    public Double pow(Double pow)  {
        Object returnValue = null;
		try {
			returnValue = invokeMethod("pow", new Object[]{pow});
		} catch (Exception e) {
			System.out.println("_stub invoke method error!");
			e.printStackTrace();
		}
        return (Double) returnValue;
        
    }

//    public String concat(String newStr){
//    	Object returnValue = invokeMethod("concat", new Object[]{pow});
//        return (double) returnValue;
//        return str.concat(newStr);
//    }
}
