package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO
{
    /**
     * Adds a message to the database.
     * @param message The message to add
     * @return The added message or null if insertion failed
     */
    public Message addMessage(Message message)
    {
        Connection connection = ConnectionUtil.getConnection();
        Message added = null;
        try
        {
            PreparedStatement ps = connection.prepareStatement("insert into Message(posted_by, message_text, time_posted_epoch) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet generatedIDSet = ps.getGeneratedKeys();
            if (generatedIDSet.next())
            {
                int generatedID = (int) generatedIDSet.getLong(1);
                added = new Message(generatedID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return added;
    }

    /**
     * Retrieves all messages in the database.
     * @return A list of all messages that currently exist in the database
     */
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try
        {
            PreparedStatement ps = connection.prepareStatement("select message_id, posted_by, message_text, time_posted_epoch from Message");
            ResultSet results = ps.executeQuery();
            while (results.next())
            {
                int message_id = results.getInt("message_id");
                int posted_by = results.getInt("posted_by");
                String message_text = results.getString("message_text");
                long time_posted_epoch = results.getLong("time_posted_epoch");
                messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return messages;
    }
    
    /**
     * Queries the database for a message based on the message's ID.
     * @param message_id The ID of the message to get
     * @return The message with the given ID or null if none exists
     */
    public Message getMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try
        {
            PreparedStatement ps = connection.prepareStatement("select message_id, posted_by, message_text, time_posted_epoch from Message where message_id = ?");
            ps.setInt(1, message_id);
            ResultSet result = ps.executeQuery();
            if (result.next())
            {
                int id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted_epoch = result.getLong("time_posted_epoch");

                message = new Message(id, posted_by, message_text, time_posted_epoch);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * Deletes a message from the database based on the message's ID.
     * @param message_id The ID of the message to delete
     */
    public void deleteMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement("delete from Message where message_id = ?");
            ps.setInt(1, message_id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the text of a message in the database based on the message's ID.
     * @param message_id The ID of the message to update
     * @param message_text The updated text
     */
    public void updateMessageByID(int message_id, String message_text)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement("update Message set message_text = ? where message_id = ?");
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Queries the database for messages based on the account that posted them.
     * @param account_id The ID of the account that posted the messages to get
     * @return A list of messages posted by the account with the given ID
     */
    public List<Message> getMessagesByAccountID(int account_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try
        {
            PreparedStatement ps = connection.prepareStatement("select message_id, posted_by, message_text, time_posted_epoch from Message where posted_by = ?");
            ps.setInt(1, account_id);
            ResultSet results = ps.executeQuery();

            while (results.next())
            {
                int message_id = results.getInt("message_id");
                int posted_by = results.getInt("posted_by");
                String message_text = results.getString("message_text");
                long time_posted_epoch = results.getLong("time_posted_epoch");
                messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return messages;
    }
}
