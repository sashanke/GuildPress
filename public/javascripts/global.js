//HashMap = function() {
//	this._dict = {};
//}
//HashMap.prototype._shared = {
//	id : 1
//};
//HashMap.prototype.put = function put(key, value) {
//	if (typeof key == "object") {
//		if (!key.hasOwnProperty._id) {
//			key.hasOwnProperty = function(key) {
//				return Object.prototype.hasOwnProperty.call(this, key);
//			}
//			key.hasOwnProperty._id = this._shared.id++;
//		}
//		this._dict[key.hasOwnProperty._id] = value;
//	} else {
//		this._dict[key] = value;
//	}
//	return this; // for chaining
//}
//HashMap.prototype.get = function get(key) {
//	if (typeof key == "object") {
//		return this._dict[key.hasOwnProperty._id];
//	}
//	return this._dict[key];
//}
//
//var count = 0;
//var files = '/public/';
//var lastTime = 0;

//function prepare(response) {
//	var d = new Date();
//	count++;
//	d.setTime(response.msg_date);
//	var mytime = d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
//
//	var string = '<div class="shoutbox-list" id="list-'
//			+ count
//			+ '">'
//			+ '<span class="shoutbox-list-message" style="white-space:nowrap; overflow:hidden;">'
//			+ response.message + '</span>'
//			+ '<span class="shoutbox-list-nick">' + response.name + '</span>'
//
//			+ '</div>';
//
//	return string;
//}
//
//function success(response, status) {
//
//	if (status == 'success') {
//		lastTime = response.date;
//		$('#daddy-shoutbox-response').html(
//				'<img src="' + files + 'images/accept.png" />');
//		$('#daddy-shoutbox-list').append(prepare(response));
//		$('input[name=message]').attr('value', '').focus();
//		$('.shoutbox-more').cluetip({
//			showTitle : false,
//			sticky : true,
//			closeText : 'Schließen'
//		});
//		$($('.shoutbox-list').get().reverse()).each(function(i, shoutboxlist) {
//			if (i >= 5) {
//				$(shoutboxlist).hide('slow');
//			}
//		});
//		$('#list-' + count).fadeIn('slow');
//		timeoutID = setTimeout(refresh, 3000);
//	}
//}
//
//function validate(formData, jqForm, options) {
//	for ( var i = 0; i < formData.length; i++) {
//		if (!formData[i].value) {
//			alert('Please fill in all the fields');
//			$('input[@name=' + formData[i].name + ']').css('background', 'red');
//			return false;
//		}
//	}
//	$('#daddy-shoutbox-response').html(
//			'<img src="' + files + 'images/loader.gif" />');
//	clearTimeout(timeoutID);
//}
//
//function onlineUsers(users, user) {
//
//	if (users.length == 0) {
//
//		$("#whosOnline").append(
//				'<i class="online-user"><span style="display: none;" id="user-'
//						+ user.id + '">' + user.avatar.avatarLink
//						+ '</span> </i>');
//		
//		
//		
//		if ($('#whosOnline').children().length > 1) {
//			$('#noone').hide();
//		}
//		
//
//		$("#user-online").append(
//				'<div class="box user-online-box" id="user-alert-box-'+ user.id +'"><span class="online-alert">' + user.avatar.avatarLink + ' ist nun Online</span></div>');
//		$('#user-alert-box-'+ user.id).fadeIn('slow');
//		$('#user-alert-box-'+ user.id).delay(5000).fadeOut('slow');
//
//		setTimeout(function(){
//			$('#user-alert-box-'+ user.id).remove();
//		}, 7000);
//		
//		
//	}
//}
//
//function oc(a) {
//	var o = {};
//	for ( var i = 0; i < a.length; i++) {
//		o[a[i]] = '';
//	}
//	return o;
//}
//
//function refresh() {
//
//	online = new Array();
//
//	$.getJSON("/shoutbox/" + lastTime, function(json) {
//
//		if (json.messages.length) {
//			for (i = 0; i < json.messages.length; i++) {
//				var newHtml = prepare(json.messages[i]);
//				$('#daddy-shoutbox-list').append(newHtml);
//				$('#list-' + count).fadeIn('slow');
//				$('.shoutbox-more').cluetip({
//					showTitle : false,
//					sticky : true,
//					closeText : 'Schließen'
//				});
//			}
//			lastTime = json.messages[json.messages.length - 1].msg_date;
//		}
//
//		if(json.user.mailCount > 0) {
//			$("#message-notifier").addClass("link-messages-unread");
//		}
//		$(".unreadedmessages").text(json.user.mailCount);
//		
//		if(json.user.mailCount == 0) {
//			$("#message-notifier").removeClass("link-messages-unread");
//		}
//		
//		
//		if (json.onlineUsers.length) {
//			for (i = 0; i < json.onlineUsers.length; i++) {
//				onlineUsers($("#user-" + json.onlineUsers[i].id).get(),
//						json.onlineUsers[i]);
//				online.push(json.onlineUsers[i].id);
//				$("#user-" + json.onlineUsers[i].id).show('slow');
//			}
//		}
//
//		$($(".online-user").get()).each(function(i, onlineUser) {
//			var currentId = $(onlineUser).children("span").attr('id').slice(5);
//			if (currentId in oc(online)) {
//				
//			} else {
//				console.log('remove ' + currentId);
//				$(onlineUser).remove();
//			}
//
//		});
//
//		console.log($('#whosOnline').children().length);
//		
//		if ($('#whosOnline').children().length == 1) {
//			$('#noone').show();
//		}
//		
//		
//	});
//	
//	$($('.shoutbox-list').get()).each(function(i, shoutboxlist) {
//		if (i >= 5) {
//			$(shoutboxlist).hide('slow');
//		}
//	});
//
//	timeoutID = setTimeout(refresh, 3000);
//}




// wait for the DOM to be loaded
$(document).ready(function() {
	$('ul.top-navigation').superfish(); 
	
	$(".class").click(
		    function(){
		    $.ajax({
		        url: $(this).attr('href'),
		        type: 'GET',
		        async: true,
		        cache: false,
		        timeout: 30000,
		        beforeSend: function() {
		  		  $.blockUI({ message: $('#waitMessage'),css: { backgroundColor: 'transparent', border: '0' } });
		  	  	},
		        error: function(){
		            return true;
		        },
		        complete: function(){
		  		  
		  	  	},
		        success: function(msg){ 
		        	$.unblockUI();
		        }
		    });
		});
});

$(function() {
	$('#box-login-success').delay(5000).fadeOut('slow');
	$('#box-login-error').delay(5000).fadeOut('slow');

	$("#dialog-nyi").dialog({
		modal : true,
		autoOpen : false,
		buttons : {
			Ok : function() {
				$(this).dialog("close");
			}
		}
	});
	$(".nyi").click(function() {
		$("#dialog-nyi").dialog("open");
	});

	$("#dialog-login").dialog({
		modal : true,
		autoOpen : false
	});
	$(".dologgin").click(function() {
		$("#dialog-login").dialog("open");
	});

});

$(document).ready(function() {

	$('.log-boss').cluetip({
		width : '260px',
		showTitle : false,
		hoverClass : 'highlight',
		sticky : true,
		closePosition : 'top',
		closeText : 'Schließen'
	});

//	$.ajax({
//		url : "/login",
//		type : "GET",
//		success : function(html) {
//			// wenn Response true bzw. den Status 1 zurückliefert
//			// $("#char-banner-request").remove();
//			$("#dialog-login").append(html);
//		}
//	});
});
