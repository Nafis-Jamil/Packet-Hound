/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import javafx.collections.ObservableList;
import jpcap.packet.ARPPacket;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

/**
 *
 * @author nafis
 */
public class PacketData {

    private Packet packet;
    private long id;
    private long time_stamp;
    private String src_ip, dest_ip, protocol;
    private int len;
    private PacketTime time = new PacketTime();
    private String rely;

    public PacketData(Packet packet, long id, long time_stamp, String src_ip, String dest_ip, String protocol, int len) {
        this.packet = packet;
        this.id = id;
        this.time_stamp = time_stamp;
        this.src_ip = src_ip;
        this.dest_ip = dest_ip;
        this.protocol = protocol;
        this.len = len;
    }

    public PacketData() {

    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public PacketData(String src_ip, String dest_ip) {
        this.src_ip = src_ip;
        this.dest_ip = dest_ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public void setDest_ip(String dest_ip) {
        this.dest_ip = dest_ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void show_info(PacketData packetData, ObservableList<InfoCell> list) {
        packet = packetData.packet;
        DatalinkPacket dp = packet.datalink;
        EthernetPacket ep = (EthernetPacket) dp;
        time.epoch_to_humanTime(packetData.time_stamp);
        list.addAll(
                new InfoCell("CAPTURE TIME", time.gmtTime),
                //new InfoCell("CAPTURE TIME (LOCAL)", time.localTime),
                new InfoCell("SOURCE IP-ADDRESS", packetData.src_ip),
                new InfoCell("DESTINATION IP-ADDRESS", packetData.dest_ip),
                new InfoCell("SOURCE HARDWARE ADDRESS(MAC)", ep.getSourceAddress().toUpperCase()),
                new InfoCell("SOURCE HARDWARE ADDRESS(MAC)", ep.getDestinationAddress().toUpperCase()),
                new InfoCell("PROTOCOL", packetData.protocol)
        );

        if (packet instanceof IPPacket) {
            IPPacket iPPacket = (IPPacket) packetData.packet;

            if (iPPacket.r_flag) {
                rely = "RELIABLE";
            } else {
                rely = "NOT RELIABLE";
            }
            list.add(new InfoCell("TRANSMISSION RELIABILITY", rely));
            if (iPPacket.protocol == IPPacket.IPPROTO_TCP) {

                TCPPacket tp = (TCPPacket) iPPacket;
                list.addAll(
                        new InfoCell("SOURCE PORT", Integer.toString(tp.src_port)),
                        new InfoCell("DESTINATION PORT", Integer.toString(tp.dst_port))
                );
                if (tp.ack) {
                    list.add(new InfoCell("ACKNOWLEDGEMENT PACKET", "YES"));
                } else {
                    list.add(new InfoCell("ACKNOWLEDGEMENT PACKET", "NO"));
                }
                if (tp.rst) {
                    list.add(new InfoCell("RESET CONNECTION", "YES"));
                } else {
                    list.add(new InfoCell("RESET CONNECTION", "NO"));
                }
                if (tp.fin) {
                    list.add(new InfoCell("SENDER DATA AVAILIBILITY", "NO"));

                } else {
                    list.add(new InfoCell("SENDER DATA AVAILIBILITY", "YES"));
                }
                if (tp.syn) {
                    list.add(new InfoCell("CONNECTION REQUEST", "YES"));

                } else {
                    list.add(new InfoCell("CONNECTION REQUEST", "NO"));
                }

            } else if (iPPacket.protocol == IPPacket.IPPROTO_UDP) {
                UDPPacket uDPPacket = (UDPPacket) iPPacket;
                list.addAll(
                        new InfoCell("SOURCE PORT", Integer.toString(uDPPacket.src_port)),
                        new InfoCell("DESTINATION PORT", Integer.toString(uDPPacket.dst_port))
                );

            } else if ((iPPacket.protocol == IPPacket.IPPROTO_ICMP)) {
                ICMPPacket iCMPPacket = (ICMPPacket) iPPacket;
                list.addAll(
                        new InfoCell("ALIVE TIME", Integer.toString(iCMPPacket.alive_time)),
                        new InfoCell("ADVERTISED ADDRESS NO.", Integer.toString((int) iCMPPacket.addr_num)),
                        new InfoCell("MAXIMUM TRANSMISSION UNIT", Integer.toString(iCMPPacket.mtu)),
                        new InfoCell("SUBNET MASK", Integer.toString(iCMPPacket.subnetmask))
                );
            }
            list.addAll(new InfoCell("HOP LIMIT", Integer.toString(iPPacket.hop_limit)),
                    new InfoCell("IDENTIFICATION FIELD", Integer.toString(iPPacket.ident)),
                    new InfoCell("PRIORITY", Integer.toString((int) iPPacket.priority)),
                    new InfoCell("SERVICE FIELD", Integer.toString(iPPacket.rsv_tos)),
                    new InfoCell("PROTOCOL VERSION", Integer.toString((int) iPPacket.version)),
                    new InfoCell("FLOW LABEL FIELD", Integer.toString((int) iPPacket.flow_label))
            );

        } else if (packet instanceof ARPPacket) {
            ARPPacket aRPPacket = (ARPPacket) packet;
            switch (aRPPacket.hardtype) {
                case ARPPacket.HARDTYPE_ETHER:
                    list.add(new InfoCell("HARDWARE TYPE", "ETHERNET"));
                    break;
                case ARPPacket.HARDTYPE_FRAMERELAY:
                    list.add(new InfoCell("HARDWARE TYPE", "FRAME RELAY"));
                    break;
                case ARPPacket.HARDTYPE_IEEE802:
                    list.add(new InfoCell("HARDWARE TYPE", "IEEE 802"));
                    break;
                default:
                    list.add(new InfoCell("HARDWARE TYPE", Integer.toString(aRPPacket.hardtype)));
                    break;
            }
            switch(aRPPacket.operation){
                case ARPPacket.ARP_REQUEST:
                    list.add(new InfoCell("OPERATION", "REQUEST"));
                    break;
                  case ARPPacket.ARP_REPLY:
                    list.add(new InfoCell("OPERATION", "REPLY"));
                    break;
                   case ARPPacket.RARP_REQUEST:
                    list.add(new InfoCell("OPERATION", "RARP REQUEST"));
                    break;
                      case ARPPacket.RARP_REPLY:
                    list.add(new InfoCell("OPERATION", "RARP REPLY"));
                    break;
                      case ARPPacket.INV_REPLY:
                    list.add(new InfoCell("OPERATION","INV-REPLY"));
                    break;
                     case ARPPacket.INV_REQUEST:
                    list.add(new InfoCell("OPERATION", "INV-REQUEST"));
                    break;
                    default:
                    list.add(new InfoCell("OPERATION", Integer.toString(aRPPacket.operation)));
                    break;
                    
                    
                    
            }
            list.addAll(
            new InfoCell("PROTOTYPE", Integer.toString(aRPPacket.prototype)),
            new InfoCell("HARDWARE LENGTH",Integer.toString(aRPPacket.hlen)),
            new InfoCell("PACKET LENGTH",Integer.toString(aRPPacket.plen))
            );
            
        }

    }

}
