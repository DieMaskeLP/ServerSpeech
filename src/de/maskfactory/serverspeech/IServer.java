package de.maskfactory.serverspeech;

import de.maskfac.ServerSDK.server.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//Created by Frederic | DieMaskeLP at 14.03.2020, 19:30
public class IServer {

    private static Server server;

    public static void main(String[] strings) throws InterruptedException {
        System.out.println("Initializing Server... Please wait!");
        Thread.sleep(1500);
        Events.ServerEvents events = new Events.ServerEvents();
        server = new Server();
        server.registerEvent(events);
        server.startServer();
        server.setConsole(": ", events);
    }



}
