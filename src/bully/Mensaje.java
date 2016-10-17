package bully;

/**
 *
 * @author joselito
 */
public enum Mensaje {
  OK, SOLICIT_LOCK, FREE_LOCK, ELECTION, COORDINATOR, PING, PONG, NO_RESPONSE, UNSPECTED_MESSAGE;
  Mensaje(){
	  System.out.println("aaa"+toString());
  }
}
