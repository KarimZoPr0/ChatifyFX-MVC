package se.kth.abdikarim.chat.client.view;

import javafx.application.Platform;
import se.kth.abdikarim.chat.client.IClientEventHandler;
import se.kth.abdikarim.chat.client.model.ClientModel;

public class ClientController implements IClientEventHandler
{
    private final ClientModel model;
    private final ClientView view;

    public ClientController( ClientModel model, ClientView view)
    {
        this.model = model;
        this.view = view;

        view.setEventHandler( this );
        handleListenToServer();
    }

    public void handleListenToServer()
    {
        model.startListening(
                message -> Platform.runLater( ( ) -> view.displayMessage( message ) ),
                online -> Platform.runLater( ( ) -> view.setOnlineLabel( "Online: " + online ) ) );
    }
    @Override
    public void handleCloseRequest( )
    {
        model.stopListeningToServer();
        model.transmitMessage(":exit");
        Platform.exit();
    }

    @Override
    public void handleMessageSend( )
    {
        model.transmitMessage( view.getMessage( ) );
    }
}
