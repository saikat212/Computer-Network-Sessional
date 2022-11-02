
#include <fstream>

#include <iostream>
#include "ns3/core-module.h"
#include "ns3/network-module.h"
#include "ns3/internet-module.h"
#include "ns3/mobility-module.h"
#include "ns3/aodv-module.h"
#include "ns3/olsr-module.h"
#include "ns3/dsdv-module.h"
#include "ns3/dsr-module.h"
#include "ns3/applications-module.h"
#include "ns3/yans-wifi-helper.h"

#include "ns3/flow-monitor-helper.h"
#include "ns3/flow-monitor-module.h"
#include<string>

using namespace ns3;
using namespace dsr;
using namespace std;

NS_LOG_COMPONENT_DEFINE ("manet-routing-compare");

class RoutingExperiment
{
public:
  RoutingExperiment ();
  void Run (int nSinks, double txp, std::string CSVfileName);
  //static void SetMACParam (ns3::NetDeviceContainer & devices,
  //                                 int slotDistance);
  std::string CommandSetup (int argc, char **argv);

private:
  Ptr<Socket> SetupPacketReceive (Ipv4Address addr, Ptr<Node> node);
  void ReceivePacket (Ptr<Socket> socket);
  void CheckThroughput ();
  void CheckThroughput1 ();
  void MyHelper();
  void MyHelper1();
  

  uint32_t port;
  uint32_t bytesTotal;
  uint32_t packetsReceived;

  std::string m_CSVfileName;
  int m_nSinks;
  std::string m_protocolName;
  double m_txp;
  bool m_traceMobility;
  uint32_t m_protocol;
};

RoutingExperiment::RoutingExperiment ()
  : port (9),
    bytesTotal (0),
    packetsReceived (0),
    m_CSVfileName ("TaskA_1.csv"),
    m_traceMobility (false),
    m_protocol (2) // AODV
{
}

static inline std::string
PrintReceivedPacket (Ptr<Socket> socket, Ptr<Packet> packet, Address senderAddress)
{
  std::ostringstream oss;

  oss << Simulator::Now ().GetSeconds () << " " << socket->GetNode ()->GetId ();

  if (InetSocketAddress::IsMatchingType (senderAddress))
    {
      InetSocketAddress addr = InetSocketAddress::ConvertFrom (senderAddress);
      oss << " received one packet from " << addr.GetIpv4 ();
    }
  else
    {
      oss << " received one packet!";
    }
  return oss.str ();
}

void
RoutingExperiment::ReceivePacket (Ptr<Socket> socket)
{
  Ptr<Packet> packet;
  Address senderAddress;
  while ((packet = socket->RecvFrom (senderAddress)))
    {
      bytesTotal += packet->GetSize ();
      packetsReceived += 1;
      NS_LOG_UNCOND (PrintReceivedPacket (socket, packet, senderAddress));
    }
}

void
RoutingExperiment::CheckThroughput ()
{
  double kbs = (bytesTotal * 8.0) / 1000;
  bytesTotal = 0;

  std::ofstream out (m_CSVfileName.c_str (), std::ios::app);

  out << (Simulator::Now ()).GetSeconds () << ","
      << kbs << ","
      << packetsReceived << ","
      << m_nSinks << ","
      << m_protocolName << ","
      << m_txp << ""
      << std::endl;

  out.close ();
  packetsReceived = 0;
  Simulator::Schedule (Seconds (1.0), &RoutingExperiment::CheckThroughput, this);
}

void
RoutingExperiment::CheckThroughput1 ()
{
  double kbs = (bytesTotal * 8.0) / 1000;
  bytesTotal = 0;

  std::ofstream out (m_CSVfileName.c_str (), std::ios::app);

  out << (Simulator::Now ()).GetSeconds () << ","
      << kbs << ","
      << packetsReceived << ","
      << m_nSinks << ","
      << m_protocolName << ","
      << m_txp << ""
      << std::endl;

  out.close ();
  packetsReceived = 0;
  Simulator::Schedule (Seconds (1.0), &RoutingExperiment::CheckThroughput, this);
}

void
RoutingExperiment::MyHelper()
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
  
void
RoutingExperiment::MyHelper1()
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


Ptr<Socket>
RoutingExperiment::SetupPacketReceive (Ipv4Address addr, Ptr<Node> node)
{
  TypeId tid = TypeId::LookupByName ("ns3::UdpSocketFactory");
  Ptr<Socket> sink = Socket::CreateSocket (node, tid);
  InetSocketAddress local = InetSocketAddress (addr, port);
  sink->Bind (local);
  sink->SetRecvCallback (MakeCallback (&RoutingExperiment::ReceivePacket, this));

  return sink;
}

std::string
RoutingExperiment::CommandSetup (int argc, char **argv)
{
  CommandLine cmd (__FILE__);
  cmd.AddValue ("CSVfileName", "The name of the CSV output file name", m_CSVfileName);
  cmd.AddValue ("traceMobility", "Enable mobility tracing", m_traceMobility);
  cmd.AddValue ("protocol", "1=OLSR;2=AODV;3=DSDV;4=DSR", m_protocol);
  cmd.Parse (argc, argv);
  return m_CSVfileName;
}

int
main (int argc, char *argv[])
{
  RoutingExperiment experiment;
  std::string CSVfileName = experiment.CommandSetup (argc,argv);

  //blank out the last output file and write the column headers
  std::ofstream out (CSVfileName.c_str ());
  out << "SimulationSecond," <<
  "ReceiveRate," <<
  "PacketsReceived," <<
  "NumberOfSinks," <<
  "RoutingProtocol," <<
  "TransmissionPower" <<
  std::endl;
  out.close ();

  int nSinks = 40;
  double txp = 7.5;

  experiment.Run (nSinks, txp, CSVfileName);
}

void
RoutingExperiment::Run (int nSinks, double txp, std::string CSVfileName)
{
  Packet::EnablePrinting ();
  m_nSinks = nSinks;
  m_txp = txp;
  m_CSVfileName = CSVfileName;

  int nWifis = 100;

   //saikat
  double TotalTime = 100.0;
  std::string rate ("2048bps");
  std::string phyMode ("DsssRate11Mbps");
  std::string tr_name ("TaskA_1");
  int nodeSpeed = 0; //in m/s
  int nodePause = 0; //in s
  m_protocolName = "protocol";

  Config::SetDefault  ("ns3::OnOffApplication::PacketSize",StringValue ("64")); 
  Config::SetDefault ("ns3::OnOffApplication::DataRate",  StringValue (rate));

  //Set Non-unicastMode rate to unicast mode
  Config::SetDefault ("ns3::WifiRemoteStationManager::NonUnicastMode",StringValue (phyMode));

  NodeContainer adhocNodes;
  adhocNodes.Create (nWifis);

  // setting up wifi phy and channel using helpers
  WifiHelper wifi;
  wifi.SetStandard (WIFI_STANDARD_80211b);

// channel   

  YansWifiPhyHelper wifiPhy;
  YansWifiChannelHelper wifiChannel = YansWifiChannelHelper::Default();;
  Config::SetDefault( "ns3::RangePropagationLossModel::MaxRange", DoubleValue(200) ); // chuti
   wifiChannel.SetPropagationDelay ("ns3::ConstantSpeedPropagationDelayModel");
    wifiChannel.AddPropagationLoss ("ns3::RangePropagationLossModel");

  wifiPhy.SetChannel (wifiChannel.Create ());

  // Add a mac and disable rate control
  WifiMacHelper wifiMac;
  wifi.SetRemoteStationManager ("ns3::ConstantRateWifiManager",
                                "DataMode",StringValue (phyMode),
                                "ControlMode",StringValue (phyMode));

  wifiPhy.Set ("TxPowerStart",DoubleValue (txp));
  wifiPhy.Set ("TxPowerEnd", DoubleValue (txp));

  wifiMac.SetType ("ns3::AdhocWifiMac");
  NetDeviceContainer adhocDevices = wifi.Install (wifiPhy, wifiMac, adhocNodes);

 MobilityHelper mobilityAdhoc;

	Ptr<ListPositionAllocator> pos = CreateObject<ListPositionAllocator>();
	pos->Add( Vector(0, 0, 0 ));
	pos->Add( Vector(209.9, 0, 0) );
  mobilityAdhoc.SetPositionAllocator (pos);
  mobilityAdhoc.SetMobilityModel ("ns3::ConstantPositionMobilityModel");

  mobilityAdhoc.Install (adhocNodes);
 
     
  AodvHelper aodv;
  OlsrHelper olsr;
  DsdvHelper dsdv;
  DsrHelper dsr;
  DsrMainHelper dsrMain;
  Ipv4ListRoutingHelper list;
  InternetStackHelper internet;

  switch (m_protocol)
    {
    case 1:
      list.Add (olsr, 100);
      m_protocolName = "OLSR";
      break;
    case 2:
      list.Add (aodv, 100);
      m_protocolName = "AODV";
      break;
    case 3:
      list.Add (dsdv, 100);
      m_protocolName = "DSDV";
      break;
    case 4:
      m_protocolName = "DSR";
      break;
    default:
      NS_FATAL_ERROR ("No such protocol:" << m_protocol);
    }

  if (m_protocol < 4)
    {
      internet.SetRoutingHelper (list);
      internet.Install (adhocNodes);
    }
  else if (m_protocol == 4)
    {
      internet.Install (adhocNodes);
      dsrMain.Install (dsr, adhocNodes);
    }

  NS_LOG_INFO ("assigning ip address");

  Ipv4AddressHelper addressAdhoc;
  addressAdhoc.SetBase ("10.1.1.0", "255.255.255.0");
  Ipv4InterfaceContainer adhocInterfaces;
  adhocInterfaces = addressAdhoc.Assign (adhocDevices);

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
  std::ofstream plotData ("Highrate_plot.txt", std::ios::app);

  OnOffHelper onoff1 ("ns3::UdpSocketFactory",Address ());
  onoff1.SetAttribute ("OnTime", StringValue ("ns3::ConstantRandomVariable[Constant=1.0]"));
  onoff1.SetAttribute ("OffTime", StringValue ("ns3::ConstantRandomVariable[Constant=0.0]"));

  for (int i = 0; i < nSinks; i++)
    {
      Ptr<Socket> sink = SetupPacketReceive (adhocInterfaces.GetAddress (i), adhocNodes.Get (i));

      AddressValue remoteAddress (InetSocketAddress (adhocInterfaces.GetAddress (i), port));
      onoff1.SetAttribute ("Remote", remoteAddress);

      Ptr<UniformRandomVariable> var = CreateObject<UniformRandomVariable> ();
      ApplicationContainer temp = onoff1.Install (adhocNodes.Get (i + nSinks));
      temp.Start (Seconds (var->GetValue (80.0,81.0)));
      temp.Stop (Seconds (TotalTime));
    }

  std::stringstream ss;
  ss << nWifis;
  std::string nodes = ss.str ();

  std::stringstream ss2;
  ss2 << nodeSpeed;
  std::string sNodeSpeed = ss2.str ();

  std::stringstream ss3;
  ss3 << nodePause;
  std::string sNodePause = ss3.str ();

  std::stringstream ss4;
  ss4 << rate;
  std::string sRate = ss4.str ();

  
  NS_LOG_INFO ("Run Simulation.");

  CheckThroughput ();

  Simulator::Stop (Seconds (TotalTime));
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
 
  monitor->SerializeToXmlFile ((tr_name + ".flowmon").c_str(), false, false);
  double xx = (((txPacketsum - rxPacketsum) * 100) /txPacketsum);

  plotData<<200<<" ";
  plotData<<((rxBytessum * 8.0) / timeDiff)/1024<<" ";
  plotData<<Delaysum/rxPacketsum<<" ";
  plotData<<((rxPacketsum * 100) /txPacketsum)<<" ";
  plotData<<xx<<"\n";

  Simulator::Destroy ();
}

 