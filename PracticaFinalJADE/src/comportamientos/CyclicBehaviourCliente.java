package comportamientos;

import java.util.ArrayList;
import java.util.Scanner;

import com.coti.tools.Esdia;

import agentes.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourCliente extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("\n---------------NUEVA SOLICITUD DE COMPRA---------------");
		String producto = Esdia.readString("Introduce el producto que quieres comprar: ");
		int cantidad = Esdia.readInt("Introduce la cantidad de " + producto + " que quieres comprar: ");
		String mensajeFormateado = producto + "/" + String.valueOf(cantidad);
		Utils.enviarMensaje(this.myAgent, "comprarProducto", mensajeFormateado);

		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));

		try {
			String respuesta = (String) msg.getContentObject();
			System.out.println(respuesta);
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------FIN SOLICITUD DE COMPRA----------------\n");
	}

}
