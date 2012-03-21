$(function() {
	$("div.content img").each(function() {
		$(this).aeImageResize({
			height : 710,
			width : 710
		});
	});

});

$(function() {
	// Expose the form
	$('form').click(function() {
		$('form').expose({
			api : true
		}).load();
	});

	// If there is an error, focus to form
	if ($('form .error').size()) {
		$('form').expose({
			api : true,
			loadSpeed : 0
		}).load();
		$('form input[type=text]').get(0).focus();
	}
});

$(document).ready(function() { 
	$(".post-content-tools").hide();
$(".post-content").hover(
        function() {
            $(".post-content-tools", this).show('slow');
        },
        function() {
            $(".post-content-tools", this).hide('slow');
        });
});
tinyMCE
		.init({
			// General options
			mode : "textareas",
			theme : "advanced",
			height : "250px",
			width : "100%",
			plugins : "inlinepopups,emotions,media,spellchecker,fullscreen,preview,searchreplace,paste,style",
			dialog_type : "modal",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : false,
			theme_advanced_path : true,
			theme_advanced_buttons1 : "bold,italic,underline,strikethrough,separator,justifyleft,justifycenter,justifyright,separator,formatselect,fontselect,fontsizeselect,forecolor,styleprops,fullscreen,code,preview",
			theme_advanced_buttons2 : "cut,copy,paste,pasteword,separator,search,replace,separator,bullist,numlist,separator,hr,removeformat,separator,outdent,indent,blockquote,spoiler,separator,link,unlink,emotions,separator,spellchecker,image,media",
			theme_advanced_buttons3 : ""
		});
