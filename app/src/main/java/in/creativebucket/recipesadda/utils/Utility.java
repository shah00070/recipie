package in.creativebucket.recipesadda.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

/**
 * Created by chandan on 10/1/16.
 */
public class Utility {

    private Activity activity;

    public Utility() {

    }

    public Utility(Activity activity) {
        this.activity = activity;
    }

    public String getUserEmailId() {

        AccountManager accountManager = AccountManager.get(activity);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}
