/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 *
 * @author nafis
 */
public class FilterValidityChecker {
    JpcapCaptor captor;
    NetworkInterface ni[];
    public FilterValidityChecker(){
          try {
              ni=JpcapCaptor.getDeviceList();
            captor = JpcapCaptor.openDevice(ni[0], 65530, false, 20);
        } catch (IOException iOException) {
        }
    }
    
    boolean checkIfValid(String filter){
        try {
            captor.setFilter(filter,true);
             return true;
            
        } catch (IOException ex) {
           return false;
        }
    }
    
}
