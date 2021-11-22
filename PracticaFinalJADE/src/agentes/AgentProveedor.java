package agentes;

import comportamientos.CyclicBehaviourProveedor;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentProveedor extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void setup() 
	{
		//Crear servicios proporcionados por el agente y registrarlos en la plataforma
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("proveerStock");
		//Establezco el tipo del servicio para poder localizarlo cuando haga una busqueda
		sd.setType("proveerStock");
		// Agents that want to use this service need to "know" the ontologia
		sd.addOntologies("ontologia");
		// Agents that want to use this service need to "speak" the PIPA-SL language
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		try
		{
			//registro los servicios
			DFService.register(this, dfd);
		}
		catch(FIPAException e)
		{
			System.err.println("Agente " + getLocalName() + ": "+e.getMessage());
		}
		
		addBehaviour(new CyclicBehaviourProveedor());
	}

}