$(document)
		.ready(
				function() {
					$('a.item')
							.cluetip(
									{
										showTitle : false,
										tracking : true,
										cluetipClass : 'wiki-tooltip',
										leftOffset : '70',
										sticky : true,
										closePosition : 'top',
										closeText : '<span style="font-size: 20px;" class="color-close icon-remove"></span>'
									});
					$('a.transmog-frame')
							.cluetip(
									{
										showTitle : false,
										tracking : true,
										cluetipClass : 'wiki-tooltip',
										leftOffset : '70',
										sticky : true,
										closePosition : 'top',
										closeText : '<span style="font-size: 20px;" class="color-close icon-remove"></span>'
									});
					$(".recipe-remember").click(function() {
						var count = $("#recipe-" + $(this).attr('rel')).text();
						if (count == null) {
							count = 1;
						} else {
							count++;
						}
						
						$("#recipe-" + $(this).attr('rel')).text(count);
						
						$.ajax({
							url : '/recipe/save/' + $(this).attr('rel'),
							type : 'GET',
							async : true,
							cache : false,
							timeout : 30000,
							beforeSend : function() {
								$.blockUI({
									message : $('#waitMessage'),
									css : {
										backgroundColor : 'transparent',
										border : '0'
									}
								});
							},
							error : function() {
								$.unblockUI();
							},
							success : function(msg) {
								$("#shoppingcart").replaceWith(msg);
								
								$('#shoppingcart a.item')
								.cluetip(
										{
											showTitle : false,
											tracking : true,
											cluetipClass : 'wiki-tooltip',
											leftOffset : '70',
											sticky : true,
											closePosition : 'top',
											closeText : '<span style="font-size: 20px;" class="color-close icon-remove"></span>'
										});
								
								$.unblockUI();
							}
						});
					});
					
					
					$(".recipe-update").click(function() {
						$.ajax({
							url : $(this).attr('rel'),
							type : 'GET',
							async : true,
							cache : false,
							timeout : 30000,
							beforeSend : function() {
								$.blockUI({
									message : $('#waitMessage'),
									css : {
										backgroundColor : 'transparent',
										border : '0'
									}
								});
							},
							error : function() {
								$.unblockUI();
							},
							success : function(msg) {
								location.reload();
								$.unblockUI();
							}
						});
					});
					
					$(".recipe-remove").click(function() {
						var count = $("#recipe-" + $(this).attr('rel')).text();
						if (count != null) {
							count--;
						}
						if (count == 0) {
							count = "";
						}
						$("#recipe-" + $(this).attr('rel')).text(count);
						
						$.ajax({
							url : '/recipe/remove/' + $(this).attr('rel'),
							type : 'GET',
							async : true,
							cache : false,
							timeout : 30000,
							beforeSend : function() {
								$.blockUI({
									message : $('#waitMessage'),
									css : {
										backgroundColor : 'transparent',
										border : '0'
									}
								});
							},
							error : function() {
								$.unblockUI();
							},
							success : function(msg) {
								$("#shoppingcart").replaceWith(msg);
								$('#shoppingcart a.item')
								.cluetip(
										{
											showTitle : false,
											tracking : true,
											cluetipClass : 'wiki-tooltip',
											leftOffset : '70',
											sticky : true,
											closePosition : 'top',
											closeText : '<span style="font-size: 20px;" class="color-close icon-remove"></span>'
										});
								$.unblockUI();
							}
						});
					});
				});
