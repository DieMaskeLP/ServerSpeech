package de.maskfactory.serverspeech;

import de.maskfac.ServerSDK.client.Client;
import de.maskfac.ServerSDK.server.Server;
import net.labymod.opus.OpusCodec;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;

//Created by Frederic | DieMaskeLP at 14.03.2020, 19:49
public class IClient {

    static Client client;
    static OpusCodec codec;
    static AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
    public static SourceDataLine speaker;
    static TargetDataLine microphone;

    public static void main(String[] args) throws IOException, LineUnavailableException {
        OpusCodec.loadNativesFromJar();

        codec = OpusCodec.createDefault();
        client = new Client();
        client.registerEvent(new Events.ClientEvents());
        speaker = AudioSystem.getSourceDataLine(audioFormat);
        microphone = AudioSystem.getTargetDataLine(audioFormat);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Address: ");
        String address = "";
        int port = Server.getDefaultPort();
        String[] aim = reader.readLine().split(":");
        if (aim.length == 0){
            address = "127.0.0.1";
        } else address = aim[0];
        if (aim.length == 2) port = Integer.parseInt(aim[1]);
        SocketAddress socketAddress = new InetSocketAddress(address, port);
        client.connectToServer(socketAddress);
        speaker.open(audioFormat);
        speaker.start();
        startSending();
    }

    static void startSending() throws LineUnavailableException {
        microphone.open(audioFormat);
        microphone.start();
        while (client.isConnected() && !client.isClosed()){
            byte[] data = new byte[codec.getChannels() * codec.getFrameSize() * 2];
            microphone.read(data, 0, data.length);

            byte[] encoded = codec.encodeFrame(data);
            client.sendData(Utils.encode(encoded));
        }
    }

}
