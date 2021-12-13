package Server;

import Server.view.ServerLayoutController;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main
  extends Application
{
  private Stage primaryStage;
  private static ObservableList<String> data = FXCollections.observableArrayList();
  
  private static Thread serverThread;
  
  public static void main(String[] args) { launch(args); }
 
  public void start(Stage primaryStage) {
    serverThread = new ConcurrentServer(this);
    serverThread.start();
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Serveur Chat");
    initRootLayout();
  }
  
  public void initRootLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("view/ServerLayout.fxml"));
      AnchorPane layout = (AnchorPane)loader.load();
      
      ServerLayoutController controller = (ServerLayoutController)loader.getController();
      controller.setViewData(data);
      
      Scene scene = new Scene(layout);
      this.primaryStage.setScene(scene);
      this.primaryStage.show();
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
    
    this.primaryStage.setOnCloseRequest(e ->        
        System.exit(0));
  }
 
  public void addData(final String str) {
    Platform.runLater(new Runnable()
        {
          public void run()
          {
            data.add(str);
          }
        });
  }
}