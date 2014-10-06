package remote640; /**
 * 
 */

/**
 * @author CGJ
 *
 */

public class Remote640Exception extends Exception {
	private static final long serialVersionUID = 23L;
	private String ExpMessage;

    public Remote640Exception (String expmessage) {
    	this.ExpMessage = expmessage;
    }
    @Override
    public String getMessage() {
        return "Remote640Exception: " + ExpMessage;
    }

}
