import java.net.*;
import java.io.*;

public class GreetingClient
{
   public static void main(String [] args)
   {
      String serverName = "127.0.0.1";
      int port = Integer.parseInt("22");
      int arr [][] = new int[4][2];
      try
      {
         System.out.println("Connecting to " + serverName +
		 " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to " 
		 + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         out.writeObject(arr);
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
         System.out.println("Server says " + in.readUTF());
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}