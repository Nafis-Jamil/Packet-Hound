/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import jpcap.JpcapCaptor;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

/**
 *
 * @author nafis
 */
public class LoadPackets {
   private  String protocols[] = {"HOPOPT", "ICMP", "IGMP", "GGP", "IPV4", "ST", "TCP", "CBT", "EGP", "IGP", "BBN", "NV2", "PUP", "ARGUS", "EMCON", "XNET", "CHAOS", "UDP", "mux"};

    public void loadPackets(TableView <PacketData> tv,ObservableList<PacketData>packets,String path) throws IOException{
        JpcapCaptor captor = JpcapCaptor.openFile(path);
        captor.setFilter("ip", true);
        packets.clear();
        Thread loadThread = new Thread(
        new Runnable(){
            @Override
            public void run() {
                while(true){
                    Packet packet = captor.getPacket();
                    if(packet==null || packet==Packet.EOF) break;
                    else{
                        IPPacket ipp= (IPPacket) packet;
                        PacketData packetData = new PacketData(packet, ++Sniffer.count, ipp.sec, ipp.src_ip.toString().substring(1), ipp.dst_ip.toString().substring(1),protocols[ipp.protocol], ipp.len);
                          Platform.runLater(
                                () -> {
                                   
                                  
                                    packets.add(packetData);
                                }
                        );
                    }
                }
            }
            
        }
        
        );
        loadThread.start();
    }
}
