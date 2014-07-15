window.$(window).add("*").unbind();
function closeSubHeader(){
    window.$(".subMenuContainer").slideUp( 300 );
    window.$( ".userSubContainer" ).slideUp( 300 );
    window.$( ".searchFilter" ).slideUp( 300 );    
}
function deselectTopMenuItem(){
    window.$(".menu a").removeClass("selected");
    window.$(".subMenu.first > div > li").removeClass("selected");
    window.$(".userIco").removeClass("selected");
    window.$(".topSearch").removeClass("selected");    
}
function subHeaderAnimation(block, menu){
    deselectTopMenuItem();
    window.$(menu).addClass("selected");
    if (window.$(block).css("display") != "block") {
      closeSubHeader();
      if (window.$(".subHeader").height() > 10) {
        window.$(block).delay(400).slideDown( 300 );
      }else{
        window.$(block).slideDown( 300 );
      }
    }
    if (block == ".searchFilter"){
      $closeOnClick = 0;
      return $closeOnClick;
    }
}
window.$( ".menu a" ).click(function() {
    subHeaderAnimation(".subMenuContainer", this);
    $closeOnClick = 1;
});
window.$( ".subMenu.first > div > li" ).click(function() {
    window.$( ".subMenu.first > div > li" ).removeClass("selected");
    window.$(this).addClass("selected");
    com_wide_wideweb_menuSelect(window.$(this).text());
    window.$( ".subMenu.no6" ).slideDown( 300 );
});
window.$( ".subMenu.second > div > li" ).click(function() {
    window.$( ".subMenu.second > div > li" ).removeClass("selected");
    window.$(this).addClass("selected");
    com_wide_wideweb_subMenuSelect(window.$(this).text(), 0, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
    window.$( "body" ).removeClass("welcome");
    subHeaderAnimation(".searchFilter", ".topSearch");
});
window.$( ".userIco" ).click(function() {
    subHeaderAnimation(".userSubContainer", this);
    $closeOnClick = 1;
});
window.$( ".filterIco" ).click(function() {
    subHeaderAnimation(".searchFilter", ".topSearch");
    com_wide_wideweb_filterCategory(window.$("input[name='Enter search keywords here']").val(),window.$("input[name='Author']").val(),window.$("input[name='Publisher']").val(),window.$("input[name='Book']").val(),window.$("input[name='Title']").val(),window.$("input[name='Submits No.']").val(),window.$("select[name='Language']").find(':selected').val(),window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
});
window.$( ".searchInput" ).click(function() {
    subHeaderAnimation(".searchFilter", ".topSearch");
});
window.$( ".closeFilter" ).click(function() {
    closeSubHeader();
    deselectTopMenuItem();
});
window.$( ".main" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".top" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".bottom" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".footer" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".left" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".right" ).click(function() { if ($closeOnClick == 1) { closeSubHeader(); deselectTopMenuItem(); } });
window.$( ".filterList > li.folder" ).click(function() {
      com_wide_wideweb_subMenuSelect(window.$(this).children().eq(1).text(), 500, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
      window.$(this).animate({"margin-left": "-=950"}, 300);
      window.$(this).hide(300);
      window.$( ".breadcrumb > div > li + li + li + li +li + li" ).delay(300).slideDown( 200 );      
});
window.$( ".filterList > li.lesson" ).click(function() {
	com_wide_wideweb_openExercise(window.$(this).attr("id"));
});
window.$( ".breadcrumb > div > li").click(function() {
	com_wide_wideweb_subMenuSelect(window.$(this).text(), 0, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
});
window.$(".searchFilter.reset .filled, .topSearch.reset .filled, .main .search .filled").each(function() {
	var actualInput = this;
    var default_value = actualInput.name;
    window.$( ".clearFilter" ).click(function() {
  	  window.$(".searchFilter.reset input:checkbox").prop("checked", true);
      window.$(actualInput).addClass("default");
      actualInput.value = (default_value);      
    });
    window.$(this).focus(function() {
      if(this.value == default_value) {
        this.value = "";
        window.$(this).removeClass("default");
        window.$(this).addClass("filled");
      }
    });
    window.$(this).blur(function() {
      if(this.value == "") {
        window.$(this).addClass("default");
        this.value = default_value;
      }
    });    
});
window.$(".searchFilter.reset select").change(function() {
	window.$(this).removeClass("default");
    window.$(this).addClass("filled");
});
window.$( ".clearFilter" ).hover(function() {
    window.$( ".searchFilter ul.col > li > a input").css( "color", "red");
    window.$( ".searchFilter ul.col > li > a select").css( "color", "red");
    window.$(".searchInput").css( "color", "red");
});
window.$( ".clearFilter" ).mouseleave(function() {
    window.$( ".searchFilter ul.col > li > a input").css( "color", "");
    window.$( ".searchFilter ul.col > li > a select").css( "color", "");
    window.$( ".searchInput").css( "color", "");
});
window.$( ".logo" ).click(function() {
	location.reload();
});
window.$( ".welcome .search .submit").click(function() {
    window.$(".welcome .search .text").trigger("enterKey");
});
window.$(".welcome .search .text").bind("enterKey",function(e){   
    if (window.$(".welcome .search .text").val() != $searchDefaultText && window.$(".welcome .search .text").val() != "") {
      var searchValue = this.value;
      window.$(".topSearch .searchInput").val(searchValue);
      window.$(".topSearch .searchInput").removeClass("default");
      window.$(".topSearch .searchInput").addClass("filled");
      subHeaderAnimation(".searchFilter", ".topSearch");
      window.$("body").removeClass("welcome");
    }
});
window.$(".welcome .search .text").keyup(enterkey = function(e){
    if(e.keyCode == 13)
    {
      window.$(this).trigger("enterKey");
    }
});
function footerFix(){
    if (window.$(window).height() < 600) {
      window.$(".footer").removeClass("fixed").val("");
    }
    else {
      window.$(".footer").addClass("fixed").val("");
    }
}  
window.$(window).resize(function(){
    footerFix();
});
window.$( ".userSubContainer > .subMenu > li" ).click(function() {
    com_wide_wideweb_loginSelect(window.$(this).text());
});
window.$( ".donate").click(function() {
    com_wide_wideweb_createExercise(window.$(this).text());
});
window.$( ".aboutUs").click(function() {
    com_wide_wideweb_editExercise(window.$(this).text());
});
window.$( "input[value='Submit']" ).click(function() {
	com_wide_wideweb_checkAnswer(window.$(".solutionBar .yourSolution > input").val());    
    window.$(".solutionBar .yourSolution > input").removeClass("filled");
    window.$(".solutionBar .yourSolution > input").addClass("default");
});
window.$( "input[value='Solution']" ).click(function() {
	com_wide_wideweb_checkSolution(window.$(".solutionBar .yourSolution > input").val());    
});
window.$(".solutionBar .yourSolution > input").focus(function() {
	if(window.$(this).hasClass("default")) {
        this.value = "";
        window.$(this).removeClass("default");
        window.$(this).addClass("filled");
        window.$(this).removeAttr("style");
    }
});
window.$(".solutionBar .yourSolution > input").blur(function() {
	if(this.value == "") {
        window.$(this).val($solutionDefaultText);
        window.$(this).addClass("default");
    }
});