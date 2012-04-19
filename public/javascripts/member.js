

$(document).ready(function() {
	
	var oTable;
	
	$(".char-remove").live('click',	function() {
		var nTr = $(this).parents('tr')[0];
		$.ajax({
			url : '/member/alt/delete/' + $(this).attr('rel'),
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
				
				oTable.fnDeleteRow( nTr );
				$.unblockUI();
			}
		});
	});
	

	
	oTable = $('#datatable-alts').dataTable({
		"bProcessing" : true,
		"bStateSave" : true,
		"sPaginationType" : "full_numbers",
		"aaSorting" : [ [ 4, "desc" ] ],
		"aoColumnDefs" : [ {
			"sClass" : "datatable_icon",
			"bSearchable" : false,
			"bSortable" : false,
			"bVisible" : true,
			"aTargets" : [ 0 ]
		}, {
			"sClass" : "datatable_name",
			"bSearchable" : true,
			"bSortable" : true,
			"bVisible" : true,
			"aTargets" : [ 1 ]
		}, {
			"sClass" : "datatable_guild",
			"bSearchable" : true,
			"bSortable" : true,
			"bVisible" : true,
			"aTargets" : [ 2 ]
		}, {
			"sClass" : "datatable_level",
			"bSearchable" : true,
			"bSortable" : true,
			"bVisible" : true,
			"aTargets" : [ 3 ]
		}, {
			"sClass" : "datatable_ilevel",
			"bSearchable" : true,
			"bSortable" : true,
			"bVisible" : true,
			"aTargets" : [ 4 ]
		}, {
			"sClass" : "datatable_options",
			"bSearchable" : false,
			"bSortable" : false,
			"bVisible" : true,
			"aTargets" : [ 5 ]
		} ],
		"oLanguage" : {
			"sProcessing" : "",
			"sLengthMenu" : "_MENU_ Einträge anzeigen",
			"sZeroRecords" : "Keine Einträge vorhanden.",
			"sInfo" : "_START_ bis _END_ von _TOTAL_ Einträgen",
			"sInfoEmpty" : "0 bis 0 von 0 Einträgen",
			"sInfoFiltered" : "(gefiltert von _MAX_  Einträgen)",
			"sInfoPostFix" : "",
			"sSearch" : "Suchen",
			"sUrl" : "",
			"oPaginate" : {
				"sFirst" : "Erster",
				"sPrevious" : "Zurück",
				"sNext" : "Nächster",
				"sLast" : "Letzter"
			}
		}
	});
//	// When a link is clicked
//	$("a.tab").click(function() {
//
//		// switch all tabs off
//		$(".active").removeClass("active");
//
//		// switch this tab on
//		$(this).addClass("active");
//
//		// slide all content up
//		$(".tab-content").slideUp();
//
//		// slide this content up
//		var content_show = $(this).attr("href");
//		$(content_show).slideDown();
//		
//		$.cookie("member-tab", content_show);
//		
//	});

	

	
	$(function() {
		$("#dialog-form-error").dialog({
			modal : true,
			autoOpen : false,
			buttons : {
				Ok : function() {
					$(this).dialog("close");
				}
			}
		});
	});
});
