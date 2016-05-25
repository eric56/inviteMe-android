package br.com.android.invviteme.httpServices;

import java.util.List;

import br.com.android.invviteme.constants.ApiCall;
import br.com.android.invviteme.model.Event;
import retrofit2.Call;
import retrofit2.http.GET;

public interface HttpServiceEvent {

    @GET(ApiCall.PATH_GET_EVENT)
    Call<List<Event>> getEvents();

}
