package br.com.android.invviteme.model;

import java.util.Date;

public class Event {

	private Integer id;
	private Users owner;
	private EventTypeAccess eventTypeAccess;
	private EventType eventType;
	private String title;
	private String description;
	private String observation;
	private boolean eventForFree;
	private boolean hiddenEvent;
	private boolean canSendInviteExternal;
	private int minimumAge;
	private String eventDate;
	private String eventInitHour;
	private String eventEndHour;
	private Integer limitGuests;
	private AddressEvent addressEvent;
	private StatusType statusType;
	private String dateUpdated;

	/**
	 * Default Constructor only use JacksonMapper
	 */
	@Deprecated
	public Event() {
	}

	public Event(Users owner, EventTypeAccess eventTypeAccess,
			EventType eventType, String title, String description,
			String observation, Integer limitGuests, AddressEvent addressEvent,
			StatusType statusType, String eventInitHour,
			String eventEndHour, boolean ageOfCensorship,
			boolean canSendInviteExternal, boolean eventForFree) {

		this.owner = owner;
		this.eventTypeAccess = eventTypeAccess;
		this.eventType = eventType;
		this.title = title;
		this.description = description;
		this.observation = observation;
		this.eventDate = new Date().toString();
		this.eventInitHour = eventInitHour;
		this.eventEndHour = eventEndHour;
		this.dateUpdated = new Date().toString();
		this.limitGuests = limitGuests;
		if (this.eventTypeAccess.getTypeAccess().equals("Público")) {
			this.hiddenEvent = Boolean.FALSE;
		}
		this.canSendInviteExternal = canSendInviteExternal;
		this.eventForFree = eventForFree;
		this.addressEvent = addressEvent;
		this.statusType = statusType;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Users getOwner() {
		return owner;
	}

	public EventTypeAccess getEventTypeAccess() {
		return eventTypeAccess;
	}

	public EventType getEventType() {
		return eventType;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getObservation() {
		return observation;
	}
	
	public void alterVisibilityEvent(boolean isHidden){
		this.hiddenEvent = isHidden;
		this.dateUpdated = new Date().toString();
	}
	
	public boolean isHiddenEvent() {
		return hiddenEvent;
	}

	public void allowSendInviteExternal(boolean allow){
		this.canSendInviteExternal = allow;
		this.dateUpdated = new Date().toString();
	}
	
	public boolean isCanSendInviteExternal() {
		return canSendInviteExternal;
	}

	public void alterDateEvent(String eventDate, String eventInitHour, String eventEndHour){
		this.eventDate = eventDate;
		this.eventInitHour = eventInitHour;
		this.eventEndHour = eventEndHour;
		this.dateUpdated = new Date().toString();
	}
	
	public String getEventDate() {
		return eventDate;
	}

	public String getEventInitHour() {
		return eventInitHour;
	}

	public String getEventEndHour() {
		return eventEndHour;
	}

	public Integer minusLimitGuests(Integer countGuests){
		return limitGuests - countGuests;
	}
	
	public Integer getLimitGuests() {
		return limitGuests;
	}

	public boolean isEventForFree() {
		return eventForFree;
	}

	public int getMinimumAge() {
		return minimumAge;
	}

	public AddressEvent getAddressEvent() {
		return addressEvent;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addressEvent == null) ? 0 : addressEvent.hashCode());
		result = prime * result + (canSendInviteExternal ? 1231 : 1237);
		result = prime * result
				+ ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result
				+ ((eventEndHour == null) ? 0 : eventEndHour.hashCode());
		result = prime * result + (eventForFree ? 1231 : 1237);
		result = prime * result
				+ ((eventInitHour == null) ? 0 : eventInitHour.hashCode());
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result
				+ ((eventTypeAccess == null) ? 0 : eventTypeAccess.hashCode());
		result = prime * result + (hiddenEvent ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((limitGuests == null) ? 0 : limitGuests.hashCode());
		result = prime * result + minimumAge;
		result = prime * result
				+ ((observation == null) ? 0 : observation.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((statusType == null) ? 0 : statusType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (addressEvent == null) {
			if (other.addressEvent != null)
				return false;
		} else if (!addressEvent.equals(other.addressEvent))
			return false;
		if (canSendInviteExternal != other.canSendInviteExternal)
			return false;
		if (dateUpdated == null) {
			if (other.dateUpdated != null)
				return false;
		} else if (!dateUpdated.equals(other.dateUpdated))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (eventEndHour == null) {
			if (other.eventEndHour != null)
				return false;
		} else if (!eventEndHour.equals(other.eventEndHour))
			return false;
		if (eventForFree != other.eventForFree)
			return false;
		if (eventInitHour == null) {
			if (other.eventInitHour != null)
				return false;
		} else if (!eventInitHour.equals(other.eventInitHour))
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (eventTypeAccess == null) {
			if (other.eventTypeAccess != null)
				return false;
		} else if (!eventTypeAccess.equals(other.eventTypeAccess))
			return false;
		if (hiddenEvent != other.hiddenEvent)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (limitGuests == null) {
			if (other.limitGuests != null)
				return false;
		} else if (!limitGuests.equals(other.limitGuests))
			return false;
		if (minimumAge != other.minimumAge)
			return false;
		if (observation == null) {
			if (other.observation != null)
				return false;
		} else if (!observation.equals(other.observation))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (statusType == null) {
			if (other.statusType != null)
				return false;
		} else if (!statusType.equals(other.statusType))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", owner=" + owner + ", eventTypeAccess="
				+ eventTypeAccess + ", eventType=" + eventType + ", title="
				+ title + ", description=" + description + ", observation="
				+ observation + ", eventForFree=" + eventForFree
				+ ", hiddenEvent=" + hiddenEvent + ", canSendInviteExternal="
				+ canSendInviteExternal + ", minimumAge=" + minimumAge
				+ ", eventDate=" + eventDate + ", eventInitHour="
				+ eventInitHour + ", eventEndHour=" + eventEndHour
				+ ", limitGuests=" + limitGuests + ", addressEvent="
				+ addressEvent + ", statusType=" + statusType
				+ ", dateUpdated=" + dateUpdated + "]";
	}
}
