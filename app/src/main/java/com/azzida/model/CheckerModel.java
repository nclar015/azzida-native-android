package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckerModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("mother_maiden_name")
    @Expose
    private Object motherMaidenName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("driver_license_state")
    @Expose
    private Object driverLicenseState;
    @SerializedName("driver_license_number")
    @Expose
    private Object driverLicenseNumber;
    @SerializedName("copy_requested")
    @Expose
    private Boolean copyRequested;
    @SerializedName("previous_driver_license_state")
    @Expose
    private Object previousDriverLicenseState;
    @SerializedName("previous_driver_license_number")
    @Expose
    private Object previousDriverLicenseNumber;
    @SerializedName("adjudication")
    @Expose
    private Object adjudication;
    @SerializedName("custom_id")
    @Expose
    private Object customId;
    @SerializedName("no_middle_name")
    @Expose
    private Boolean noMiddleName;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("report_ids")
    @Expose
    private List<Object> reportIds = null;
    @SerializedName("geo_ids")
    @Expose
    private List<Object> geoIds = null;


    @SerializedName("error")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Object getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(Object motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getDriverLicenseState() {
        return driverLicenseState;
    }

    public void setDriverLicenseState(Object driverLicenseState) {
        this.driverLicenseState = driverLicenseState;
    }

    public Object getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(Object driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public Boolean getCopyRequested() {
        return copyRequested;
    }

    public void setCopyRequested(Boolean copyRequested) {
        this.copyRequested = copyRequested;
    }

    public Object getPreviousDriverLicenseState() {
        return previousDriverLicenseState;
    }

    public void setPreviousDriverLicenseState(Object previousDriverLicenseState) {
        this.previousDriverLicenseState = previousDriverLicenseState;
    }

    public Object getPreviousDriverLicenseNumber() {
        return previousDriverLicenseNumber;
    }

    public void setPreviousDriverLicenseNumber(Object previousDriverLicenseNumber) {
        this.previousDriverLicenseNumber = previousDriverLicenseNumber;
    }

    public Object getAdjudication() {
        return adjudication;
    }

    public void setAdjudication(Object adjudication) {
        this.adjudication = adjudication;
    }

    public Object getCustomId() {
        return customId;
    }

    public void setCustomId(Object customId) {
        this.customId = customId;
    }

    public Boolean getNoMiddleName() {
        return noMiddleName;
    }

    public void setNoMiddleName(Boolean noMiddleName) {
        this.noMiddleName = noMiddleName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Object> getReportIds() {
        return reportIds;
    }

    public void setReportIds(List<Object> reportIds) {
        this.reportIds = reportIds;
    }

    public List<Object> getGeoIds() {
        return geoIds;
    }

    public void setGeoIds(List<Object> geoIds) {
        this.geoIds = geoIds;
    }
}
