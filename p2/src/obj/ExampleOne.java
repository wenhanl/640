package obj;

import lombok.*;

/**
 * Example Object
 *
 * Created by wenhanl on 14-10-4.
 */


/**
 * You don't need to add getter, setter, constructor with no arg or constructor with all args
 * Annotations below will take care of all those
 * This feature is provided by Lombok.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleOne {
    private double num;
    private String str;

    public double pow(double pow){
        return Math.pow(num, pow);
    }

    public String concat(String newStr){
        return str.concat(newStr);
    }
}
