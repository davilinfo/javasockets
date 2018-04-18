package socketspck;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import javax.swing.plaf.SliderUI;

import java.lang.*;

public class ServerSocket {
	java.net.ServerSocket serverSocket;
	Socket connection;
	ObjectInputStream msgReceived;
	String msg = "";
	
	public ServerSocket() throws IOException, ClassNotFoundException{
		System.out.println("Iniciando socket servidor");
		serverSocket = new java.net.ServerSocket(6000, 3);
		
		connection = serverSocket.accept();
		
		//Informa endereço de máquina que se conectou ao socket servidor
		System.out.println("Remote address: " + connection.getRemoteSocketAddress());
	}
	
	private void Ack() throws IOException, ClassNotFoundException, InterruptedException{
		ObjectOutputStream msgSent = new ObjectOutputStream(connection.getOutputStream());
		
		//abre canal de recebimento de stream
		msgReceived = new ObjectInputStream(connection.getInputStream());
		
		msg = (String)msgReceived.readObject();
		//verifica se existe alguma mensagem
		if (msg != ""){
			//Informa endereço do cliente e mensagem enviada
			System.out.println(connection.getRemoteSocketAddress() + ": " + msg);
		}
		
		//envia mensagem para cliente informando que conexão será encerrada
		msgSent.writeObject("Hi client, closing connection");
		msgSent.writeInt(8000);
		msgSent.flush();

		Thread.sleep(5000);
		
		System.out.println("Closing connection");
		
		msgSent.close();
		
		serverSocket.close();
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException
	{
		socketspck.ServerSocket ss = new socketspck.ServerSocket();
		
		ss.Ack();
		
		System.out.println("End socket application");
	}
}
