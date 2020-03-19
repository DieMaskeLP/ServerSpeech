package de.maskfactory.serverspeech;

import com.sun.deploy.util.SessionState;
import de.maskfac.ServerSDK.client.*;
import de.maskfac.ServerSDK.server.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//Created by Frederic | DieMaskeLP at 14.03.2020, 20:01
public class Events {
    static class ServerEvents implements ServerSuccessfulStartEvent, ServerClientConnectEvent, ServerClientDisconnectEvent, ServerDataReceivedEvent, ServerConsoleInputEvent {

        @Override
        public void onServerClientConnect(Client client, Server server) {
            System.out.println("\rClient {" + client.getAddress() + "}: joined the Server.");
            System.out.print(": ");
            server.sendDataToConnectedClients("{$leaveMessage}" + client.getAddress() + " connected to Server!");
        }

        @Override
        public void onServerClientDisconnect(Client client, Server server) {
            System.out.println("\rClient {" + client.getAddress() + "}: left the Server.");
            System.out.print(": ");
            server.sendDataToConnectedClients("{$joinMessage}" + client.getAddress() + " disconnected from Server!");
        }


        //for (Client client1 : server.getConnectedClients()){
          //  if (client != client1){
            //    client1.sendData(s);
            //}
        //}

        @Override
        public void onServerDataReceived(Client client, Server server, String s) {
            for (Client client1 : server.getConnectedClients()){
                if (client != client1){
                    client1.sendData(s);
                    }
                }
        }


            @Override
        public void onServerStarted(Server server) {
            System.out.println("Server started successful!");
            server.setConsoleInputEnabled(true);
        }

        @Override
        public void onServerConsoleInput(Server server, String s) {

            String[] args = s.split(" ");
            switch (args[0]){
                case "stop":
                    server.stopServer();
                    server.setConsoleInputEnabled(false);

                    break;

                case "send":
                    StringBuilder msg = new StringBuilder();
                    if (args.length >= 2){
                        String end = " ";
                        for (int i = 1; i < args.length; i++){
                            if (i == args.length-1){
                                end = "";
                            }
                            msg.append(args[i]).append(end);
                        }
                        System.out.println();
                        System.out.println("Server {127.0.0.1}: sent data: " + msg.toString());
                        server.sendDataToConnectedClients("{$serverMessage}" + msg.toString());
                    } else System.out.println("Please enter a message");
                    break;
            }

        }
    }


    static class ClientEvents implements ClientConnectToServerEvent, ClientDataReceivedEvent, ClientConsoleInputEvent {
        @Override
        public void onClientConnect(Client client) {
            System.out.println("\rConnected to server!");
            System.out.print(": ");
        }

        @Override
        public void onClientConsoleInput(Client client, String s, String[] strings) {

        }

        @Override
        public void onClientDataReceived(Client client, String s) {
            if (s.startsWith("{$joinMessage}")){
                System.out.println(s.replace("{$joinMessage}", ""));
            } else if (s.startsWith("{$leaveMessage}")){
                System.out.println(s.replace("{$leaveMessage}", ""));
            } else if (s.startsWith("{$serverMessage}")){
                System.out.println("Server {" + client.getRemoteSocketAddress() + "}: " + s.replace("{$serverMessage}", ""));
            } else {



                byte[] decodedData = IClient.codec.decodeFrame(Utils.decode(s));
                IClient.speaker.write(decodedData, 0, decodedData.length);
            }
        }

    }

}
