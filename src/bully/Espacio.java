package bully;

// Represent the critical section
public class Espacio {
  int used_by = 0;

  public Espacio(){
    System.out.println("Espacio inicializado");
  } 

  public void start_use(int id_agent){
    if(used_by==0){
      used_by=id_agent;
      System.out.println(id_agent+": Resource locked.");
    }else{
      System.err.println("Resource accesed by 2 agents at the same time. ("+ used_by +" and "+ id_agent +".");
    }
  }

  public void stop_use(int id_agent){
    if(used_by==id_agent){
      used_by=0;
      System.out.println(id_agent+": Resource free.");
    }else{
      System.err.println("Resource locked and free by 2 different agents. ("+ used_by +" and "+ id_agent +".");
    }
  }
}
