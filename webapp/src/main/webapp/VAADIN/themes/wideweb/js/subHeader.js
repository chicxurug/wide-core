window.$( document ).ready(function() {
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
        function footerFix(){
          if (window.$(window).height() < 600) {
            window.$(".footer").removeClass("fixed").val("");
          }
          else {
            window.$(".footer").addClass("fixed").val("");
          }
        }
        var $closeOnClick = 1;
        window.$( ".subMenuContainer" ).hide();
        window.$( ".subMenu + .subMenu" ).hide();        
        window.$( ".menu a" ).click(function() {
          subHeaderAnimation(".subMenuContainer", this);
          $closeOnClick = 1;
        });
window.$( ".subMenu.first > div > li" ).click(function() {
          window.$( ".subMenu.first > div > li" ).removeClass("selected");
          window.$(this).addClass("selected");
          com.wide.wideweb.menuSelect(window.$(this).text());
          window.$( ".subMenu.no6" ).slideDown( 300 );
        });
        window.$( ".subMenu.second > div > li" ).click(function() {
          window.$( ".subMenu.second > div > li" ).removeClass("selected");
          window.$(this).addClass("selected");
          com.wide.wideweb.subMenuSelect(window.$(this).text(), 0, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
          window.$( "body" ).removeClass("welcome");
          subHeaderAnimation(".searchFilter", ".topSearch");
        });
        window.$( ".userSubContainer" ).hide();
        window.$( ".userIco" ).click(function() {
          subHeaderAnimation(".userSubContainer", this);
          $closeOnClick = 1;
        });
        window.$( ".searchFilter" ).hide();
        window.$( ".filterIco" ).click(function() {
          subHeaderAnimation(".searchFilter", ".topSearch");
          com.wide.wideweb.filterCategory(window.$("input[name='Enter search keywords here']").val(),window.$("input[name='Author']").val(),window.$("input[name='Publisher']").val(),window.$("input[name='Book']").val(),window.$("input[name='Title']").val(),window.$("input[name='Submits No.']").val(),window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
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
        window.$( ".breadcrumb > div > li + li + li + li" ).hide();
        window.$( ".filterList > li.folder" ).click(function() {       
          com.wide.wideweb.subMenuSelect(window.$(this).children().eq(1).text(), 500, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
          window.$(this).animate({"margin-left": "-=950"}, 300);
          window.$(this).hide(300);
          window.$( ".breadcrumb > div > li + li + li + li" ).delay(300).slideDown( 200 );          
        });
        window.$( ".breadcrumb > div > li").click(function() {
          com.wide.wideweb.subMenuSelect(window.$(this).text(), 0, window.$("input[name='typeCategory']").prop('checked'), window.$("input[name='typeLesson']").prop('checked'), window.$("input[name='typeTest']").prop('checked'));
        });
        window.$( ".donate").click(function() {
            com.wide.wideweb.createExercise(window.$(this).text());
        });
        window.$(".searchFilter.reset .filled, .topSearch.reset .filled, .main .search .filled").each(function() {
          var actualInput = this;
          var default_value = actualInput.name;
          window.$( ".clearFilter" ).click(function() {
        	window.$(".searchFilter.reset input:checkbox").prop("checked", true);
            window.$(actualInput).addClass("default");
            actualInput.value = (default_value);
          });
          var default_value = this.value;
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
        window.$( ".clearFilter" ).hover(function() {
          window.$( ".searchFilter ul.col > li > a input").css( "color", "red");
            window.$(".searchInput").css( "color", "red");
        });
        window.$( ".clearFilter" ).mouseleave(function() {
          window.$( ".searchFilter ul.col > li > a input").css( "color", "");
          window.$( ".searchInput").css( "color", "");
        });
        window.$( ".logo" ).click(function() {
          window.$("body").removeClass("welcome");
          window.$("body").addClass("welcome");
          closeSubHeader();
          deselectTopMenuItem();
        });
        var $searchDefaultText = window.$(".welcome .search .text").val();
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
        footerFix();
        window.$(window).resize(function(){
          footerFix();
        });
        window.$( ".userSubContainer > .subMenu > li" ).click(function() {
          com.wide.wideweb.loginSelect(window.$(this).text());
        });
});