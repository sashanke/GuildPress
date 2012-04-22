$.extend($.gritter.options, { 
    position: 'top-left', // defaults to 'top-right' but can be 'bottom-left', 'bottom-right', 'top-left', 'top-right' (added in 1.7.1)
	fade_in_speed: 'medium', // how fast notifications fade in (string or int)
	fade_out_speed: 2000, // how fast the notices fade out
	time: 6000 // hang on the screen for...
});

userOnlineChannel.bind('pusher:subscription_succeeded', function(members){
  $('#members').empty();
  members.each(function(member) {
      addMember(member);
  });
});

userOnlineChannel.bind('pusher:member_added', function(member){
  addMember(member);
});

userOnlineChannel.bind('pusher:member_removed', function(member){
    removeMember(member)
});

userOnlineChannel.bind('newPrivateMessageBroadcast', function(member){
    $.getJSON("/ws/check/privatemessages/", function(json) {
		if(json.user.mailCount > 0) {
			$("#message-notifier").addClass("link-messages-unread");
			
			$.gritter.add({
				// (string | mandatory) the heading of the notification
				title: 'Neue Nachricht von ' + json.message.fromUser.avatarLink,
				// (string | mandatory) the text inside the notification
				text: json.message.subject,
				image: json.message.fromUser.image
			});
			
			
		}
		$(".unreadedmessages").text(json.user.mailCount);
		if(json.user.mailCount == 0) {
			$("#message-notifier").removeClass("link-messages-unread");
		}
    });
});


function addMember(member){
  var span = '<span id="user-'+member.id+'">'+member.info.avatarLink+'</span>';
  var div = '<div class="box user-online-box" id="user-alert-box-'+member.id+'"><span class="online-alert">'+member.info.avatarLink+' ist nun Online</span></div>';
  
  $('#user-online').append( div );
  $('#whosOnline').append( span );
  $('#noone').remove()
}

function removeMember(member){
  $('#user-alert-box-'+member.id).remove()
  $('#user-'+member.id).remove()
}





function addMessage(msgId) {
    $.get("/shoutbox/message/"+msgId+"/"+false, function(response){
    	var content = $(response);
    	$(content).hide();
    	$("#shoutbox-list").append(content);
    	var count = $("#shoutbox-list div").length;
    	if (count > 5) {
    		$("#shoutbox-list div:first-child").hide(500, function() { $(this).remove(); $(content).show('slow'); });
    	} else {
    		$(content).show('slow');
    	}
    	$("#shoutbox-input").val("");
    }).error(function() { setTimeout('addMessage('+msgId+')',500); })
    .complete(function() { });
}

shoutBoxChannel.bind('newMessage', function(msgId) {
	addMessage(msgId);
});

shoutBoxChannel.bind('deleteMessage', function(msgId) {
	$("#list-" + msgId).effect('highlight', {color:"#9E0036"}, 3000).stop().fadeOut(500, function() { 
		$(this).remove(); 
	    $.get("/shoutbox/message/json/list", function(response) {
	    	$("#shoutbox-list").replaceWith(response);
	    });
	});
});

shoutBoxChannel.bind('updateMessage', function(msgId) {
    $.get("/shoutbox/message/json/"+msgId, function(response){
    	$("#shoutbox-list-message-" + msgId).text(response.shortMessage);
    });
	$("#list-" + msgId).effect("bounce", {times:3}, 200 );
});


$(document).ready(function() {
	$('ul.top-navigation').superfish(); 
	$("#shoutbox-top-input", this).hide();
	$("#shoutbox").hover(function() {
		$("#shoutbox-top-input", this).show('slow');
	}, function() {
		$("#shoutbox-top-input", this).hide('slow');
	});
	
	$("#shoutbox-form").submit(function(event) {
	    /* stop form from submitting normally */
	    event.preventDefault(); 
	    /* get some values from elements on the page: */
	    var $form = $( this ),
	        message = $form.find( 'input[name="message"]' ).val(),
	        avatarId = $form.find( 'input[name="avatarId"]' ).val(),
	        url = $form.attr( 'action' );
	    if(message.trim().length == 0) {
	    	return false;
	    }
	    /* Send the data using post and put the results in a div */
	    $.post( url, { message: message, id: avatarId, fullsize: true});
	  });
});