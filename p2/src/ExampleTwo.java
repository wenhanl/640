
/**
 * Created by wenhanl on 14-10-4.
 */
public class ExampleTwo {
	
	public String reverse (String s){
		char []tmp = new char[s.length()];
		int j=0;
		for(int i=s.length()-1;i>=0;i--)
			tmp[j++]=s.charAt(i);
		return String.valueOf(tmp);
	}	
}
