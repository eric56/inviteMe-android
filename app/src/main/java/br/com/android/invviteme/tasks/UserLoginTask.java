package br.com.android.invviteme.tasks;

import android.os.AsyncTask;

import br.com.android.invviteme.delegate.UserLoginDelegate;

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private UserLoginDelegate userLoginDelegate;

    public UserLoginTask(String email, String password, UserLoginDelegate loginDelegate) {
        mEmail = email;
        mPassword = password;
        userLoginDelegate = loginDelegate;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //TODO: Implementar código de login
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        userLoginDelegate.resultLogin(success);
    }

    @Override
    protected void onCancelled() {
        userLoginDelegate.resultLogin(false);
    }
}
