package br.com.android.invviteme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.android.invviteme.R;
import br.com.android.invviteme.model.Event;

public class AdapterCardEvent extends RecyclerView.Adapter<AdapterCardEvent.ViewHolder> {

    private List<Event> eventList;

    public AdapterCardEvent(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);
        //TODO: Setar os holders com os valores do event
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameEvent, addressEvent, dateEvent, hourEvent, limitGuest, eventType;
        private ImageView imageEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            nameEvent = (TextView) itemView.findViewById(R.id.nameEvent);
            addressEvent = (TextView) itemView.findViewById(R.id.localEvent);
            dateEvent = (TextView) itemView.findViewById(R.id.dateEvent);
            hourEvent = (TextView) itemView.findViewById(R.id.hourEvent);
            limitGuest = (TextView) itemView.findViewById(R.id.limitGuests);
            eventType = (TextView) itemView.findViewById(R.id.eventTypeAccess);
            imageEvent = (ImageView) itemView.findViewById(R.id.imageEvent);
        }
    }
}
