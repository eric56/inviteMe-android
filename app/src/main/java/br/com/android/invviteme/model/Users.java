package br.com.android.invviteme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class Users implements Serializable {

    private Integer id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String password;
    private StatusType statusType;
    private String dateUpdated;
    private String birthday;
    private boolean admin;

    /**
     * Default Constructor only use JacksonMapper and DataSnapshot.getValue() do Firebase
     */
    public Users() {
    }

    public Users(String name, String lastName, String birthday, String phoneNumber, String email, String password, String gender, StatusType statusType) {
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.statusType = statusType;
        this.dateUpdated = new Date().toString();
        this.admin = Boolean.FALSE;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public void alterPassword(String password) {
        this.password = password;
        this.dateUpdated = new Date().toString();
    }

    public String getPassword() {
        return password;
    }

    public void alterStatusType(StatusType statusType) {
        this.statusType = statusType;
        this.dateUpdated = new Date().toString();
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getCompleteName(){
        return  name + " " + lastName;
    }

    @Exclude
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (admin ? 1231 : 1237);
        result = prime * result
                + ((birthday == null) ? 0 : birthday.hashCode());
        result = prime * result
                + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + gender.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result
                + ((statusType == null) ? 0 : statusType.hashCode());
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
        Users other = (Users) obj;
        if (admin != other.admin)
            return false;
        if (birthday == null) {
            if (other.birthday != null)
                return false;
        } else if (!birthday.equals(other.birthday))
            return false;
        if (dateUpdated == null) {
            if (other.dateUpdated != null)
                return false;
        } else if (!dateUpdated.equals(other.dateUpdated))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (gender != other.gender)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (statusType == null) {
            if (other.statusType != null)
                return false;
        } else if (!statusType.equals(other.statusType))
            return false;
        return true;
    }

    @Exclude
    @Override
    public String toString() {
        return "Users [id=" + id + ", name=" + name + ", lastName=" + lastName
                + ", phoneNumber=" + phoneNumber + ", email=" + email
                + ", gender=" + gender + ", password=" + password
                + ", statusType=" + statusType + ", dateUpdated=" + dateUpdated
                + ", birthday=" + birthday + ", admin=" + admin + "]";
    }
}
