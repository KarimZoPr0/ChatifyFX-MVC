package se.kth.abdikarim.chat.server.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ServerView extends ScrollPane
{
    private final TextArea textArea;

    public ServerView(  )
    {
        super();
        textArea = new TextArea(  );
        setContent( textArea );
    }


    public void appendTextToTextArea( String text )
    {
        textArea.appendText( text );
    }
}
