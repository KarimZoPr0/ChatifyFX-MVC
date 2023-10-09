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

    public Thread listenThread;

    public ServerController( ServerModel model, ServerView view )
    {
        this.model = model;
        this.view = view;

        handleServerListen( );
    }

    public void handleServerListen( )
    {
        listenThread = new Thread( ( ) ->
        {
            try
            {
                while ( !listenThread.isInterrupted() )
                {
                    Socket socket = model.getServerSocket( ).accept( );

                    int numOfClients = model.incrementNumOfClients( );

                    InetAddress inetAddress = socket.getInetAddress( );
                    Platform.runLater( ( ) ->
                    {
                        view.appendTextToTextArea( "Starting thread for client " + numOfClients + " at " + new Date( ) + '\n' );
                        view.appendTextToTextArea( "Client " + numOfClients + "'s host name is " + inetAddress.getHostName( ) + '\n' );
                        view.appendTextToTextArea( "Client " + numOfClients + "'s IP Address is " + inetAddress.getHostAddress( ) + '\n' );

                    } );

                    var fromClient = new DataInputStream( socket.getInputStream( ) );
                    var toClient = new DataOutputStream( socket.getOutputStream( ) );

                    model.addFromClient( fromClient );
                    model.addToClient( toClient );

                    model.broadcastData( model.getNumOfClients( ), "" );

                    new Thread( new HandleAClient( model.getNumOfClients( ) ) ).start( );
                }
            } catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        } );

        listenThread.start();
    }


    class HandleAClient implements Runnable
    {
        DataInputStream fromClient;
        private final int id;

        public HandleAClient( int clientNo )
        {
            this.id = clientNo;
            fromClient = model.getFromClient( model.getNumOfClients( ) - 1 );
        }

        @Override
        public void run( )
        {
            try
            {
                while ( !listenThread.isInterrupted() )
                {

                    String utf = fromClient.readUTF( );
                    boolean isConnected = !utf.equals( ":exit" );
                    String message = utf.trim( );

                    if ( !isConnected ) model.disconnectClient( model.getNumOfClients( ) - 1 );
                    model.broadcastData( model.getNumOfClients( ), isConnected ? message : "" );

                    Platform.runLater( ( ) ->
                    {
                        if ( !isConnected )
                        {
                            view.appendTextToTextArea( "Client " + id + " Disconnected!" + '\n' );
                        }
                    } );
                }
            } catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        }
    }
}
