package br.com.android.invviteme.model;

public class EventType {

    private Integer id;

    private String category;

    /**
	 * Default Constructor only use JacksonMapper
	 */
	@Deprecated
    public EventType() {}
    
    public EventType(String category, StatusType statusType) {
		this.category = category;
		this.statusType = statusType;
	}

    private StatusType statusType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((statusType == null) ? 0 : statusType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EventType other = (EventType) obj;
        if (category == null) {
            if (other.category != null) return false;
        } else if (!category.equals(other.category)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (statusType == null) {
            if (other.statusType != null) return false;
        } else if (!statusType.equals(other.statusType)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "EventType [id=" + id + ", category=" + category + ", statusType=" + statusType + "]";
    }

}
