package com.example.demo.controller;
import com.example.demo.service.FileChecker;
import com.example.demo.service.MulticastListner;
import com.example.demo.service.RestNodeService;
import com.example.demo.service.TCPListner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class NodeController {
    RestNodeService nodeService;
    ExecutorService threadPool = Executors.newFixedThreadPool(5);
    public NodeController() throws IOException {
    }

    @PostConstruct
    public void init() throws IOException {
        nodeService = new RestNodeService();
        threadPool.execute(new MulticastListner(nodeService));
        threadPool.execute(new FileChecker(nodeService));
    }


    @GetMapping("/SetNameServer")
    public String setNameServer (@RequestParam(value = "ip", defaultValue = "omo") String ip) throws IOException {
        if (!ip.equals("omo")) {
            System.out.println("running /SetNameServer, ip "+ip);
            nodeService.addToNameServer(ip);
            return "node  with ip address "+ip+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/SetNext")
    public String setNext (@RequestParam(value = "name", defaultValue = "omo") String name,@RequestParam(value = "ip", defaultValue = "omo") String ip) throws IOException {
        if (!name.equals("omo") && !ip.equals("omo")) {
            System.out.println("Running /SetNext, name "+name+" ip "+ip);
            nodeService.next(name, ip);
            return "node "+name+" with ip address "+ip+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/SetPrevious")
    public String setPrevious (@RequestParam(value = "name", defaultValue = "omo") String name,@RequestParam(value = "ip", defaultValue = "omo") String ip) throws IOException {
        if (!name.equals("omo") && !ip.equals("omo")) {
            System.out.println("Running /SetPrevious, name "+name+" ip "+ip);
            nodeService.previous(name, ip);
            return "node "+name+" with ip address "+ip+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/GetReplicationFile")
    public String getReplicationFile (@RequestParam(value = "name", defaultValue = "omo") String name,@RequestParam(value = "ownerIP", defaultValue = "omo") String ip) throws IOException, InterruptedException {
        if (!name.equals("omo") && !ip.equals("omo")) {
            System.out.println("Running /GetReplicatedFile, name "+name+" ownerIP "+ip);
            nodeService.recieveTCP(ip,name);
            return "node "+name+" with ip address "+ip+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/TransferReplicatedFile")
    public String transferReplicatedFile (@RequestParam(value = "name", defaultValue = "omo") String name) throws IOException {
        if (!name.equals("omo")) {
            System.out.println("Running /TransferReplicatedFile, name "+name+" ownerIP ");
            threadPool.execute(new TCPListner(nodeService,false,name));
            return "node "+name+" with ip address "+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/HostLocalFile")
    public String hostLocalFile (@RequestParam(value = "FileName", defaultValue = "omo") String name) throws IOException {
        if (!name.equals("omo")) {
            System.out.println("Running /HostLocalFile, name "+name);
            //Ga thread moete worrexx
            threadPool.execute( new TCPListner(nodeService,true,name));
            return "node "+name+" with ip address was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/RemoveReplicatedFile")
    public String RemoveReplicatedFile (@RequestParam(value = "File", defaultValue = "omo") String file) throws IOException {
        if (!file.equals("omo")) {
            nodeService.removeReplicatedFile(file);
            return "node "+file+" with ip address "+" was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/IsHighest")
    public String isHighest (@RequestParam(value = "Highest", defaultValue = "omo") String value) throws IOException {
        if (!value.equals("omo")) {
            if(value.equals("true"))
            nodeService.setHighest();
            return "node "+value+" with ip address was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/IsLowest")
    public String isLowest (@RequestParam(value = "Lowest", defaultValue = "omo") String value) throws IOException {
        if (!value.equals("omo")) {
            if (value.equals("true"))
            nodeService.setLowest();
            return "node "+value+" with ip address was succesfully added to the node map";
        }
        else
            return"adding new node failed";
    }
    @GetMapping("/Kill")
    public String kill () throws IOException {
        System.out.println("Ik run nu /kill");
        nodeService.shutdown();
        return "node "+nodeService.name+" with ip address "+nodeService.thisIp+" was succesfully added to the node map";
    }

}