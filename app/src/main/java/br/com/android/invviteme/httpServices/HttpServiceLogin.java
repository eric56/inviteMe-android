package br.com.android.invviteme.httpServices;

import br.com.android.invviteme.constants.ApiCall;
import br.com.android.invviteme.model.LoginModel;
import br.com.android.invviteme.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HttpServiceLogin {

    @POST(ApiCall.PATH_LOGIN)
    Call<User> invokeLogin(@Body LoginModel loginModel);

}
