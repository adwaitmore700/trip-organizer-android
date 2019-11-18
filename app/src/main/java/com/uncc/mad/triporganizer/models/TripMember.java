package com.uncc.mad.triporganizer.models;

import java.util.Date;

public class TripMember {
    private String TripId;
    private String MemberId;
    private Date FirstJoinDate;
    private Date LastJoinDate;      //this would be same as FirstJoinDate when joined the trip for the first time
    private Date LastDeletedDate;

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public Date getFirstJoinDate() {
        return FirstJoinDate;
    }

    public void setFirstJoinDate(Date firstJoinDate) {
        FirstJoinDate = firstJoinDate;
    }

    public Date getLastJoinDate() {
        return LastJoinDate;
    }

    public void setLastJoinDate(Date lastJoinDate) {
        LastJoinDate = lastJoinDate;
    }

    public Date getLastDeletedDate() {
        return LastDeletedDate;
    }

    public void setLastDeletedDate(Date lastDeletedDate) {
        LastDeletedDate = lastDeletedDate;
    }
}
