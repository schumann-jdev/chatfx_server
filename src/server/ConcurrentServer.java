package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class ConcurrentServer
  extends Thread
{
  private static HashMap<String, ClientSocket> clientSockets = new HashMap();
  
  private static Main main;
  
  public ConcurrentServer(Main main) {
    ConcurrentServer.main = main;
    main.addData("Serveur en marche");
    System.out.println("*************************************");
    System.out.println("Serveur courant côté console");
    System.out.println("*************************************");
  }
 
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(1016);
      
      while (true) {
        Socket socket = serverSocket.accept();
        ClientSocket clientSocket = new ClientSocket(socket, 
            new ObjectOutputStream(socket.getOutputStream()), 
            new ObjectInputStream(socket.getInputStream()));
        
        Thread newThread = new ThreadHandler(clientSocket);
        newThread.start();
      }
    
    } catch (IOException ioe) {
      
      ioe.printStackTrace();
      return;
    } 
  }
  
  public static void addToClientSockets(String name, ClientSocket clientSocket) {
    clientSockets.put(name, clientSocket);
    main.addData(String.valueOf(name) + " connecté");
  }
  
  public static void removeFromClientSockets(String name) {
    ClientSocket clientSocket = (ClientSocket)clientSockets.get(name);
    clientSocket.closeSocket();
    System.out.println("size before:" + clientSockets.size());
    clientSockets.remove(name);
    System.out.println("size after:" + clientSockets.size());
    main.addData(String.valueOf(name) + " déconnecté");
  }
  
  public static void SendToAllButOne(Object obj, ClientSocket someClientSocket) {
    HashSet<ClientSocket> sockets = new HashSet<ClientSocket>(clientSockets.values());
    
    for (ClientSocket clientSocket : sockets) {
      
      if (clientSocket != someClientSocket) {
        
        try {
          
          clientSocket.getObjectOutputStream().writeObject(obj);
          clientSocket.getObjectOutputStream().flush();
        }
        catch (IOException e) {
          e.printStackTrace();
        } 
      }
    } 
  }
  
  public static void sendClientsListToClient(ClientSocket clientSocket) {
    HashSet<String> names = new HashSet<String>(clientSockets.keySet());
   
    try {
      clientSocket.getObjectOutputStream().writeObject(names);
      clientSocket.getObjectOutputStream().flush();
    } catch (IOException e) {
      
      e.printStackTrace();
    } 
  }
  
  public static void sendToAll(Object obj) {
    HashSet<ClientSocket> sockets = new HashSet<ClientSocket>(clientSockets.values());
    
    for (ClientSocket clientSocket : sockets) {
      
      try {
        clientSocket.getObjectOutputStream().writeObject(obj);
        clientSocket.getObjectOutputStream().flush();
      }
      catch (IOException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public static void sendTo(String to, Object obj, ClientSocket senderSocket) {
    ClientSocket clientSocket = (ClientSocket)clientSockets.get(to);
    try {
      clientSocket.getObjectOutputStream().writeObject(obj);
      clientSocket.getObjectOutputStream().flush();
      
      senderSocket.getObjectOutputStream().writeObject(obj);
      senderSocket.getObjectOutputStream().flush();
    } catch (IOException e) {
      
      e.printStackTrace();
    } 
  }
  
  public static boolean isNameFree(String name) { return !clientSockets.containsKey(name); }
}