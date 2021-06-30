/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.packet.Packet;

/**
 *
 * @author nafis
 */
public class SavePackets {
    public void savePackets(JpcapCaptor captor, String path, ObservableList<PacketData> packets ) throws IOException{
       
        JpcapWriter writer = JpcapWriter.openDumpFile(captor, path);
        Thread saveThread = new Thread(
          new Runnable(){
            @Override
            public void run() {
                 for (PacketData packetData : packets) {
            writer.writePacket(packetData.getPacket());
        }
            }
              
          }
           
        );
        saveThread.start();
    }
    
}
