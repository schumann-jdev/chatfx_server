package Server;

import chat.model.Message;
import chat.model.NameAvailable;
import chat.model.RequestToExit;
import java.io.IOException;

public class ThreadHandler
  extends Thread
{
  ClientSocket clientSocket;
  
  public ThreadHandler(ClientSocket clientSocket) { this.clientSocket = clientSocket; }
 
  public void run() {
    try {
      ConcurrentServer.sendClientsListToClient(this.clientSocket);
      
      Object obj = null;
      String name = null;
      boolean isNameFree = false;
      
      do {
        try {
          obj = this.clientSocket.getObjectInputStream().readObject();
        }
        catch (ClassNotFoundException e) {
          e.printStackTrace();
        } 
        if (!(obj instanceof String))
          continue; 
        name = (String)obj;
        
        if (ConcurrentServer.isNameFree(name)) {
          
          System.out.print("FREE");
          isNameFree = true;
          sendToClient(new NameAvailable(name, true));
          ConcurrentServer.addToClientSockets((String)obj, this.clientSocket);
          
          break;
        } 
        
        System.out.print("NOT FREE");
        isNameFree = false;
        sendToClient(new NameAvailable(name, false));
      
      }
      while (!isNameFree);
      
      ConcurrentServer.SendToAllButOne(name, this.clientSocket);
      
      while (true) {
        obj = null;
        
        try {
          obj = this.clientSocket.getObjectInputStream().readObject();
        }
        catch (ClassNotFoundException e) {          
          e.printStackTrace();
        } 
        
        Message msg = null;
        
        if (obj instanceof Message) {
          
          msg = (Message)obj;
          String to = msg.getTo();
          
          if (to == null) {
            
            ConcurrentServer.sendToAll(obj);
            
            continue;
          } 
          ConcurrentServer.sendTo(to, obj, this.clientSocket);
          continue;
        } 
        if (obj instanceof RequestToExit)
          break; 
      }  RequestToExit requestToExit = (RequestToExit)obj;
      ConcurrentServer.removeFromClientSockets(requestToExit.getName());
      
      ConcurrentServer.sendToAll(obj);
      
      return;
    } catch (IOException ioe) {
      
      ioe.printStackTrace();
      return;
    } 
  }
  
  private void sendToClient(Object obj) {
    try {
      this.clientSocket.getObjectOutputStream().writeObject(obj);
      this.clientSocket.getObjectOutputStream().flush();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}