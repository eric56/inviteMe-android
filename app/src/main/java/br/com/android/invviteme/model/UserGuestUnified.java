package br.com.android.invviteme.model;

public class UserGuestUnified {

    private Integer id;
    private Users user;
    private Event event;
    private Integer amount;

    /**
	 * Default Constructor only use JacksonMapper
	 */
	@Deprecated
    public UserGuestUnified(){}
	
    public UserGuestUnified(Users user, Event event, Integer amount) {
		this.user = user;
		this.event = event;
		this.amount = amount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserGuestUnified other = (UserGuestUnified) obj;
        if (amount == null) {
            if (other.amount != null) return false;
        } else if (!amount.equals(other.amount)) return false;
        if (event == null) {
            if (other.event != null) return false;
        } else if (!event.equals(other.event)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserGuestUnified [id=" + id + ", user=" + user + ", event=" + event + ", amount=" + amount + "]";
    }
}
