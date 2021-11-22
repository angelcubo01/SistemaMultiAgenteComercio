package comportamientos;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import com.coti.tools.OpMat;
import com.coti.tools.Rutas;

import agentes.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourProveedor extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {

		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		System.out.println("\n---------------NUEVA SOLICITUD DE STOCK---------------");
		try {
			String mensajeFormateado[] = ((String) msg.getContentObject()).split("/");
			int indProducto = Integer.parseInt(mensajeFormateado[0]);
			int cantidad = Integer.parseInt(mensajeFormateado[1]);
			String nombreProducto = mensajeFormateado[2];
			System.out.println("La tienda (" + msg.getSender().getName() + ") me ha solicitado " + cantidad
					+ " unidades de " + nombreProducto);
			solicitaStock(indProducto, cantidad);
			String respuesta = "El proveedor me ha mandado " + cantidad + " unidades de " + nombreProducto
					+ " que le he solicitado";
			Utils.enviarMensaje(this.myAgent, "comprarProducto", respuesta);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------FIN SOLICITUD DE STOCK----------------\n");

	}

	static void solicitaStock(int producto, int cantidad) {
		Path archivoStockTienda = Rutas.pathToFileOnDesktop("stockTienda.csv");
		try {
			String tablaStockTienda[][] = OpMat.importFromDisk(archivoStockTienda.toFile(), "-");
			int stockProducto = Integer.parseInt(tablaStockTienda[producto][1]) + cantidad;
			tablaStockTienda[producto][1] = String.valueOf(stockProducto);
			OpMat.exportToDisk(tablaStockTienda, archivoStockTienda.toFile(), "-");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

}
