package agentes;


import comportamientos.CyclicBehaviourCliente;
import jade.core.Agent;


public class AgentCliente extends Agent {

	private static final long serialVersionUID = 1L;

	public void setup() 
	{
	
		addBehaviour(new CyclicBehaviourCliente());
		
	}
}
