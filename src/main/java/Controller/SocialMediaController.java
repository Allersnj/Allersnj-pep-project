package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController
{

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController()
    {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI()
    {
        Javalin app = Javalin.create();
        app.post("register", this::registerAccountHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::addMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::updateMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountIDHandler);

        return app;
    }

    /**
     * Handles registration for new accounts. Sends client 200 status and JSON of new account if registration succeeds,
     * otherwise sends 400 status.
     * @param context The context for the HTTP request and response
     */
    private void registerAccountHandler(Context context)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Account account = mapper.readValue(context.body(), Account.class);
            Account registered = accountService.addAccount(account);
            if (registered == null)
            {
                context.status(400);
            }
            else
            {
                context.status(200);
                context.json(registered);
            }
        }
        catch (JsonProcessingException e)
        {
            context.status(400);
        }

    }

    /**
     * Handles logins for existing accounts. Sends client 200 status and JSON of logged in account if login succeeds,
     * otherwise sends 401 status.
     * @param context The context for the HTTP request and response
     */
    private void loginHandler(Context context)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Account account = mapper.readValue(context.body(), Account.class);
            Account loggedIn = accountService.loginAccount(account);
            if (loggedIn == null)
            {
                context.status(401);
            }
            else
            {
                context.status(200);
                context.json(loggedIn);
            }
        }
        catch (JsonProcessingException e)
        {
            context.status(401);
        }
    }

    /**
     * Handles adding a new message. Sends client 200 status and JSON of added message if adding succeeds,
     * otherwise sends 400 status.
     * @param context The context for the HTTP request and response
     */
    private void addMessageHandler(Context context)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Message message = mapper.readValue(context.body(), Message.class);
            Message added = messageService.addMessage(message);
            if (added == null)
            {
                context.status(400);
            }
            else
            {
                context.status(200);
                context.json(added);
            }
        }
        catch (JsonProcessingException e)
        {
            context.status(400);
        }
    }

    /**
     * Handles retrieval of all messages. Always sends client 200 status and JSON array of messages,
     * even if no messages exist.
     * @param context The context for the HTTP request and response
     */
    private void getAllMessagesHandler(Context context)
    {
        context.status(200);
        context.json(messageService.getAllMessages());
    }

    /**
     * Handles retrieval of a message based on the message's ID. Always sends client 200 status,
     * even if no message with the provided ID exists.
     * @param context The context for the HTTP request and response
     */
    private void getMessageByIDHandler(Context context)
    {
        Message message = null;
        try
        {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            message = messageService.getMessageByID(message_id);
        }
        catch (NumberFormatException e)
        {
            // Invalid path parameter
        }
        
        context.status(200);
        if (message != null)
        {
            context.json(message);
        }
    }

    /**
     * Handles deletion of a message based on the message's ID. Always sends client 200 status,
     * even if no message with the provided ID exists.
     * @param context The context for the HTTP request and response
     */
    private void deleteMessageByIDHandler(Context context)
    {
        Message deleted = null;
        try
        {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            deleted = messageService.deleteMessageByID(message_id);
        }
        catch (NumberFormatException e)
        {
            // Invalid path parameter
        }
        
        context.status(200);
        if (deleted != null)
        {
            context.json(deleted);
        }
    }

    /**
     * Handles updating a message based on the message's ID. Sends client 200 status and JSON of
     * updated message if the update succeeds, otherwise sends 400 status.
     * @param context The context for the HTTP request and response
     */
    private void updateMessageByIDHandler(Context context)
    {
        Message updated = null;
        try
        {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                Message message = mapper.readValue(context.body(), Message.class);
                updated = messageService.updateMessageByID(message_id, message);
                if (updated == null)
                {
                    context.status(400);
                }
                else
                {
                    context.status(200);
                    context.json(updated);
                }
            }
            catch (JsonProcessingException e)
            {
                context.status(400);
            }
        }
        catch (NumberFormatException e)
        {
            // Invalid path parameter
            context.status(400);
        }
    }

    /**
     * Handles retrieval of all messages sent by a single account based on the account's ID.
     * Always sends client 200 status, even if no messages sent by the account exist.
     * @param context The context for the HTTP request and response
     */
    private void getMessagesByAccountIDHandler(Context context)
    {
        context.status(200);
        List<Message> messages = null;
        try
        {
            int account_id = Integer.parseInt(context.pathParam("account_id"));
            messages = messageService.getMessagesByAccountID(account_id);
        }
        catch (NumberFormatException e)
        {
            // Invalid path parameter
            messages = new ArrayList<>();
        }
        finally
        {
            context.json(messages);
        }
    }
}