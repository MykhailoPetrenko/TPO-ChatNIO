/**
 *
 *  @author Petrenko Mykhailo S17006
 *
 */

package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

  private ServerSocketChannel ccs;
  private Selector selector;
  private boolean serverIsRunning = true;

  public Server(InetSocketAddress address) {
    try {
      ccs = ServerSocketChannel.open();
      ccs.configureBlocking(false);
      ccs.socket().bind(address);
      selector = Selector.open();
      ccs.register(selector, SelectionKey.OP_ACCEPT);


    } catch (IOException e) {
      e.printStackTrace();
    }
    loop();
  }

  private void loop() {
    while(serverIsRunning){
      {
        try{
          selector.select();
          Set keys = selector.selectedKeys();
          Iterator iter = keys.iterator();
          while(iter.hasNext()){
            SelectionKey key = (SelectionKey) iter.next();
            // iter.remove();
            // if(!key.isValid()) continue;
            if(key.isAcceptable()){
              SocketChannel ccc = ccs.accept();
              if(ccc==null) continue;
              ccc.configureBlocking(false);
              ccc.register(selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE));
              continue;
            }
            if(key.isReadable()){
              SocketChannel cc = (SocketChannel) key.channel();
              if(cc==null) continue;
              String message = ClientSession.read(cc);
              if(message.length()>0)
                writeToChat(message);

            }
          }


        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void writeToChat(String message) {
    try{
      selector.select();
      Set keys = selector.selectedKeys();
      Iterator iter = keys.iterator();
      while(iter.hasNext()){
        SelectionKey key = (SelectionKey) iter.next();
        //   iter.remove();
        if(key.isWritable()){
          SocketChannel channel = (SocketChannel) key.channel();
          ClientSession.write(message,channel);
        }
      }


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new Server(new InetSocketAddress("localhost", 1337));
  }
}
