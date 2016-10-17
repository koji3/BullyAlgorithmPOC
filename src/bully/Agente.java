package bully;

import java.util.ArrayList;

public class Agente {
  private int stepsToLive;                              // When 0, the agent die                    
  private final Espacio critical_section;               // Critical section
  int lockDuration;                                     //How much steps will stay in the critical section
  boolean im_coordinator=false;
  public int ID;
  public int ID_coordinator=-1;
  ArrayList agentList;
  
  public Agente(int minLifespan, int maxLifespan, Espacio crit_section, int id, ArrayList agentList){
    stepsToLive = (int)((Math.random() * (maxLifespan - minLifespan)) + minLifespan);

    critical_section = crit_section;
    recalculate_duration();
    ID=id;
	this.agentList=agentList;
    report();
  }
  
  private Mensaje send(Mensaje m, int id_receiver){
	  return getAgentById(id_receiver).receive(m, ID);
  }
  
  public Mensaje receive(Mensaje m, int id_sender){
	if(stillAlive()){
		switch(m){
			case PING: 
				if(im_coordinator){
					System.out.print(ID+" ");
					return Mensaje.PONG; 
				}else{
					return Mensaje.UNSPECTED_MESSAGE;
				}
			case ELECTION:
				if(id_sender<=ID){
					startElection();
					return Mensaje.OK;
				}else{
					return Mensaje.UNSPECTED_MESSAGE;
				}
			case COORDINATOR:
				if(id_sender>=ID){
					ID_coordinator=id_sender;
					return Mensaje.OK;
				}else{
					return Mensaje.UNSPECTED_MESSAGE;
				}
			default:
				return Mensaje.UNSPECTED_MESSAGE;
		}
	}else{
		return Mensaje.NO_RESPONSE;
	}
  }
  
  public int getID(){
    return ID;
  }
  
  private boolean stillAlive(){
	return (stepsToLive>0);
  }

  public int getStepsToLive(){
    return stepsToLive;
  }
  
  private void recalculate_duration(){
    lockDuration = (int)((Math.random() * (3)) + 1);
  }

  public void report(){
    System.out.println(getID() + " has " + getStepsToLive() + " steps to live.");
	System.out.println("Others in the swarm: ");
	for(int x=0; x<agentList.size();x++){
		System.out.print(((Agente)agentList.get(x)).ID+" ");
	}
	System.out.println(" ");

  }
  
  public void step(){
	  if(im_coordinator){
		  report();
	  }
    if(stepsToLive>0){
	  if(ID_coordinator>0){
		Mensaje response = send(Mensaje.PING,ID_coordinator);
		switch(response){
			case PONG: break;
			case NO_RESPONSE: startElection(); break;
			case UNSPECTED_MESSAGE:	System.err.println("Unspected ping from "+ID+" to "+ID_coordinator);
			default:					System.err.println("Unspected ping response from "+ID_coordinator+"("+response.toString()+")");
		}
	  }else{
		  startElection();
	  }
    }		
  stepsToLive--;
  }
  
  private Agente getAgentById(int id){
	  for(int x=0; x<agentList.size();x++){
		  if(((Agente)agentList.get(x)).ID == id){
			  return (Agente)agentList.get(x);
		  }
	  }  
	  return this;
  }
  
  private void advertNewCoordinator(){
	  im_coordinator=true;
	  for(int x=0; x<agentList.size();x++){		
		  if(((Agente)agentList.get(x)).receive(Mensaje.COORDINATOR,ID)==Mensaje.UNSPECTED_MESSAGE){
			  System.err.println("Unspected coordinator declaration from "+ID+" existing "+((Agente)agentList.get(x)).ID);
		  }// else its ok
	  }
  }

	private void startElection() {		//Envia mensajes a los de id mayor que el mio
		boolean imTheNext=true;
		for(int x=0; x<agentList.size();x++){												// Itero los agentes
		  if(((Agente)agentList.get(x)).ID > ID){											// Si su id es mayor que el mio le envio un mensaje de eleccion
			  if(((Agente)agentList.get(x)).receive(Mensaje.ELECTION,ID)==Mensaje.OK){		// Si alguno responde, yo no soy el coordinador
				  imTheNext=false;
			  }
		  }
		}
		if(imTheNext){
			advertNewCoordinator();
		}
	}
}
