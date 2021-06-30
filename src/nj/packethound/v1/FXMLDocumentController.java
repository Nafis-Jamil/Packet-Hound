/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author nafis
 */
public class FXMLDocumentController implements Initializable {

    public static Stage fileStage = new Stage();
    Sniffer sniffer;
    FileChooser fc;
    final String not_connected_header = "DEVICE NOT CONNECTED";
    final String not_connected_text = "Please choose a connected device based on the icon status.";
    final Image filterIcon = new Image("filtericon.png");
    @FXML
    public Button startBtn;
    @FXML
    public Button stopBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button clearBtn;
    @FXML
    public ComboBox<MyDevice> cb;
    @FXML
    public TableView<PacketData> tv;
    @FXML
    public TableColumn<PacketData, Long> packet_id;
    @FXML
    public TableColumn<PacketData, Long> time;
    @FXML
    public TableColumn<PacketData, String> src;
    @FXML
    public TableColumn<PacketData, String> dest;
    @FXML
    public TableColumn<PacketData, String> proto;
    @FXML
    public TableColumn<PacketData, Integer> len;
    @FXML
    public TextField searchField;
    @FXML
    public TableView<InfoCell> dev_info_table;
    @FXML
    public TableColumn<InfoCell, String> dev_prop_col;
    @FXML
    public TableColumn<InfoCell, String> dev_value_col;
    @FXML
    public TableView<InfoCell> packet_info_table;
    @FXML
    public TableColumn<InfoCell, String> packet_prop_col;
    @FXML
    public TableColumn<InfoCell, String> packet_val_col;

    public ObservableList<InfoCell> packet_info = FXCollections.observableArrayList();
    public ObservableList<InfoCell> dev_info = FXCollections.observableArrayList();
    public ObservableList<PacketData> packets = FXCollections.observableArrayList();
    public FilteredList<PacketData> filteredList = new FilteredList<>(packets, e -> true);

    public void loadFilter(ActionEvent event) throws Exception {
        FilterController.scene_id = 1;
        Stage filterStage = new Stage();
        filterStage.initModality(Modality.APPLICATION_MODAL);
        filterStage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        filterStage.setTitle("FILTER");
        filterStage.setResizable(false);
        filterStage.setAlwaysOnTop(true);
        Parent root = FXMLLoader.load(getClass().getResource("SetFilter.fxml"));
        Scene scene = new Scene(root);
        filterStage.setScene(scene);
        Sniffer.capture_state = false;
        filterStage.getIcons().add(filterIcon);
        filterStage.show();
    }

    public void loadDevice(ActionEvent event) {
        Sniffer.capture_state = false;
        sniffer.index = cb.getSelectionModel().getSelectedItem().index;
        dev_info.clear();
        new DeviceInfo(sniffer.deviceList[sniffer.index]).setInfo(dev_info);
        System.out.println(sniffer.index);
    }

    public void startCapture(ActionEvent event) throws IOException {
        if (cb.getSelectionModel().getSelectedItem().connected) {
            System.out.println("Start");
            Sniffer.capture_state = true;
            sniffer.capture(tv, packets);
        } else {
            Alert not_connected_alert = new Alert(Alert.AlertType.WARNING);
            not_connected_alert.setHeaderText(not_connected_header);
            not_connected_alert.setContentText(not_connected_text);
            not_connected_alert.show();
        }
    }

    public void stopCapture(ActionEvent event) {
        System.out.println("Stop");
        Sniffer.capture_state = false;

    }

    public void clear(ActionEvent event) {
        Sniffer.count = 0;
        packets.clear();
    }

    public void loadPacket(ActionEvent event) throws IOException {
        Sniffer.capture_state = false;
        File packetFile = fc.showOpenDialog(new Stage());
        if (packetFile != null) {
            Sniffer.count = 0;
            String path = packetFile.getAbsolutePath();
            new LoadPackets().loadPackets(tv, packets, path);
        }

    }

    public void savePacket(ActionEvent event) throws IOException {
        Sniffer.capture_state = false;
        File packetFile = fc.showSaveDialog(new Stage());
        if (packetFile != null) {
            String path = packetFile.getAbsolutePath();
            new SavePackets().savePackets(sniffer.captor, path, packets);
        }

    }

    public void search(KeyEvent event) {
        searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super PacketData>) pd -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                if (Long.toString(pd.getId()).contains(newValue)) {
                    return true;
                } else if (pd.getSrc_ip().toLowerCase().contains(lowercase)) {
                    return true;
                } else if (pd.getDest_ip().toLowerCase().contains(lowercase)) {
                    return true;
                } else if (pd.getProtocol().toLowerCase().contains(lowercase)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<PacketData> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tv.comparatorProperty());
        tv.setItems(sortedList);

    }

    public void setPlaceHolder() {
        Label packLabel = new Label("WAITING FOR PACKETS");
        packLabel.setFont(Font.font("sans serif", FontWeight.BOLD, 50));
        packLabel.setOpacity(.4);
        tv.setPlaceholder(packLabel);
        Label infoLabel = new Label("PACKET INFORMATION");
        infoLabel.setFont(Font.font("sans serif", FontWeight.BOLD, 30));
        infoLabel.setOpacity(.4);
        packet_info_table.setPlaceholder(infoLabel);
        Label devLabel = new Label("DEVICE INFORMATION");
        devLabel.setFont(Font.font("sans serif", FontWeight.BOLD, 20));
        devLabel.setOpacity(.4);
        dev_info_table.setPlaceholder(devLabel);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startBtn.disableProperty().bind(cb.getSelectionModel().selectedItemProperty().isNull());
        stopBtn.disableProperty().bind(cb.getSelectionModel().selectedItemProperty().isNull());
        saveBtn.disableProperty().bind(Bindings.size(packets).isEqualTo(0));
        clearBtn.disableProperty().bind(Bindings.size(packets).isEqualTo(0));
        dev_info_table.setItems(dev_info);
        packet_info_table.setItems(packet_info);
        tv.setItems(packets);
        setPlaceHolder();
        tv.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<PacketData>() {
            @Override
            public void changed(ObservableValue<? extends PacketData> observable, PacketData oldValue, PacketData newValue) {
                try {
                    packet_info_table.getItems().clear();
                    new PacketData().show_info(tv.getSelectionModel().getSelectedItem(), packet_info);
                } catch (Exception e) {
                }

            }

        }
        );

        sniffer = new Sniffer(0);
        fc = new FileChooser();
        fc.setInitialDirectory(new File("Packets"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tcpdump", "*.pcap"));
        String hostAdress = null;
        try {
            hostAdress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < sniffer.deviceList.length; i++) {

            String ip_address = sniffer.deviceList[i].addresses[1].address.toString();

            ip_address = ip_address.replace("/", " ");
            ip_address = ip_address.trim();

            if (ip_address.equals(hostAdress)) {
                cb.getItems().add(new MyDevice(sniffer.deviceList[i].description, i, true));
            } else {
                cb.getItems().add(new MyDevice(sniffer.deviceList[i].description, i, false));
            }

        }
        cb.setCellFactory(new BoxCellFactory());
        cb.setButtonCell(new BoxCell());

//        dev_info_table.setStyle(
//         "-fx-font-size: 20; "
//        );
        dev_prop_col.setCellValueFactory(new PropertyValueFactory<>("property"));
        dev_value_col.setCellValueFactory(new PropertyValueFactory<>("value"));
        packet_prop_col.setCellValueFactory(new PropertyValueFactory<>("property"));
        packet_val_col.setCellValueFactory(new PropertyValueFactory<>("value"));
        packet_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        time.setCellValueFactory(new PropertyValueFactory<>("time_stamp"));
        src.setCellValueFactory(new PropertyValueFactory<>("src_ip"));
        dest.setCellValueFactory(new PropertyValueFactory<>("dest_ip"));
        proto.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        len.setCellValueFactory(new PropertyValueFactory<>("len"));

    }

}
