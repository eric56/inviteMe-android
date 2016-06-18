package br.com.android.invviteme.model;

import com.google.firebase.database.Exclude;

import br.com.android.invviteme.enums.StatusData;

public class StatusType {

    private Integer id;
    private String description;

    /**
     * Default Constructor only use JacksonMapper
     */
    public StatusType() {
    }

    public StatusType(StatusData data) {
        this.id = data.getId();
        this.description = data.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Exclude
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StatusType other = (StatusType) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Exclude
    @Override
    public String toString() {
        return "Status [id=" + id + ", description=" + description + "]";
    }
}
