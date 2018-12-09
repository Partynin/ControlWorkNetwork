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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

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
        tfServer.setText("www.oracle.com"); // Default server name
        paneForLabelAndTfEnterAServer.setCenter(tfServer);

        // Panel p to hold the label enter a server and text field
        BorderPane paneForLabelAndTfEnterAPort = new BorderPane();
        paneForLabelAndTfEnterAPort.setPadding(new Insets(5, 5, 5, 5));
        paneForLabelAndTfEnterAPort.setLeft(new Label("Enter a port:     "));
        tfPort.setAlignment(Pos.BOTTOM_RIGHT);
        tfPort.setText("80"); // Default port number
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
                    System.out.println("Connection to server " + server + " is "
                            + socket.isConnected());
                    findAllPages(server);
                }
                else {
                    System.out.println("Can't connect to the server.");
                }
            } catch (IOException ex) {
                System.out.println(ex.toString() + " it happen in first try-catch");
            }
        }
    }

    private void findAllPages(String startingURL) {
        LinkedList<String> listOfPendingURLs = new LinkedList<>();
        HashSet<String> listOfTraversedURLs = new HashSet<>();

        listOfPendingURLs.add(startingURL);
        while (!listOfPendingURLs.isEmpty()) {
            String url = listOfPendingURLs.pop(); // Remove and return the top element
            if (!listOfTraversedURLs.contains(url)) {
                listOfTraversedURLs.add(url);
                System.out.println("Craw " + url);
                getSubURLs(url).stream().filter(
                        s -> !listOfTraversedURLs.contains(s)).forEach(listOfPendingURLs::push);
            }
        }
    }

    private ArrayList<String> getSubURLs(String urlString) {
        ArrayList<String> list = new ArrayList<>(1000);

        try {
            URL url = new URL("https://" + urlString);
            Scanner input = new Scanner(url.openStream());
            int current = 0;
            while (input.hasNext()) {
                String line = input.nextLine();
                current = line.indexOf("https://" + urlString, current);
                while (current > 0) {
                    int endIndex = line.indexOf("\"", current);
                    if (endIndex > 0) { // Ensure that a correct URL is found
                        list.add(line.substring(current, endIndex));
                        current = line.indexOf("https://" + urlString, endIndex);
                    }
                    else
                        current = -1;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return list;
    }
}
