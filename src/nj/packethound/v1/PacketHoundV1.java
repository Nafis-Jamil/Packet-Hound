/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author nafis
 */
public class PacketHoundV1 extends Application {
    
   static File file = new File("bookmarks.txt");

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("myLayout.css").toExternalForm());
        Image iconImage= new Image("wolf.png");
        stage.getIcons().add(iconImage);
        stage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        stage.setTitle("PACKET HOUND");

        stage.setScene(scene);
        stage.show();

    }

    private void closeProgram() {
        Sniffer.capture_state = false;
        saveFiles();
        Platform.exit();
        System.exit(0);
    }
    private  void saveFiles(){
            try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<BookMarks>(FilterController.bookMarks));
            oos.close();
        } catch (FileNotFoundException e) { 
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    
    
          private static ObservableList<BookMarks> readFiles(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<BookMarks> list = (List<BookMarks>) ois.readObject() ;

            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FilterController.bookMarks=readFiles(file);
        launch(args);
      

    }

}
