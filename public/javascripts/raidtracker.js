 $(document).ready(function(){
		
		// When a link is clicked
		$("a.tab").click(function () {

			// switch all tabs off
			$(".active").removeClass("active");
			
			// switch this tab on
			$(this).addClass("active");
			
			// slide all content up
			$(".content").slideUp();
			
			// slide this content up
			var content_show = $(this).attr("href");
			$(content_show).slideDown();
		  	
		  	$.cookie("raidtracker-tab", content_show);
		  	
		  	////var test = $("#switch-pool").attr('hash');
		  	////alert(test);
		  	
		  	////var form_url = $("#switch-pool").attr("action");
    		////alert("Before - action=" + form_url);   
			var form_url = $("#switch-pool").attr("action");
			$("#switch-pool").removeAttr("action");
			var new_form_url = form_url.substring(0,form_url.indexOf("#"));

			
			$("#switch-pool").attr('action', new_form_url+content_show);
			var form_url2 = $("#switch-pool").attr("action");

		  	
		});
		
		var link = $("#raiditem").attr('href');
		var tab = $.cookie('raidtracker-tab');
		$("#raiditem").attr('href',link+tab);
		
		
		$("table.tooltip a").each(function(index) { 
			$(this).attr('target','_new');
		});
		
		
		var anchor = $(location).attr('hash');
		if(anchor != "") {
			$(".active").removeClass("active");
			$(".content").slideUp();
			$("#"+"tab-"+anchor.substring(1)).addClass("active");
			$(anchor).slideDown();
			var form_url = $("#switch-pool").attr("action");
			$("#switch-pool").removeAttr("action");
			$("#switch-pool").attr('action', form_url+anchor);
			var form_url2 = $("#switch-pool").attr("action");
		}

		$("#switch-pool").change(function () {
                var myform = $(this);
                // submit the form based on something?
                myform.submit();
        });
		
		
		$('a.item').cluetip({showTitle: false});
		
		
	  });
	  
	  
	    /* attach a submit handler to the form */
  $("#addRaid").submit(function(event) {

    /* stop form from submitting normally */
    event.preventDefault(); 
        
    /* get some values from elements on the page: */
    var $form = $( this ),
        xml = $form.find( 'textarea[name="raidxml"]' ).val(),
        pool = $form.find( 'select[name="raidpool"]' ).val(),
        url = $form.attr( 'action' );

    /* Send the data using post and put the results in a div */
    $.post( url, { pool: pool, raidxml: xml },
      function( data ) {
          var content = $( data ).find( '#content' );
          $( "#result" ).empty().append( content );
      }
    );
  });