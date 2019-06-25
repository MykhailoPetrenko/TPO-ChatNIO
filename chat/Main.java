/**
 *
 *  @author Petrenko Mykhailo S17006
 *
 */

package chat;


import java.net.InetSocketAddress;

public class Main {
  public static void main(String[] args) {
    try{
      new Thread(() -> new Server(new InetSocketAddress("localhost", 1337))).start();
      new Thread(() -> new Client()).start();
      new Client();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
}
