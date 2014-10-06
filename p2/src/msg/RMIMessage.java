package msg;

import lombok.*;
import java.io.*;

/**
 * RMI Message Object
 *
 * @author CGJ
 */


/**
 * You don't need to add getter, setter, constructor with no arg.
 * Annotations below will take care of all those
 * This feature is provided by Lombok.
 */

@Data
@NoArgsConstructor
public class RMIMessage implements Serializable{
	
	public static enum Type { LOOKUP, LIST, INVOKE, RETURN, EXCEPTION }
	private Type type;
	
	private String objectName;
	private String method;
	private Object[] args;
	private Exception exception;
	private Object retValue;

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
