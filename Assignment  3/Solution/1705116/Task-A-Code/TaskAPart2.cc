
#include <fstream>
#include "ns3/core-module.h"
#include "ns3/internet-module.h"
#include "ns3/internet-apps-module.h"
#include "ns3/mobility-module.h"
#include "ns3/spectrum-module.h"
#include "ns3/propagation-module.h"
#include "ns3/sixlowpan-module.h"
#include "ns3/lr-wpan-module.h"
#include "ns3/csma-module.h"


#include "ns3/flow-monitor-helper.h"
#include "ns3/flow-monitor-module.h"
#include "ns3/packet-sink-helper.h"
#include "ns3/bulk-send-helper.h"
#include <iostream>
#include<string>

using namespace ns3;
using namespace std;
uint32_t  nSink = 10;


void MyHelper1()
{
 string s1= "10.1.1.";
      for(int node_index =1 ; node_index<50; node_index++)
      {
        string s2 =to_string(node_index);
        string s=s1+s2;
        int v = s.length() ;
        char str[v+1];
        for(int i=0; i<v; i++)
        {
            str[i]=s[i];
        }
        str[v]='\0';
        Ipv4Address myadd = str;
        myadd =myadd;
       
      }

      for(int node_index =1 ; node_index<50; node_index++)
      {
        std::cout << "\n\n";
  std::cout << "Total Tx Packets: " << "\n";
  std::cout << "Total Rx Packets: " << "\n";
  std::cout << "Total Packets Lost: "  << "\n";
  std::cout << "Throughput: "<<"\n";
  std::cout << "End to End Delay: " <<"\n";
  std::cout << "Packets Delivery Ratio: " << "%" << "\n";
  std::cout << "Packets Loss Ratio: "<< "%" << "\n";
       
      }
}
  

  void MyHelper2()
{
 string s1= "10.1.1.";
      for(int node_index =1 ; node_index<50; node_index++)
      {
        string s2 =to_string(node_index);
        string s=s1+s2;
        int v = s.length() ;
        char str[v+1];
        for(int i=0; i<v; i++)
        {
            str[i]=s[i];
        }
        str[v]='\0';
        Ipv4Address myadd = str;
        myadd =myadd;
       
      }

      for(int node_index =1 ; node_index<50; node_index++)
      {
        std::cout << "\n\n";
  std::cout << "Total Tx Packets: " << "\n";
  std::cout << "Total Rx Packets: " << "\n";
  std::cout << "Total Packets Lost: "  << "\n";
  std::cout << "Throughput: "<<"\n";
  std::cout << "End to End Delay: " <<"\n";
  std::cout << "Packets Delivery Ratio: " << "%" << "\n";
  std::cout << "Packets Loss Ratio: "<< "%" << "\n";
       
      }
}
  
  void MyHelper3()
{
 string s1= "10.1.1.";
      for(int node_index =1 ; node_index<50; node_index++)
      {
        string s2 =to_string(node_index);
        string s=s1+s2;
        int v = s.length() ;
        char str[v+1];
        for(int i=0; i<v; i++)
        {
            str[i]=s[i];
        }
        str[v]='\0';
        Ipv4Address myadd = str;
        myadd =myadd;
       
      }

      for(int node_index =1 ; node_index<50; node_index++)
      {
        std::cout << "\n\n";
  std::cout << "Total Tx Packets: " << "\n";
  std::cout << "Total Rx Packets: " << "\n";
  std::cout << "Total Packets Lost: "  << "\n";
  std::cout << "Throughput: "<<"\n";
  std::cout << "End to End Delay: " <<"\n";
  std::cout << "Packets Delivery Ratio: " << "%" << "\n";
  std::cout << "Packets Loss Ratio: "<< "%" << "\n";
       
      }
}
  


int main (int argc, char** argv)
{
  bool verbose = false;

  Packet::EnablePrinting ();

  CommandLine cmd (__FILE__);
  cmd.AddValue ("verbose", "turn on log components", verbose);
  cmd.Parse (argc, argv);

  if (verbose)
    {
      LogComponentEnable ("Ping6Application", LOG_LEVEL_ALL);
      LogComponentEnable ("LrWpanMac", LOG_LEVEL_ALL);
      LogComponentEnable ("LrWpanPhy", LOG_LEVEL_ALL);
      LogComponentEnable ("LrWpanNetDevice", LOG_LEVEL_ALL);
      LogComponentEnable ("SixLowPanNetDevice", LOG_LEVEL_ALL);
    }


  uint32_t nWsnNodes = 100;
  NodeContainer wsnNodes;
  wsnNodes.Create (nWsnNodes);

  NodeContainer wiredNodes;
  wiredNodes.Create (1);
  wiredNodes.Add (wsnNodes.Get (0));

  MobilityHelper mobility;
  mobility.SetMobilityModel ("ns3::ConstantPositionMobilityModel");
  mobility.SetPositionAllocator ("ns3::GridPositionAllocator",
                                 "MinX", DoubleValue (0.0),
                                 "MinY", DoubleValue (0.0),
                                 "DeltaX", DoubleValue (80),
                                 "DeltaY", DoubleValue (80),
                                 "GridWidth", UintegerValue (10),
                                 "LayoutType", StringValue ("RowFirst"));
  mobility.SetMobilityModel ("ns3::ConstantPositionMobilityModel");
  mobility.Install (wsnNodes);

  Config::SetDefault ("ns3::RangePropagationLossModel::MaxRange", DoubleValue (100));
  Ptr<SingleModelSpectrumChannel> channel = CreateObject<SingleModelSpectrumChannel> ();
  Ptr<RangePropagationLossModel> propModel = CreateObject<RangePropagationLossModel> ();
  Ptr<ConstantSpeedPropagationDelayModel> delayModel = CreateObject<ConstantSpeedPropagationDelayModel> ();
  channel->AddPropagationLossModel (propModel);
  channel->SetPropagationDelayModel (delayModel);

  LrWpanHelper lrWpanHelper;
  lrWpanHelper.SetChannel(channel);
  // Add and install the LrWpanNetDevice for each node
  NetDeviceContainer lrwpanDevices = lrWpanHelper.Install (wsnNodes);

  // Fake PAN association and short address assignment.
  // This is needed because the lr-wpan module does not provide (yet)
  // a full PAN association procedure.
  lrWpanHelper.AssociateToPan (lrwpanDevices, 0);

  InternetStackHelper internetv6;
  internetv6.Install (wsnNodes);
  internetv6.Install (wiredNodes.Get (0));

  SixLowPanHelper sixLowPanHelper;
  NetDeviceContainer sixLowPanDevices = sixLowPanHelper.Install (lrwpanDevices);

  CsmaHelper csmaHelper;
  NetDeviceContainer csmaDevices = csmaHelper.Install (wiredNodes);

  Ipv6AddressHelper ipv6;
  ipv6.SetBase (Ipv6Address ("2001:cafe::"), Ipv6Prefix (64));
  Ipv6InterfaceContainer wiredDeviceInterfaces;
  wiredDeviceInterfaces = ipv6.Assign (csmaDevices);
  wiredDeviceInterfaces.SetForwarding (1, true);
  wiredDeviceInterfaces.SetDefaultRouteInAllNodes (1);

  ipv6.SetBase (Ipv6Address ("2001:f00d::"), Ipv6Prefix (64));
  Ipv6InterfaceContainer wsnDeviceInterfaces;
  wsnDeviceInterfaces = ipv6.Assign (sixLowPanDevices);
  wsnDeviceInterfaces.SetForwarding (0, true);
  wsnDeviceInterfaces.SetDefaultRouteInAllNodes (0);

  for (uint32_t i = 0; i < sixLowPanDevices.GetN (); i++)
    {
      Ptr<NetDevice> dev = sixLowPanDevices.Get (i);
      dev->SetAttribute ("UseMeshUnder", BooleanValue (true));
      dev->SetAttribute ("MeshUnderRadius", UintegerValue (10));
    }

  uint32_t packetSize = 64;
  uint32_t maxPacketCount = 50;
  Time interPacketInterval = Seconds (1.);
  Ping6Helper ping6;

 uint32_t rxPacketsum = 0;
  double Delaysum = 0; 
  // double rxTimeSum = 0;
 // double txTimeSum = 0;
  uint32_t txPacketsum = 0;
  uint32_t txBytessum = 0;
  uint32_t rxBytessum = 0;
  uint32_t txTimeFirst = 0;
  uint32_t rxTimeLast = 0;
  uint32_t lostPacketssum = 0;

  FlowMonitorHelper flowmon;
  Ptr<FlowMonitor> monitor = flowmon.InstallAll();
  std::ofstream plotData ("Lowrate_plot.txt", std::ios::app);




  ping6.SetLocal (wsnDeviceInterfaces.GetAddress (nWsnNodes - 1, 1));
  ping6.SetRemote (wiredDeviceInterfaces.GetAddress (0, 1));

  ping6.SetAttribute ("MaxPackets", UintegerValue (maxPacketCount));
  ping6.SetAttribute ("Interval", TimeValue (interPacketInterval));
  ping6.SetAttribute ("PacketSize", UintegerValue (packetSize));

  
  ApplicationContainer apps = ping6.Install (wsnNodes.Get (nWsnNodes - 1));
  uint32_t sinkPort =1;
  uint32_t tcp_adu_size = 64;
  uint32_t data_mbytes = 4;

  for( uint32_t i=1; i<=nSink; i++ ) {

    BulkSendHelper sourceApp ("ns3::TcpSocketFactory",
                              Inet6SocketAddress (wiredDeviceInterfaces.GetAddress (0, 1),sinkPort));
    sourceApp.SetAttribute ("SendSize", UintegerValue (tcp_adu_size));
    sourceApp.SetAttribute ("MaxBytes", UintegerValue (data_mbytes * 1000000));
    ApplicationContainer sourceApps = sourceApp.Install (wsnNodes.Get (i));
    sourceApps.Start (Seconds (1));
    sourceApps.Stop (Seconds (10));

    PacketSinkHelper sinkApp ("ns3::TcpSocketFactory",
    Inet6SocketAddress (Ipv6Address::GetAny (), sinkPort));
    sinkApp.SetAttribute ("Protocol", TypeIdValue (TcpSocketFactory::GetTypeId ()));
    ApplicationContainer sinkApps = sinkApp.Install (wiredNodes.Get(0));
    sinkApps.Start (Seconds (0.0));
    sinkApps.Stop (Seconds (10));
    sinkPort++;
  }

  apps.Start (Seconds (1.0));
  apps.Stop (Seconds (10.0));


  Simulator::Stop (Seconds (10));

  Simulator::Run ();

Ptr<Ipv4FlowClassifier> classifier = DynamicCast<Ipv4FlowClassifier> (flowmon.GetClassifier ());
  std::map<FlowId, FlowMonitor::FlowStats> stats = monitor->GetFlowStats ();
  
  for (std::map<FlowId, FlowMonitor::FlowStats>::const_iterator i = stats.begin (); i != stats.end (); ++i)
  {

    rxPacketsum += i->second.rxPackets;
    txPacketsum += i->second.txPackets;
    txBytessum += i->second.txBytes;
    rxBytessum += i->second.rxBytes;
    Delaysum += i->second.delaySum.GetSeconds();
    lostPacketssum += i->second.lostPackets;
    
    if(txTimeFirst == 0)
    {
      txTimeFirst = i->second.timeFirstTxPacket.GetSeconds();
    }
    
    rxTimeLast = i->second.timeLastRxPacket.GetSeconds();
    lostPacketssum += i->second.lostPackets;
    Delaysum += i->second.delaySum.GetSeconds();
  }

  uint64_t timeDiff = (rxTimeLast - txTimeFirst);
  
  std::cout << "\n\n";
  std::cout << "Total Tx Packets: " << txPacketsum << "\n";
  std::cout << "Total Rx Packets: " << rxPacketsum << "\n";
  std::cout << "Total Packets Lost: " << (txPacketsum - rxPacketsum) << "\n";
  std::cout << "Throughput: " << ((rxBytessum * 8.0) / timeDiff)/1024<<" Kbps"<<"\n";
  std::cout << "End to End Delay: " << Delaysum/rxPacketsum << "\n";
  std::cout << "Packets Delivery Ratio: " << ((rxPacketsum * 100) /txPacketsum) << "%" << "\n";
  std::cout << "Packets Loss Ratio: " << (((txPacketsum - rxPacketsum) * 100) /txPacketsum) << "%" << "\n";
 
  // monitor->SerializeToXmlFile ((tr_name + ".flowmon").c_str(), false, false);

  plotData<<nWsnNodes<<" ";
  plotData<<((rxBytessum * 8.0) / timeDiff)/1024<<" ";
  plotData<<Delaysum/rxPacketsum<<" ";
  plotData<<((rxPacketsum * 100) /txPacketsum)<<" ";
  plotData<<(((txPacketsum - rxPacketsum) * 100) /txPacketsum)<<"\n";

  Simulator::Destroy ();

}

