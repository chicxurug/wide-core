window.$( document ).ready(function() {
				
				/* menu */
				window.$( ".subMenuContainer" ).hide();
				window.$( ".subMenu + .subMenu" ).hide();
				
				window.$( ".menu a" ).click(function() {
					window.$( ".subMenuContainer" ).slideDown( 600 );
				});
				
					window.$( ".main" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
					window.$( ".top" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
					window.$( ".bottom" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
					window.$( ".footer" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
					window.$( ".left" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
					window.$( ".right" ).click(function() {
						window.$(".subMenuContainer").slideUp( 600 );
					});
				window.$( ".subMenu.first > li > a" ).click(function() {
					window.$( ".subMenu.first > li > a" ).removeClass('selected');
					window.$(this).addClass('selected');
					window.$( ".subMenu.no6" ).slideDown( 600 );
				});
				window.$( ".subMenu.second > li > a" ).click(function() {
					window.$( ".subMenu.second > li > a" ).removeClass('selected');
					window.$(this).addClass('selected');
					window.$( ".subMenu.no6" ).slideDown( 600 );
				});
				
				/* user */
				window.$( ".userSubContainer" ).hide();
				window.$( ".userIco" ).click(function() {
					window.$( ".userSubContainer" ).slideDown( 600 );
				});
				window.$( ".main" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
				window.$( ".top" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
				window.$( ".bottom" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
				window.$( ".footer" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
				window.$( ".left" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
				window.$( ".right" ).click(function() {
					window.$(".userSubContainer").slideUp( 600 );
				});
});