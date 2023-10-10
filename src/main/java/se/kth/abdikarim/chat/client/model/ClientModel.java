package se.kth.abdikarim.chat.client.model;

import se.kth.abdikarim.chat.server.model.ServerModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientModel
{
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    private Thread listenerThread;

    public ClientModel( )
    {
        connectToServer( );
    }

    public void connectToServer( )
    {
        try
        {
            Socket socket = new Socket( "26.147.72.226", 8000 );
            fromServer = new DataInputStream( socket.getInputStream( ) );
            toServer = new DataOutputStream( socket.getOutputStream( ) );
        } catch ( IOException ex )
        {
            System.out.println( ex );
        }
    }

    public void startListening( Consumer<String> onReceivedMessage, Consumer<Integer> onReceivedOnline)
    {
        listenerThread = new Thread( ( ) ->
        {
            while ( !listenerThread.isInterrupted() )
            {
                var onlineFromServer = readOnline( );
                var messageFromServer = readMessage( );

                if(!messageFromServer.isEmpty()) onReceivedMessage.accept( messageFromServer );
                onReceivedOnline.accept( onlineFromServer );
            }
        } );
        listenerThread.start();
    }

    public void transmitMessage( String message )
    {
        try
        {
            if ( !message.isEmpty( ) )
            {
                toServer.writeUTF( message );
                toServer.flush( );
            }

        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public int readOnline( )
    {
        try
        {
            return fromServer.readInt( );
        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public String readMessage( )
    {
        try
        {
            return fromServer.readUTF( );
        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public void stopListeningToServer( )
    {
        listenerThread.interrupt();
    }
}
