package bully;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.util.SimUtilities;
import java.util.ArrayList;

/**
 *
 * @author joselito
 */
public class Modelo extends SimModelImpl {
  private Schedule schedule;
  public ArrayList agentList;
  public static final Espacio SPACE = new Espacio();
  private static int IDNumber = 0;                      // Actual counter ID for the next agent 
                    
    
  // Default Values
  private static final int NUMAGENTS = 10;
  private static final int AGENT_MIN_LIFESPAN = 2;
  private static final int AGENT_MAX_LIFESPAN = 10;

  private int numAgents = NUMAGENTS;
  private int agentMinLifespan = AGENT_MIN_LIFESPAN;
  private int agentMaxLifespan = AGENT_MAX_LIFESPAN;

  public String getName(){
    return "Bully";
  }

  public void setup(){
    agentList = new ArrayList();
    schedule = new Schedule(1);
  }     

  public void begin(){
    buildModel();
    buildSchedule();
    buildDisplay();
  }

  public void buildModel(){
    for(int i = 0; i < numAgents; i++){
      addNewAgent();
    }  
  }

  public void buildSchedule(){
    class Step extends BasicAction {
      public void execute() {
        SimUtilities.shuffle(agentList);
        for(int i =0; i < agentList.size(); i++){
          Agente cda = (Agente)agentList.get(i);
          cda.step();
        }

        int deadAgents = countDeadAgents();
        for(int i =0; i < deadAgents; i++){
          addNewAgent();
        }

      }
    }

    schedule.scheduleActionBeginning(0, new Step());
  }

  public void buildDisplay(){

  }

  public int getNumAgents(){
    return numAgents;
  }

  public void setNumAgents(int na){
    numAgents = na;
  }

  public String[] getInitParam(){
    String[] initParams = { "NumAgents", "AgentMinLifespan", "AgentMaxLifespan"};
    return initParams;
  }

  public Schedule getSchedule(){ 
    return schedule;
  }

  public int getAgentMaxLifespan() {
    return agentMaxLifespan;
  }

  public int getAgentMinLifespan() {
    return agentMinLifespan;
  }

  public void setAgentMaxLifespan(int i) {
    agentMaxLifespan = i;
  }

  public void setAgentMinLifespan(int i) {
    agentMinLifespan = i;
  }


  private void addNewAgent(){
    IDNumber++;
	boolean valid=false;
	int id=1;
	while(!freeId(id)){
		id = (int)((Math.random() * (NUMAGENTS*10)) + 1);
	}
    Agente a = new Agente(agentMinLifespan, agentMaxLifespan, SPACE, id, agentList);
    agentList.add(a);
  }

  private int getActualCoordinatorId(){
	  int result;
	  if(agentList.isEmpty()){
		  result=1;
	  }else{
		 result=((Agente)agentList.get(0)).ID_coordinator;
	  }
	  return result;
  }

private int countDeadAgents(){
  int count = 0;
  for(int i = (agentList.size() - 1); i >= 0 ; i--){
    Agente cda = (Agente)agentList.get(i);
    if(cda.getStepsToLive() == 0){
      count++;
    }
    if(cda.getStepsToLive() == -30){
      agentList.remove(i);
    }
  }
  return count;
}

public static void main(String[] args) {
  SimInit init = new SimInit();
  Modelo model = new Modelo();
  init.loadModel(model, "", false);
}
  private boolean freeId(int id){
	  for(int x=0; x<agentList.size();x++){
		  if(((Agente)agentList.get(x)).ID == id){
			  return false;
		  }
	  }  
	  return true;
  }
}
