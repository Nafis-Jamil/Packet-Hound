/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * FXML Controller class
 *
 * @author nafis
 */
public class FilterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    static int scene_id = 1;
    static String selectedFilterID = "null";
    static ObservableList<BookMarks> bookMarks = FXCollections.observableArrayList();
    ObservableList<WebHistory.Entry> entrys;
    WebHistory history;
    WebEngine engine;
    double webZoom = 1;
    final String homepage = "https://wiki.wireshark.org";
    FilterValidityChecker fvc;
    final String Right = "-fx-background-color:rgba(22, 199, 154, .4)";
    final String Wrong = "-fx-background-color:rgba(245, 92, 71, .4)";
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    JFXToggleButton ip;
    @FXML
    JFXToggleButton arp;
    @FXML
    JFXToggleButton tcp;
    @FXML
    JFXToggleButton udp;
    @FXML
    JFXToggleButton icmp;
    @FXML
    JFXToggleButton port_53;
    @FXML
    JFXToggleButton port_80;
    @FXML
    JFXToggleButton port_443;
    @FXML
    JFXToggleButton igmp;
    @FXML
    JFXToggleButton ip6;
    @FXML
    JFXToggleButton not_arp;
    @FXML
    JFXToggleButton custom_filter_toggle;
    @FXML
    TextField custom_filter_field;
    @FXML
    RadioButton filter_checker;
    @FXML
    Label filter_warning;
    @FXML
    Hyperlink filter_hyper;
    @FXML
    ToggleGroup filter;
    @FXML
    TableView<BookMarks> book_table;
    @FXML
    TableColumn<BookMarks, String> filter_name_col;
    @FXML
    TableColumn<BookMarks, String> filter_exp_col;
    @FXML
    Button add_btn;
    @FXML
    Button remove_btn;
    @FXML
    Button copy_btn;
    @FXML
    WebView webView;

    public void open_filter_link(ActionEvent event) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI("https://gitlab.com/wireshark/wireshark/-/wikis/CaptureFilters"));
    }

    public void setFilterScene(ActionEvent event) throws Exception {
        scene_id = 1;
        root = FXMLLoader.load(getClass().getResource("SetFilter.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void bookmarkScene(ActionEvent event) throws Exception {
        scene_id = 2;
        root = FXMLLoader.load(getClass().getResource("Bookmark.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void filterInfoScene(ActionEvent event) throws Exception {
        scene_id = 3;
        root = FXMLLoader.load(getClass().getResource("FilterInfo.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void set_custom_filter(KeyEvent event) {
        String str = custom_filter_field.getText().toLowerCase();
        Sniffer.filterString = str;
        if (filter_checker.isSelected()) {

            if (fvc.checkIfValid(str)) {
                custom_filter_field.setStyle(
                        Right
                );
            } else {
                custom_filter_field.setStyle(
                        Wrong
                );

            }
        }

    }

    public void filter_checker_toggle(ActionEvent event) {
        if (filter_checker.isSelected()) {
            fvc = new FilterValidityChecker();
        }
    }

    public void change_filter_name(TableColumn.CellEditEvent<BookMarks, String> editEvent) {
        BookMarks bm = book_table.getSelectionModel().getSelectedItem();
        bm.setFilterName(editEvent.getNewValue());

    }

    public void change_filter_exp(TableColumn.CellEditEvent<BookMarks, String> editEvent) {
        BookMarks bm = book_table.getSelectionModel().getSelectedItem();
        bm.setFilterExpression(editEvent.getNewValue());

    }

    public void addNew() {

        // get current position
        TablePosition pos = book_table.getFocusModel().getFocusedCell();

        // clear current selection
        book_table.getSelectionModel().clearSelection();

        // create new record and add it to the model
        BookMarks bm = new BookMarks();
        bookMarks.add(bm);

        // get last row
        book_table.getSelectionModel().selectLast();

        book_table.scrollTo(bm);

    }

    public void removeFilter(ActionEvent event) {
        bookMarks.remove(book_table.getSelectionModel().getSelectedIndex());
    }

    public void addFilter(ActionEvent event) {

        addNew();
    }

    public void copyFilter(ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(book_table.getSelectionModel().getSelectedItem().filterExpression);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public void home(ActionEvent event) {
        engine.load(homepage);
    }

    public void back(ActionEvent event) {
        history = engine.getHistory();
        try {
            history.go(-1);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        }
    }

    public void forward(ActionEvent event) {
        history = engine.getHistory();
        try {
            history.go(1);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        }
    }

    public void zoom_in(ActionEvent event) {
        webZoom += .25;
        webView.setZoom(webZoom);
    }

    public void zoom_out(ActionEvent event) {
        webZoom -= .25;
        webView.setZoom(webZoom);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switch (scene_id) {

//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
            case 1:

                custom_filter_field.disableProperty().bind(custom_filter_toggle.selectedProperty().not());
                filter_checker.disableProperty().bind(custom_filter_toggle.selectedProperty().not());
                filter_warning.visibleProperty().bind(filter_checker.selectedProperty());

                switch (selectedFilterID) {
                    case "ip":
                        ip.setSelected(true);
                        break;
                    case "arp":
                        arp.setSelected(true);
                        break;
                    case "tcp":
                        tcp.setSelected(true);
                        break;
                    case "udp":
                        udp.setSelected(true);
                        break;
                    case "icmp":
                        icmp.setSelected(true);
                        break;
                    case "port_53":
                        port_53.setSelected(true);
                        break;
                    case "port_80":
                        port_80.setSelected(true);
                        break;
                    case "port_443":
                        port_443.setSelected(true);
                        break;
                    case "igmp":
                        igmp.setSelected(true);
                        break;
                    case "ip6":
                        ip6.setSelected(true);
                        break;
                    case "not_arp":
                        not_arp.setSelected(true);
                        break;
                    case "custom_filter_toggle":
                        custom_filter_toggle.setSelected(true);
                        custom_filter_field.setPromptText(Sniffer.filterString.toUpperCase());
                        break;
                    default:
                        break;

                }

                filter.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

                    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                        if (filter.getSelectedToggle() != null) {

                            JFXToggleButton tb = (JFXToggleButton) filter.getSelectedToggle();
                            selectedFilterID = tb.getId();

                            Sniffer.filter = true;
                            if (!selectedFilterID.equals("custom_filter_toggle")) {
                                String filterString = tb.getId().replaceAll("_", " ");
                                Sniffer.filterString = filterString;
                            } else {
                                Sniffer.filterString = "";
                            }

                        } else {
                            selectedFilterID = "null";
                            Sniffer.filterString = "";
                            Sniffer.filter = false;
                        }

                    }
                });

                break;
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//

            case 2:
                add_btn.setFocusTraversable(false);
                remove_btn.disableProperty().bind(book_table.getSelectionModel().selectedItemProperty().isNull());
                copy_btn.disableProperty().bind(book_table.getSelectionModel().selectedItemProperty().isNull());
                Tooltip addTooltip = new Tooltip("Add Bookmark");
                add_btn.setTooltip(new Tooltip("Add Bookmark"));
                remove_btn.setTooltip(new Tooltip("Remove Bookmark"));
                copy_btn.setTooltip(new Tooltip("Copy Bookmark"));
                filter_name_col.setCellValueFactory(new PropertyValueFactory<>("filterName"));
                filter_name_col.setCellFactory(TextFieldTableCell.forTableColumn());
                filter_exp_col.setCellValueFactory(new PropertyValueFactory<>("filterExpression"));
                filter_exp_col.setCellFactory(TextFieldTableCell.forTableColumn());
                book_table.setItems(bookMarks);

                break;

//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//
//....................................................................//                
            case 3:
                engine = webView.getEngine();
                engine.load(homepage);
                history = engine.getHistory();
                entrys = history.getEntries();
                break;

            default:
                System.out.println("No Scene");

        }
    }

}
