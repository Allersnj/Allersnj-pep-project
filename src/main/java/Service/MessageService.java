package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService
{
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService()
    {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO)
    {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * Uses the MessageDAO to add a new message if the message meets these requirements:
     *  * The message text is not blank
     *  * The message is shorter than 255 characters
     *  * The account posting the message exists
     * @param message The message to add
     * @return The added message or null if adding failed
     */
    public Message addMessage(Message message)
    {
        Message added = null;
        if (messageTextIsValid(message.getMessage_text()) && accountDAO.getAccountByID(message.getPosted_by()) != null)
        {
            added = messageDAO.addMessage(message);
        }

        return added;
    }

    /**
     * Uses the MessageDAO to get a list of all messages that currently exist.
     * @return The list of currently existing Messages
     */
    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    /**
     * Uses the MessageDAO to get a message based on the message's ID.
     * @param message_id The ID of the message to get
     * @return The message returned by the MessageDAO
     */
    public Message getMessageByID(int message_id)
    {
        return messageDAO.getMessageByID(message_id);
    }

    /**
     * Uses the MessageDAO to delete a message based on the message's ID.
     * @param message_id The ID of the message to delete
     * @return The deleted message or null if deletion failed
     */
    public Message deleteMessageByID(int message_id)
    {
        Message deleted = getMessageByID(message_id);
        if (deleted != null)
        {
            messageDAO.deleteMessageByID(message_id);
        }
        
        return deleted;
    }

    /**
     * Uses the MessageDAO to update a message with new text based on the message's ID.
     * Updated text must meet the same requirements as newly inserted message text.
     * @param message_id The ID of the message to update
     * @param message The message with updated text to use
     * @return The updated message or null if updating failed
     */
    public Message updateMessageByID(int message_id, Message message)
    {
        Message updated = null;
        if (messageTextIsValid(message.getMessage_text()) && getMessageByID(message_id) != null)
        {
            messageDAO.updateMessageByID(message_id, message.getMessage_text());
            updated = getMessageByID(message_id);
        }

        return updated;
    }

    /**
     * Uses the MessageDAO to get a list of all messages posted by a single account based on the account's ID.
     * @param account_id The id of the account that posted the messages to get
     * @return The list of messages posted by the account with the given ID.
     */
    public List<Message> getMessagesByAccountID(int account_id)
    {
        return messageDAO.getMessagesByAccountID(account_id);
    }

    /**
     * Validates that message text meets the requirements of not being blank and being shorter than 255 characters.
     * @param message_text The message text to validate
     * @return True if requirements are met, false otherwise
     */
    private boolean messageTextIsValid(String message_text)
    {
        return !message_text.isBlank() && message_text.length() < 255;
    }
}
