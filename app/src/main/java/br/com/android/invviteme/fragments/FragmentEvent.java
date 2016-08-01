package br.com.android.invviteme.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import br.com.android.invviteme.R;
import br.com.android.invviteme.animations.HideShowProgressBar;

public class FragmentEvent extends Fragment {

    private final String KEY_STATE_LIST_EVENT = "state_list_event";
    private static Bundle mBundleRecyclerViewState;

    SwipeRefreshLayout swipeEventList;
    ObservableRecyclerView resultEventList;
    ProgressBar progressListEvent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event,container,false);

        swipeEventList = (SwipeRefreshLayout)view.findViewById(R.id.swipeEventList);
        progressListEvent = (ProgressBar) view.findViewById(R.id.progressListEvent);
        resultEventList = (ObservableRecyclerView) view.findViewById(R.id.resultEventList);

        configureListEvent(resultEventList);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HideShowProgressBar.showProgress(true,progressListEvent,resultEventList, getActivity());

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_STATE_LIST_EVENT);
            resultEventList.getLayoutManager().onRestoreInstanceState(listState);
            HideShowProgressBar.showProgress(false,progressListEvent,resultEventList, getActivity());
        }else{
            /*
            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCall.BASE_URL).build();
            HttpServiceEvent serviceLogin = retrofit.create(HttpServiceEvent.class);

            Call<List<Event>> call = serviceLogin.getEvents();
            call.enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    // TODO: converter na lista e chamar o adapter do recycler view
                    HideShowProgressBar.showProgress(false,progressListEvent,resultEventList, getActivity());
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    HideShowProgressBar.showProgress(false,progressListEvent,resultEventList, getActivity());
                    Log.d("invviteMe", t.getMessage());
                    Snackbar.make(resultEventList, R.string.error_process, Snackbar.LENGTH_LONG)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                @TargetApi(Build.VERSION_CODES.M)
                                public void onClick(View v) {
                                }
                            });
                }
            });
            */
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = resultEventList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_STATE_LIST_EVENT, listState);
    }

    private void configureListEvent(ObservableRecyclerView recycler){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.getItemAnimator().setAddDuration(200);
        recycler.getItemAnimator().setRemoveDuration(200);
        recycler.getItemAnimator().setMoveDuration(200);
        recycler.getItemAnimator().setChangeDuration(200);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
    }

}
