package se.kth.abdikarim.chat.client.view;

import javafx.application.Platform;
import se.kth.abdikarim.chat.client.model.ClientModel;

public class ClientController
{
    private final ClientModel model;
    private final ClientView view;
    private Thread listenerThread;

    public ClientController( ClientModel model, ClientView view)
    {
        this.model = model;
        this.view = view;

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
        } );
        listenerThread.start();
    }
    public void handleCloseRequest( )
    {
        listenerThread.interrupt();
        model.transmitMessage(":exit");
        Platform.exit();
    }

    public void handleMessageSend( )
    {
        String message = view.getMessage( );
        model.transmitMessage( message );
    }
}
