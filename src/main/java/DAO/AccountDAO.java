package DAO;

import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;

public class AccountDAO
{
    /**
     * Queries the database for an account based on username.
     * @param username The username of the account to get
     * @return The Account with the given username or null if none exists
     */
    public Account getAccountByUsername(String username)
    {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try
        {
            PreparedStatement ps = connection.prepareStatement("select account_id, username, password from Account where username = ?");
            ps.setString(1, username);
            ResultSet result = ps.executeQuery();
            if (result.next())
            {
                account = new Account(result.getInt("account_id"), result.getString("username"), result.getString("password"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return account;
    }

    /**
     * Queries the database for an account based on ID.
     * @param id The ID of the account to get
     * @return The Account with the given ID or null if none exists
     */
    public Account getAccountByID(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try
        {
            PreparedStatement ps = connection.prepareStatement("select account_id, username, password from Account where account_id = ?");
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if (result.next())
            {
                account = new Account(result.getInt("account_id"), result.getString("username"), result.getString("password"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return account;
    }

    /**
     * Adds an account to the database.
     * @param account The account to add
     * @return The added account or null if insertion failed
     */
    public Account addAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        Account addedAccount = null;
        try
        {
            PreparedStatement ps = connection.prepareStatement("insert into Account(username, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet generatedAccountIDSet = ps.getGeneratedKeys();
            if (generatedAccountIDSet.next())
            {
                int generatedAccountID = (int) generatedAccountIDSet.getLong(1);
                addedAccount = new Account(generatedAccountID, account.getUsername(), account.getPassword());
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return addedAccount;
    }
}
