package comportamientos;

import jade.content.lang.sl.SLCodec;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.coti.tools.Esdia;
import com.coti.tools.OpMat;
import com.coti.tools.Rutas;

import agentes.Utils;

public class CyclicBehaviourTienda extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {

		// TODO Auto-generated method stub
		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		System.out.println("\n---------------NUEVA SOLICITUD DE VENTA---------------");
		try {
			String mensajeRecibido[] = ((String) msg.getContentObject()).split("/"); // [0] -> Producto [1] -> Cantidad
			int indProducto;
			int cantProducto = Integer.parseInt(mensajeRecibido[1]);
			String respuesta;
			System.out.println("El agente: " + msg.getSender().getName() + " quiere comprar " + cantProducto
					+ " unidades de " + mensajeRecibido[0]);

			indProducto = comprobarProducto(mensajeRecibido[0]);
			if (indProducto < 0) { // No existe el producto
				System.out.println("El producto (" + mensajeRecibido[0] + ") solicitado por "
						+ msg.getSender().getName() + " no existe en el inventario de mi tienda");

				// Le decimos al cliente que su producto no existe
				respuesta = "Lo sentimos pero el producto deseado (" + mensajeRecibido[0]
						+ ") no existe en nuestro inventario";
			} else {// El producto si existe, se procede a su venta
				int err = realizarVenta(indProducto, cantProducto, mensajeRecibido[0]);
				if (err == 0) {
					respuesta = "La tienda me ha vendido " + cantProducto + " unidades de " + mensajeRecibido[0];
				} else {
					respuesta = "La tienda no ha podido venderme " + cantProducto + " unidades de " + mensajeRecibido[0]
							+ " porque no tenia suficiente stock";
				}
			}

			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.addReceiver(msg.getSender());

			aclMessage.setOntology("ontologia");
			// el lenguaje que se define para el servicio
			aclMessage.setLanguage(new SLCodec().getName());
			// el mensaje se transmita en XML
			aclMessage.setEnvelope(new Envelope());
			// cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
			// aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML);
			aclMessage.setContentObject((Serializable) respuesta);
			this.myAgent.send(aclMessage);

		} catch (UnreadableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------FIN SOLICITUD DE VENTA----------------\n");
	}

	public int comprobarProducto(String producto) {
		Path archivoStockTienda = Rutas.pathToFileOnDesktop("stockTienda.csv");
		try {
			String tablaStockTienda[][] = OpMat.importFromDisk(archivoStockTienda.toFile(), "-");
			int tamanoTabla = tablaStockTienda.length;
			for (int i = 0; i < tamanoTabla; i++) {
				if (producto.toLowerCase().equals(tablaStockTienda[i][0].toLowerCase())) {
					return i;
				}
			}
			return -1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}

	}

	public int realizarVenta(int producto, int cantidad, String productoNombre) {
		int nMinUnidades = 4; // Si quedan menos unidades que estas la tienda procede a aumentar sus
								// provisiones de es producto
		Path archivoStockTienda = Rutas.pathToFileOnDesktop("stockTienda.csv");
		try {
			String tablaStockTienda[][] = OpMat.importFromDisk(archivoStockTienda.toFile(), "-");
			int stockProducto = Integer.parseInt(tablaStockTienda[producto][1]);
			if (stockProducto < cantidad) {
				// No quedan existencias del producto;
				System.out.println("No quedan existencias suficientes del producto solicitado, solo me quedan "
						+ stockProducto + " unidades");
				// El comercio solicita al proovedor producto
				int cantidadASolicitar = Esdia.readInt("Solicita unidades al proovedor: ");
				// Cambiar por mensajes
				String mensajeAlProveedor = producto + "/" + cantidadASolicitar + "/" + productoNombre;
				Utils.enviarMensaje(this.myAgent, "proveerStock", mensajeAlProveedor);
				ACLMessage msg = this.myAgent
						.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.MatchOntology("ontologia")));

				try {
					String respuesta = (String) msg.getContentObject();
					System.out.println(respuesta);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return -1;
			} else if (stockProducto - cantidad < nMinUnidades) {

				int cantFinal = stockProducto - cantidad;
				tablaStockTienda[producto][1] = String.valueOf(cantFinal);
				OpMat.exportToDisk(tablaStockTienda, archivoStockTienda.toFile(), "-");
				// No quedan existencias del producto;
				System.out.println(
						"Si quedan existencias suficientes del producto solicitado, pero menos de la cantidadMinima de stock");
				// El comercio solicita al proovedor producto
				int cantidadASolicitar = Esdia.readInt("Solicita unidades al proovedor: ");
				// Cambiar por mensajes
				String mensajeAlProveedor = producto + "/" + cantidadASolicitar + "/" + productoNombre;
				Utils.enviarMensaje(this.myAgent, "proveerStock", mensajeAlProveedor);
				ACLMessage msg = this.myAgent
						.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.MatchOntology("ontologia")));

				try {
					String respuesta = (String) msg.getContentObject();
					System.out.println(respuesta);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			} else {
				int cantFinal = stockProducto - cantidad;
				tablaStockTienda[producto][1] = String.valueOf(cantFinal);
				OpMat.exportToDisk(tablaStockTienda, archivoStockTienda.toFile(), "-");
				System.out.println("Si quedan existencias suficientes del producto solicitado, se realiza la venta");
				return 0;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}
	}

}
