package se.kth.abdikarim.chat.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se.kth.abdikarim.chat.client.IClientEventHandler;

public class ClientView extends BorderPane
{
    private IClientEventHandler eventHandler;
    private TextField messageField;
    private TextField nameField;
    private Label onlineLabel;
    private TextArea textArea;

    public ClientView( )
    {
        super();
        initChatView( );
        addEventHandlers( );
    }

    private void initChatView( )
    {
        // Pane for name
        HBox paneForName = new HBox( 15 );
        paneForName.setPadding( new Insets( 5, 5, 5, 5 ) );
        paneForName.getChildren( ).add( new Label( "Name :" ) );

        // Name Text Field
        nameField = new TextField( );
        nameField.setAlignment( Pos.BOTTOM_LEFT );
        paneForName.getChildren( ).add( nameField );

        // Pane for Message
        HBox paneMessage = new HBox( );
        paneMessage.setPadding( new Insets( 5, 5, 5, 5 ) );
        paneMessage.getChildren( ).add( new Label( "Message: " ) );

        // Message Text Field
        messageField = new TextField( );
        messageField.setAlignment( Pos.BOTTOM_LEFT );
        paneMessage.getChildren( ).add( messageField );

        // Online status
        onlineLabel = new Label( "Online: 0" );
        onlineLabel.setPadding( new Insets( 5, 5, 10, 5 ) );

        // Pane for Name and Message
        var pane = new VBox( );
        pane.getChildren( ).add( onlineLabel );
        pane.getChildren( ).add( paneForName );
        pane.getChildren( ).add( paneMessage );

        // Text Area
        textArea = new TextArea( );

        // Main Pane
        this.setCenter( new ScrollPane( textArea ) );
        this.setTop( pane );
    }

    public void setEventHandler( IClientEventHandler clientEventHandler )
    {
        this.eventHandler = clientEventHandler;
    }
    public void addEventHandlers(  )
    {
        messageField.setOnAction( e -> eventHandler.handleMessageSend() );
    }

    public TextField getMessageField( )
    {
        return messageField;
    }

    public TextField getNameField( )
    {
        return nameField;
    }


    public void appendTextArea( String text )
    {
        textArea.appendText( text );
    }

    public void setOnlineLabel( String text )
    {
        onlineLabel.setText( text );
    }

    public void setMessageFieldText( String text )
    {
        messageField.setText( text );
    }


    public String getMessage( )
    {
        return getNameField( ).getText( ) + ": " + getMessageField( ).getText( );
    }

}
