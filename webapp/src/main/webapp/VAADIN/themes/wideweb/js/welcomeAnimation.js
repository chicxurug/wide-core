window.$( document ).ready(function() {
  if(window.$animation == 0) { window.$( ".welcomeTop" ).hide(); }
    if(window.$animation == 0) { window.$( ".welcomeBottom" ).hide(); }
    window.$( ".welcome1" ).hide();
    window.$( ".welcome2" ).hide();
    window.$( ".welcome3" ).hide();
    window.$( ".welcome4" ).hide();
    window.$( ".searchLogo" ).hide();
    window.$( ".fadeIn" ).hide();
    window.$( ".searchLogo" ).delay( 200 ).fadeIn( 1000 );
    window.$( ".fadeIn" ).fadeIn( 1000 );
});