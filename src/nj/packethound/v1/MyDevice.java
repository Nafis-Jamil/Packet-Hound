/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

/**
 *
 * @author nafis
 */
public class MyDevice {
    
    String name;
    boolean connected;
    int index;
    public MyDevice(String name,int index, boolean connected){
          this.name= name;
          this.index=index;
          this.connected=connected;
    }
    
}
