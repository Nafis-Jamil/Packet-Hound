/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author nafis
 */
public class BoxCell extends ListCell<MyDevice>{
    @Override
    public void updateItem(MyDevice item, boolean empty){
        super.updateItem(item, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }
        else{
            setText(item.name);
            Image img = null;
            if(item.connected){
                img=new Image("connected.png");
            }
            else{
                img=new Image("not_connected.png");
            }
            
            ImageView icon = new ImageView(img);
            setGraphic(icon);
        }
    }
    
}