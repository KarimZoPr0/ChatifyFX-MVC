package se.kth.abdikarim.chat.client.view;

import javafx.application.Platform;
import se.kth.abdikarim.chat.client.IClientEventHandler;
import se.kth.abdikarim.chat.client.model.ClientModel;

public class ClientController implements IClientEventHandler
{
    private final ClientModel model;
    private final ClientView view;
    private Thread listenerThread;

    public ClientController( ClientModel model, ClientView view)
    {
        this.model = model;
        this.view = view;

        view.setEventHandler( this );
        handleListenToServer();
    }

    public void handleListenToServer()
    {
        listenerThread = new Thread( ( ) ->
        {
            while ( !listenerThread.isInterrupted() )
            {
                int online = model.readOnline( );
                String message = model.readMessage( );

                Platform.runLater( ( ) ->
                {
                    if ( !message.isEmpty( ) )
                    {
                        view.appendTextArea( message + '\n' );
                        view.setMessageFieldText( "" );
                    }

                    view.setOnlineLabel( "Online: " + online );
                } );
            }
            System.out.println("Thread has exited the loop");
        } );
        listenerThread.start();
    }
    @Override
    public void handleCloseRequest( )
    {
        listenerThread.interrupt();
        model.transmitMessage(":exit");
        System.out.println( "Is thread Alive:" + listenerThread.isAlive() );
        Platform.exit();
    }

    @Override
    public void handleMessageSend( )
    {
        model.transmitMessage( view.getMessage( ) );
    }
}
