package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    /**
     * Uses the AccountDAO to get an account based on the account's username.
     * @param username The username of the Account to get
     * @return The account returned by the AccountDAO
     */
    public Account getAccountByUsername(String username)
    {
        return accountDAO.getAccountByUsername(username);
    }

    /**
     * Uses the AccountDAO to add a new account if the account meets these requirements:
     *  * username is not empty
     *  * password is at least 4 characters
     *  * username does not already exist
     * @param account The account to add
     * @return The added account or null if adding failed
     */
    public Account addAccount(Account account)
    {
        Account added = null;
        
        if (!account.getUsername().isEmpty() && account.getPassword().length() >= 4 && accountDAO.getAccountByUsername(account.getUsername()) == null)
        {
            added = accountDAO.addAccount(account);
        }

        return added;
    }

    /**
     * Uses the AccountDAO to log in a user if the provided account's credentials match an existing account
     * @param account The account with credentials to check
     * @return The account with matching username and password or null if credentials don't match
     */
    public Account loginAccount(Account account)
    {
        Account loggedIn = null;
        Account matchedUsername = accountDAO.getAccountByUsername(account.getUsername());
        if (matchedUsername != null && matchedUsername.getPassword().equals(account.getPassword()))
        {
            loggedIn = matchedUsername;
        }

        return loggedIn;
    }
}
