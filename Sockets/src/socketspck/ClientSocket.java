package socketspck;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientSocket {
	Socket clientSocket;
	ObjectInputStream msgReceived;
	String msg = "";
	
	public ClientSocket() throws IOException {
		
		clientSocket = new Socket("localhost", 6000);
		
		System.out.println("Connected to localhost in port 6000");
		
		
	}
	
	private void Ack() throws IOException, ClassNotFoundException, InterruptedException{
		ObjectOutputStream msgSent = new ObjectOutputStream(clientSocket.getOutputStream());
		
		//envia sauda��es ao servidor socket
		msgSent.writeObject(clientSocket.getLocalAddress() + ": Hi server.");
		msgSent.flush();
		
		//abre canal de recebimento de stream
		msgReceived = new ObjectInputStream(clientSocket.getInputStream());
		
		int conclude = 0;
		//enquanto n�o receber mensagem closing connection de server socket, cliente continua ouvindo na conex�o
		while (conclude >= 0)
		{
			msg = msgReceived.readObject().toString();
			//verifica se existe alguma mensagem
			if (msg != ""){
				//Informa endere�o do servidor e mensagem enviada
				System.out.println(clientSocket.getRemoteSocketAddress() + ": " + msg);
				//Informa conhecimento de recebimento de mensagem de servidor
				msgSent.writeObject(clientSocket.getLocalAddress() + ": Ack");
				msgSent.flush();
			}
			Thread.sleep(1000);
			//verifica se alguma mensagem chegou se n�o for para finalizar conex�o
			
			if (msgReceived.available() > 0 && msgReceived.readInt() == 8000){
				conclude = -1;
			}
		}
		
		msgSent.close();
		clientSocket.close();
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException{
		ClientSocket client = new ClientSocket();
		client.Ack();
		
		System.out.println("Finished");
	}
}
