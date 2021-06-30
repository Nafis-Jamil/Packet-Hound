/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import javafx.collections.ObservableList;
import jpcap.NetworkInterface;

/**
 *
 * @author nafis
 */
public class DeviceInfo {
    NetworkInterface networkInterface;
    String dev_name="NULL";
    String mac_address="";
    String ipV4_address="NULL";
    String data_link_name="NULL";
    String data_link_type="NULL";
    String ipV6_address="NULL";
    String subnet_address="NULL";
    String broadcast_address="NULL";
    final String loop_yes= "YES";
    final String loop_no= "NO";
    
    
    public DeviceInfo(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }
    
    public void setInfo(ObservableList <InfoCell> list){
        try {
            dev_name = networkInterface.description;
        } catch (Exception e) {
        }
         try {
            for (byte b : networkInterface.mac_address) {
                mac_address += ":" + Integer.toHexString(b & 0xff).toUpperCase();
                
            }
        } catch (Exception e) {
        }
        try {
            mac_address = mac_address.substring(1);
        } catch (Exception e) {
        }
        try {
            ipV4_address = networkInterface.addresses[1].address.toString().substring(1);
        } catch (Exception e) {
        }
         try {
            subnet_address = networkInterface.addresses[1].subnet.toString().substring(1);
        } catch (Exception e) {
        }
         try {
            broadcast_address = networkInterface.addresses[1].broadcast.toString().substring(1);
        } catch (Exception e) {
        }
         try {
            ipV6_address = networkInterface.addresses[0].address.toString().substring(1);
        } catch (Exception e) {
        }
        try {
            data_link_name = networkInterface.datalink_name;
        } catch (Exception e) {
        }
         try {
            data_link_type = networkInterface.datalink_description;
        } catch (Exception e) {
        }
         list.addAll(
          new InfoCell("DEVICE NAME", dev_name),
          new InfoCell("DATALINK NAME", data_link_name),
          new InfoCell("DATALINK TYPE", data_link_type),
          new InfoCell("IPv4 ADDRESS", ipV4_address),
          new InfoCell("IPv6 ADDRESS", ipV6_address),
          new InfoCell("PHYSICAL ADDRESS (MAC)", mac_address),
          new InfoCell("SUBNET ADDRESS", subnet_address),
          new InfoCell("BROADCAST ADDRESS", broadcast_address)
         );
        try {
            if (networkInterface.loopback) {
                list.add(new InfoCell("LOOPBACK", loop_yes));
            } else {
                list.add(new InfoCell("LOOPBACK", loop_no));
            }
        } catch (Exception e) {
             list.add(new InfoCell("LOOPBACK", "NULL"));
        }
    }
    
}
