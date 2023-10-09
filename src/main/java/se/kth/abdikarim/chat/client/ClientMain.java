package se.kth.abdikarim.chat.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.kth.abdikarim.chat.client.model.ClientModel;
import se.kth.abdikarim.chat.client.view.ClientController;
import se.kth.abdikarim.chat.client.view.ClientView;

public class ClientMain extends Application
{
    @Override
    public void start( Stage primaryStage )
    {
        ClientModel model = new ClientModel( );
        ClientView view = new ClientView( );
        ClientController controller = new ClientController( model, view );

        primaryStage.setOnCloseRequest( e -> controller.handleCloseRequest() );

        Scene scene = new Scene( view, 450, 300 );
        primaryStage.setTitle( "Client" );
        primaryStage.setScene( scene );
        primaryStage.show( );
    }
}
