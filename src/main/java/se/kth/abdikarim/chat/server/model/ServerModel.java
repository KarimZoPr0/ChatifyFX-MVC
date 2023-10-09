package se.kth.abdikarim.chat.server.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerModel
{
    private int numOfClients;
    private final ArrayList<DataInputStream> fromClients = new ArrayList<>(  );
    private final ArrayList<DataOutputStream> toClients = new ArrayList<>(  );
    private ServerSocket serverSocket = null;
    public ServerModel()
    {
        initServer();
    }

    public void initServer()
    {
        try
        {
            serverSocket = new ServerSocket( 8000 );
        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public int getNumOfClients( )
    {
        return numOfClients;
    }

    public int incrementNumOfClients( )
    {
        return ++numOfClients;
    }

    public ServerSocket getServerSocket( )
    {
        return serverSocket;
    }

    public DataInputStream getFromClient(int index)
    {
        return fromClients.get( index );
    }


    public void addFromClient(DataInputStream fromClient)
    {
        fromClients.add( fromClient );
    }

    public void addToClient(DataOutputStream toClient)
    {
        toClients.add( toClient );
    }

    public void disconnectClient(int index)
    {
       fromClients.remove( index );
       toClients.remove( index );
       numOfClients--;
    }


    public void broadcastData(int online, String message)
    {
        try
        {
            for ( DataOutputStream toClient : toClients )
            {
                toClient.writeInt( online );
                toClient.writeUTF( message );
            }
        } catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
}
