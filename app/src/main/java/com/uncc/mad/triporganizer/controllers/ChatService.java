package com.uncc.mad.triporganizer.controllers;

import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.Message;

import java.util.Date;

public class ChatService {

    IData iData;

    public ChatService(IData iData) {
        this.iData = iData;
    }

    private void GetAllMessagesBasedOnChatRoomId(String chatRoomId){


    }

    private void DeleteMessage(Message message){

        //use chatroom id and message from object message

    }

    private void FilterMessagesForDeletedUser(Date endDate){


    }
}
