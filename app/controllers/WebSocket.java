package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import play.mvc.Http.*;

import static play.libs.F.*;
import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.*;

import java.util.*;

import models.*;

public class WebSocket extends Controller {
    
    public static void room(String chatname) {
        render(chatname);
    }

    public static class ChatRoomSocket extends WebSocketController {
        
        public static void join(String chatname) {
            
            Chat room = Chat.get();
            
            // Socket connected, join the chat room
            EventStream<Chat.Event> roomMessagesStream = room.join(chatname);
         
            // Loop while the socket is open
            while(inbound.isOpen()) {
                
                // Wait for an event (either something coming on the inbound socket channel, or ChatRoom messages)
                Either<WebSocketEvent,Chat.Event> e = await(Promise.waitEither(
                    inbound.nextEvent(), 
                    roomMessagesStream.nextEvent()
                ));
                
                // Case: chatname typed 'quit'
                for(String chatnameMessage: TextFrame.and(Equals("quit")).match(e._1)) {
                    room.leave(chatname);
                    outbound.send("quit:ok");
                    disconnect();
                }
                
                // Case: TextEvent received on the socket
                for(String chatnameMessage: TextFrame.match(e._1)) {
                    room.say(chatname, chatnameMessage, null);
                }
                
                // Case: Someone joined the room
                for(Chat.Join joined: ClassOf(Chat.Join.class).match(e._2)) {
                    outbound.send("join:%s", joined.chatname);
                }
                
                // Case: New message on the chat room
                for(Chat.Message message: ClassOf(Chat.Message.class).match(e._2)) {
                    outbound.send("message:%s:%s", message.chatname, message.text);
                }
                
                // Case: Someone left the room
                for(Chat.Leave left: ClassOf(Chat.Leave.class).match(e._2)) {
                    outbound.send("leave:%s", left.chatname);
                }
                
                // Case: The socket has been closed
                for(WebSocketClose closed: SocketClosed.match(e._1)) {
                    room.leave(chatname);
                    disconnect();
                }
                
            }
            
        }
        
    }
    
}

