package se.kth.abdikarim.chat.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.kth.abdikarim.chat.server.model.ServerModel;
import se.kth.abdikarim.chat.server.view.ServerController;
import se.kth.abdikarim.chat.server.view.ServerView;

public class ServerMain extends Application
{

    @Override
    public void start( Stage stage )
    {
        ServerModel model = new ServerModel( );
        ServerView view = new ServerView( );
        ServerController controller = new ServerController( model, view );

        stage.setOnCloseRequest( e -> Platform.exit());

        Scene scene = new Scene( view, 450, 200 );
        stage.setTitle( "MultiThreadServer" );
        stage.setScene( scene );
        stage.show( );
    }
}
