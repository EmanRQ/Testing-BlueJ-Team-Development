import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;

public class NEXA_Main extends Application {
    // 1. Declare these at the CLASS LEVEL (Fields)
    private NEXA_Team[] members = new NEXA_Team[4];
    private Label welcomeLabel; 
    private Button memberButton;

    @Override
    public void start(Stage primaryStage) {
        loadMembersFromFile();

        // 2. Initialize them inside start()
        welcomeLabel = new Label("Hello Visitor ^ ^");
        memberButton = new Button("Are you a NEXA member?");
        
        memberButton.setOnAction(e -> {
            // Now you can safely use welcomeLabel here because it's a field
            welcomeLabel.setText("Checking database...");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("NEXA Login");
            dialog.setContentText("Please enter your name:");
        
            dialog.showAndWait().ifPresent(inputName -> {
                boolean found = false;
                // Re-use your members array here!
                for (NEXA_Team m : members) { 
                    if (m != null && m.getName().equalsIgnoreCase(inputName)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Welcome " + m.getName() + "! Role: " + m.getRole());
                        alert.show();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Member not found!");
                    alert.show();
                }
            });
        });
        
        // 2. Create Layout
        VBox root = new VBox(10);
        root.getChildren().addAll(welcomeLabel, memberButton);
        
        // 3. Create Scene and link CSS
        Scene scene = new Scene(root, 300, 200);
        
        // --- THIS IS WHERE YOU CALL THE CSS ---
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        primaryStage.setTitle("NEXA System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void loadMembersFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Members.txt"));
            for (int i = 0; i < 4; i++) {
                String line = br.readLine();
                if (line != null) {
                    String[] parts = line.split(", ");
                    members[i] = new NEXA_Team(parts[0], parts[1]);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}