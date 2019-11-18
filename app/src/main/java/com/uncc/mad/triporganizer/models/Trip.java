package com.uncc.mad.triporganizer.models;

import java.util.List;

public class Trip {
    private String Id;
    private String TripCreatorUserId;
    private String Title;
    private String CoverPhotoUri;
    private String LocationName;
    private double LocationLatitude;
    private double LocationLongitude;
    private List<TripMember> ExistingMembers;
    private List<TripMember> DeletedMembers;
    private ChatRoom ChatRoom;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTripCreatorUserId() {
        return TripCreatorUserId;
    }

    public void setTripCreatorUserId(String tripCreatorUserId) {
        TripCreatorUserId = tripCreatorUserId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCoverPhoto() {
        return CoverPhotoUri;
    }

    public void setCoverPhoto(String coverPhoto) {
        CoverPhotoUri = coverPhoto;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getLocationLatitude() {
        return LocationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        LocationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return LocationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        LocationLongitude = locationLongitude;
    }

    public List<TripMember> getExistingMembers() {
        return ExistingMembers;
    }

    public void setExistingMembers(List<TripMember> existingMembers) {
        ExistingMembers = existingMembers;
    }

    public List<TripMember> getDeletedMembers() {
        return DeletedMembers;
    }

    public void setDeletedMembers(List<TripMember> deletedMembers) {
        DeletedMembers = deletedMembers;
    }

    public ChatRoom getChatRoom() {
        return ChatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        ChatRoom = chatRoom;
    }
}
