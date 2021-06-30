/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author nafis
 */
public class BoxCellFactory implements Callback<ListView<MyDevice>,ListCell<MyDevice>>
{

    @Override
    public ListCell<MyDevice> call(ListView<MyDevice> param) {
        return new BoxCell();
    }
    
}
