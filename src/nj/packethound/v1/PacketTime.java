/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author nafis
 */
public class PacketTime {
    String gmtTime;
    String localTime;
    
    public void epoch_to_humanTime (long epoch){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            gmtTime = sdf.format(new java.util.Date(epoch * 1000));
            sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT+6"));
            localTime = sdf.format(new java.util.Date(epoch * 1000));
        } catch (Exception e) {
            gmtTime="UNKNOWN";
            localTime="UNKNOWN";
        }
    }
}
