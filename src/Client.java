import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    TextField tfServer = new TextField();
    TextField tfPort = new TextField();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Panel p to hold the label enter a server and text field
        BorderPane paneForLabelAndTfEnterAServer = new BorderPane();
        paneForLabelAndTfEnterAServer.setPadding(new Insets(5, 5, 5, 5));
        paneForLabelAndTfEnterAServer.setLeft(new Label("Enter a server:  "));
        tfServer.setAlignment(Pos.BOTTOM_RIGHT);
        paneForLabelAndTfEnterAServer.setCenter(tfServer);

        // Panel p to hold the label enter a server and text field
        BorderPane paneForLabelAndTfEnterAPort = new BorderPane();
        paneForLabelAndTfEnterAPort.setPadding(new Insets(5, 5, 5, 5));
        paneForLabelAndTfEnterAPort.setLeft(new Label("Enter a port:     "));
        tfPort.setAlignment(Pos.BOTTOM_RIGHT);
        paneForLabelAndTfEnterAPort.setCenter(tfPort);

        // Panel fro the button submit
        BorderPane paneForButton = new BorderPane();
        paneForButton.setPadding(new Insets(5, 5, 5, 5));
        Button buttonSubmit = new Button("Submit");
        paneForButton.setRight(buttonSubmit);

        // VPanel for hold the fields enter server, enter port and button submit
        VBox vPanelForEnterServerPortAndButton = new VBox();
        vPanelForEnterServerPortAndButton.setStyle("-fx-border-color: green");
        vPanelForEnterServerPortAndButton.getChildren().addAll(paneForLabelAndTfEnterAServer,
                paneForLabelAndTfEnterAPort, paneForButton);

        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(vPanelForEnterServerPortAndButton);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 300);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonSubmit.setOnAction(e -> connectToServer());
    }

    private void connectToServer() {
        String server = tfServer.getText();
        String port = tfPort.getText();
        if (server.isEmpty() || port.isEmpty()) {
            System.out.println("Please enter the server name and port.");
        }
        else {
            try (Socket socket = new Socket(server, Integer.parseInt(port))) {
                // Check the connection
                if (socket.isConnected()) {
                    findAllPages();
                }
                else {
                    System.out.println("Can't connect to the server.");
                }
            } catch (IOException ex) {
                System.out.println(ex.toString() + " it happen in first try-catch");
            }
        }
    }

    private void findAllPages() {

    }
}
