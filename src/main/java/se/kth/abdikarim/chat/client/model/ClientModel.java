package se.kth.abdikarim.chat.client.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientModel
{
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

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
}