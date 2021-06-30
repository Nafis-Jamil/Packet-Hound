package nj.packethound.v1;

import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Sniffer {

    NetworkInterface[] deviceList;
    JpcapCaptor captor;
    int index;
    static boolean filter;
    static boolean capture_state;
    private  String protocols[] = {"HOPOPT", "ICMP", "IGMP", "GGP", "IPV4", "ST", "TCP", "CBT", "EGP", "IGP", "BBN", "NV2", "PUP", "ARGUS", "EMCON", "XNET", "CHAOS", "UDP", "mux"};
    static long count;
    static String filterString="";

    public Sniffer(long count) {
        deviceList = JpcapCaptor.getDeviceList();
        filter = false;
        capture_state = false;
        Sniffer.count= count;
      
    }
    
    private PacketData getTableInfo(Packet packet){
        PacketData packetData=null;
        if(packet instanceof IPPacket){
              String protoString = "UNKNOWN";
              IPPacket ipp= (IPPacket) packet;
              if(ipp.protocol < protocols.length) protoString = protocols[ipp.protocol] ;
              else if(ipp.protocol== IPPacket.IPPROTO_IPv6) protoString="IPv6";
              packetData = new PacketData(packet, ++count, ipp.sec, ipp.src_ip.toString().substring(1), ipp.dst_ip.toString().substring(1),protoString, ipp.len);
        }
        else if(packet instanceof ARPPacket){
            ARPPacket ap= (ARPPacket) packet;
            packetData= new PacketData(packet, ++count, ap.sec ,ap.getSenderProtocolAddress().toString().substring(1), ap.getTargetProtocolAddress().toString().substring(1),"ARP", ap.len);
        }
        else{
            packetData=new PacketData(packet,0,++count,"Packet Type Not Identified","Packet Type Not Identified","Packet Type Not Identified",0);
        }
        return packetData;
    }
  

    public void capture(TableView <PacketData> tv,ObservableList <PacketData> packets) throws IOException {
               captor = JpcapCaptor.openDevice(deviceList[index], 65530, false, 1000);
               System.out.println(Sniffer.filter);
               System.out.println(Sniffer.filterString);
         if (filter) {
             try {
                 captor.setFilter(filterString, true);
                
    
       

             } catch (IOException iOException) {
             Alert invalid_filter_alert = new Alert(Alert.AlertType.ERROR);
            invalid_filter_alert.setHeaderText("INVALID CAPTURE FILTER");
            invalid_filter_alert.setContentText("Please provide a filter with correct syntax. Check out the INFORMATION section to know more about filters.");
            invalid_filter_alert.show();
            return;
             }
        }
           Thread capThread = new Thread(
                new Runnable() {
            @Override
            public void run() {
           
                while (capture_state) {
                    Packet packet = captor.getPacket();
                    if (packet != null) {
                       
                       PacketData packetData =getTableInfo(packet);
                       
                       
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
            capThread.start(); 
          }
}
