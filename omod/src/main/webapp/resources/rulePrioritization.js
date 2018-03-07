$( function() {
    $( "#ruleTypeSelect" )
      .selectmenu()
      .selectmenu( "menuWidget" )
        .addClass( "overflow" );
    
    $( "#availableRules, #prioritizedRules, #nonPrioritizedRules" ).sortable({
        connectWith: ".connectedSortable",
        revert: true
      }).disableSelection();
} );