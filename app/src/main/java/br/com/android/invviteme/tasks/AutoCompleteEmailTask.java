package br.com.android.invviteme.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.com.android.invviteme.delegate.AutoCompleteEmailTaskDelegate;

public class AutoCompleteEmailTask extends AsyncTask<Void, Void, List<String>> {

    AutoCompleteEmailTaskDelegate delegate = null;
    Context context;

    public AutoCompleteEmailTask(AutoCompleteEmailTaskDelegate autoCompleteEmailTaskDelegate, Context ctx) {
        delegate = autoCompleteEmailTaskDelegate;
        context = ctx;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        ArrayList<String> emailAddressCollection = new ArrayList<>();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                if (!emailAddressCollection.contains(account.name)) {
                    emailAddressCollection.add(account.name);
                }
            }
        }
        return emailAddressCollection;
    }

    @Override
    protected void onPostExecute(List<String> emailAddressCollection) {
        delegate.setAdapterEmail(emailAddressCollection);
    }

}
