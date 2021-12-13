package Server.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerLayoutController
{
  @FXML
  private ListView<String> listView;
  
  @FXML
  private void initialize() {}
  
  public void setViewData(ObservableList<String> data) { this.listView.setItems(data); }
}