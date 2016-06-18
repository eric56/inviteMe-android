package br.com.android.invviteme.model;

public class EventTypeAccess {

	private Integer id;
	private String typeAccess;
	
	/**
	 * Default Constructor only use JacksonMapper
	 */
	@Deprecated
	public EventTypeAccess() {}
	
	public EventTypeAccess(String typeAccess) {
		this.typeAccess = typeAccess;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeAccess() {
		return typeAccess;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((typeAccess == null) ? 0 : typeAccess.hashCode());
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
		EventTypeAccess other = (EventTypeAccess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (typeAccess == null) {
			if (other.typeAccess != null)
				return false;
		} else if (!typeAccess.equals(other.typeAccess))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "EventTypeAccess [id=" + id + ", typeAccess=" + typeAccess + "]";
    }
}