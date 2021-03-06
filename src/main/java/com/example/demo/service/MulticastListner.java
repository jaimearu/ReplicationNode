package com.example.demo.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.io.IOException;
import java.util.ArrayList;

public class MulticastListner implements Runnable {

    RestNodeService nodeService;


    public MulticastListner(RestNodeService temp) throws UnknownHostException {
        nodeService = temp;
    }


    private ArrayList<String> getNameAndIp(String msg) throws IOException, InterruptedException {
        ArrayList<String> temp = new ArrayList<>();
        if (msg.contains("newNode")) {
            String haha = msg.replace("newNode ", "");
            if (!haha.isEmpty()) {
                String[] tokens = haha.split("::");
                for (String t : tokens)
                    temp.add(t);
            }
        if (nodeService.setupb) {
            if (nodeService.first) {
                System.out.println("Second node present");
                Thread.sleep(500);
                //Hier rest shit set previous
                //sendUDPMessage("previous " + name + "::ip " + thisIp, temp.get(1), 10000);
                URL connection = new URL("http://" + temp.get(1) + ":9000/SetPrevious?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                //Mogenlijk Zo reply opvangen?
                connection.openConnection().getInputStream();
                //
                //sendUDPMessage("next " + name + "::ip " + thisIp, temp.get(1), 10000);
                URL connection2 = new URL("http://" + temp.get(1) + ":9000/SetNext?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                //
                connection2.openConnection().getInputStream();
                //
                if(nodeService.isHoogste)
                if (nodeService.hashfunction(temp.get(0),true) > nodeService.hashfunction(nodeService.name,true)) {
                    System.out.println(temp.get(0)+" is the new highest node");
                    nodeService.isHoogste = false;
                    URL connection5 = new URL("http://" + temp.get(1) + ":9000/IsHighest?Highest=true");
                    connection5.openConnection().getInputStream();
                    //
                    /*
                    Thread.sleep(500);
                    URL connection3 = new URL("http://" + temp.get(1) + ":9000/SetPrevious?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                    connection3.openConnection().getInputStream();
                    URL connection4 = new URL("http://" + temp.get(1) + ":9000/SetNext?name=" + nodeService.next + "&ip=" + nodeService.nextIP);
                    connection4.openConnection().getInputStream();
                    URL connection5 = new URL("http://" + temp.get(1) + ":9000/IsHighest?Highest=true");
                    connection5.openConnection().getInputStream();
                    System.out.println(temp.get(0)+" is nu de hoogst gehashte node");
                    //
                    System.out.println(temp.get(0)+" is nu de hoogst gehashte node");

                     */
                }
                if(nodeService.isLaagste)
                    if (nodeService.hashfunction(temp.get(0),true) < nodeService.hashfunction(nodeService.name,true)) {
                        System.out.println(temp.get(0)+" is the new lowest node");
                        nodeService.isLaagste = false;
                        URL connection5 = new URL("http://" + temp.get(1) + ":9000/IsLowest?Lowest=true");
                        connection5.openConnection().getInputStream();
                    }
                nodeService.next = temp.get(0);
                nodeService.nextIP = temp.get(1);
                nodeService.previous = temp.get(0);
                nodeService.previousIP = temp.get(1);
                System.out.println("Next node is " + nodeService.next + " " + nodeService.nextIP);
                System.out.println("Previous node is " + nodeService.previous + " " + nodeService.previousIP);
                nodeService.first = false;
            } else {
                if(nodeService.isHoogste)
                    if (nodeService.hashfunction(temp.get(0),true) > nodeService.hashfunction(nodeService.name,true)) {
                        System.out.println(temp.get(0)+" is the new highest node");
                        nodeService.isHoogste = false;

                        Thread.sleep(500);
                        URL connection = new URL("http://" + temp.get(1) + ":9000/SetPrevious?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                        connection.openConnection().getInputStream();
                        URL connection2 = new URL("http://" + temp.get(1) + ":9000/SetNext?name=" + nodeService.next + "&ip=" + nodeService.nextIP);
                        connection2.openConnection().getInputStream();
                        URL connection3 = new URL("http://" + temp.get(1) + ":9000/IsHighest?Highest=true");
                        connection3.openConnection().getInputStream();
                        nodeService.next = temp.get(0);
                        nodeService.nextIP = temp.get(1);
                        System.out.println("My new next is  "+nodeService.next+" "+nodeService.nextIP);
                    }
                if(nodeService.isLaagste)
                    if (nodeService.hashfunction(temp.get(0),true) < nodeService.hashfunction(nodeService.name,true)) {
                        System.out.println(temp.get(0)+" is the new lowest node");
                        nodeService.isLaagste = false;


                        Thread.sleep(500);
                        URL connection = new URL("http://" + temp.get(1) + ":9000/SetNext?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                        connection.openConnection().getInputStream();
                        URL connection2 = new URL("http://" + temp.get(1) + ":9000/SetPrevious?name=" + nodeService.previous + "&ip=" + nodeService.previousIP);
                        connection2.openConnection().getInputStream();
                        URL connection3 = new URL("http://" + temp.get(1) + ":9000/IsLowest?Lowest=true");
                        connection3.openConnection().getInputStream();
                        nodeService.previous = temp.get(0);
                        nodeService.previousIP = temp.get(1);
                        System.out.println("My new previous is "+nodeService.previous+" "+nodeService.previousIP);
                    }

                if (nodeService.hashfunction(nodeService.name, true) < nodeService.hashfunction(temp.get(0), true) && nodeService.hashfunction(temp.get(0), true) < nodeService.hashfunction(nodeService.next, true)) {
                    Thread.sleep(500);
                    URL connection = new URL("http://" + temp.get(1) + ":9000/SetPrevious?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                    //
                    connection.openConnection().getInputStream();
                    //
                    nodeService.next = temp.get(0);
                    nodeService.nextIP = temp.get(1);
                    System.out.println("My new next is  " + nodeService.next + " " + nodeService.nextIP);
                    System.out.println("My new previous is " + nodeService.previous + " " + nodeService.previousIP);
                }
                if (nodeService.hashfunction(nodeService.previous, true) < nodeService.hashfunction(temp.get(0), true) && nodeService.hashfunction(temp.get(0), true) < nodeService.hashfunction(nodeService.name, true)) {
                    Thread.sleep(500);
                    URL connection2 = new URL("http://" + temp.get(1) + ":9000/SetNext?name=" + nodeService.name + "&ip=" + nodeService.thisIp);
                    connection2.openConnection().getInputStream();
                    //haha
                    nodeService.previous = temp.get(0);
                    nodeService.previousIP = temp.get(1);
                    System.out.println("My new next is  " + nodeService.next + " " + nodeService.nextIP);
                    System.out.println("My new previous is " + nodeService.previous + " " + nodeService.previousIP);
                }
            }
        }
        return temp;
    }
        return null;
    }
    public void receiveUDPMessage(String ip, int port) throws
            IOException, InterruptedException {
        byte[] buffer = new byte[1024];
        MulticastSocket socket = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        while (true) {
            //System.out.println("Waiting for multicast message...");
            DatagramPacket packet = new DatagramPacket(buffer,
                    buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(),
                    packet.getOffset(), packet.getLength());
            System.out.println("Ik receive multicast "+msg);
            if (msg.contains("nodeCount"))
                nodeService.setUp(msg);
            if (msg.contains("newNode"))
                getNameAndIp(msg);

            //Dees Ga Ook Weg Moete
            if ("shutdown".equals(msg)) {
                nodeService.shutdown();
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
    }

    @Override
    public void run() {
        try {
            receiveUDPMessage("230.0.0.0", 10000);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

