package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket
{
  private Socket socket;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  
  public ClientSocket(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
    this.socket = socket;
    this.output = output;
    this.input = input;
  }
  
  public Socket getSocker() { return this.socket; }
  
  public ObjectOutputStream getObjectOutputStream() { return this.output; }
  
  public ObjectInputStream getObjectInputStream() { return this.input; }
  
  public void closeSocket() {
    try {
      this.socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}