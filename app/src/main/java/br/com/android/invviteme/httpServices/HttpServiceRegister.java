package br.com.android.invviteme.httpServices;

import br.com.android.invviteme.constants.ApiCall;
import br.com.android.invviteme.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HttpServiceRegister {

    @POST(ApiCall.PATH_REGISTER)
    Call<User> invokeRegister(@Body User user);

}
