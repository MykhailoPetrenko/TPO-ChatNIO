/**
 *
 *  @author Petrenko Mykhailo S17006
 *
 */

package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
  private String nick;
  private SocketChannel channel;

  public Client(){
    new Login();
  }

  public Client(String nick){
    this.nick=nick;
    try {
      channel = SocketChannel.open();
      channel.connect(new InetSocketAddress("localhost", 1337));
    } catch (IOException e) {
      e.printStackTrace();
    }
    start();
  }
  public void start(){

    MainWindow window = new MainWindow(this);
    while(true){
      String message= ClientSession.read(channel);
      if(message.length()>0)
        window.writeText(message);
    }
  }
  public void sendMessage(String text){
    ClientSession.write(nick + ":  " + text , channel);
  }

  public String getNick() {
    return nick;
  }

  public static void main(String[] args) {
    new Client();
  }
}
