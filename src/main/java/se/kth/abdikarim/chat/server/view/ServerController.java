package se.kth.abdikarim.chat.server.view;

import javafx.application.Platform;
import se.kth.abdikarim.chat.server.model.ServerModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ServerController
{
    private final ServerModel model;
    private final ServerView view;
    private boolean running = true;


    public ServerController( ServerModel model, ServerView view )
    {
        this.model = model;
        this.view = view;

        establishClientConnection( );
    }

    public void establishClientConnection( )
    {
        StringBuilder sb = new StringBuilder(  );
        new Thread( ( ) ->
        {
            try
            {
                while ( running  )
                {
                    Socket socket = model.getServerSocket( ).accept( );
                    int numOfClients = model.incrementNumOfClients( );
                    InetAddress inetAddress = socket.getInetAddress( );

                    new Thread( () -> {

                        sb.append( "Starting thread for client " ).append( numOfClients ).append( " at " ).append( new Date( ) ).append( '\n' );

                        // Blocking! We don't want to block listenThread or the worker thread(JavaFX)
                        sb.append( "Client " ).append( numOfClients ).append( "'s host name is " ).append( inetAddress.getHostName( ) ).append( '\n' );
                        sb.append( "Client " ).append( numOfClients ).append( "'s IP Address is " ).append( inetAddress.getHostAddress( ) ).append( '\n' );

                        Platform.runLater( ( ) -> view.appendToTextArea( sb.toString() ) );
                    } ).start();

                    var fromClient = new DataInputStream( socket.getInputStream( ) );
                    var toClient = new DataOutputStream( socket.getOutputStream( ) );

                    model.addFromClient( fromClient );
                    model.addToClient( toClient );

                    model.broadcastData( model.getNumOfClients( ), "" );

                    new Thread( new HandleAClient( model.getNumOfClients( ) ) ).start();
                }

                System.out.println( "Ended server" );

            } catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        } ).start();
    }


    public void closeServer()
    {
        running = false;
    }

    class HandleAClient implements Runnable
    {
        private final int id;
        private boolean isDisconnected = false;

        public HandleAClient( int clientNo )
        {
            this.id = clientNo;
        }

        @Override
        public void run( )
        {
            DataInputStream fromClient =  model.getFromClient( model.getNumOfClients( ) - 1 );
            try
            {
                while ( !isDisconnected )
                {

                    String utf = fromClient.readUTF( );
                    isDisconnected = utf.equals( ":exit" );
                    String message = utf.trim( );

                    if ( isDisconnected ) model.disconnectClient( model.getNumOfClients( ) - 1 );
                    model.broadcastData( model.getNumOfClients( ), !isDisconnected ? message : "" );

                    Platform.runLater( ( ) ->
                    {
                        if ( isDisconnected )
                        {
                            view.appendToTextArea( "Client " + id + " Disconnected!" + '\n' );
                        }
                    } );
                }
                System.out.println( "Stopped thread for client: " +  id);
            } catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        }
    }
}
