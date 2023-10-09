package se.kth.abdikarim.chat.client;

public interface IClientEventHandler
{
    void handleCloseRequest();
    void handleMessageSend();
}
