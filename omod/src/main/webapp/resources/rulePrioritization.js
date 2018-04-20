var dssRulePrioritizedRulesUrl = ctx + "/module/dss/getPrioritizedRuleEntries.htm?";
var dssRuleNonPrioritizedRulesUrl = ctx + "/module/dss/getNonPrioritizedRuleEntries.htm?";
$( function() {
    $( "#ruleTypeSelect" )
      .selectmenu({
          change: function( event, data ) {
        	  var ruleType = data.item.value;
        	  populatePrioritizedRules(ruleType);
        	  populateNonPrioritizedRules(ruleType);
          }
         }
      )
      .selectmenu( "menuWidget" )
        .addClass( "overflow" );
    
    $("#availableRules, #prioritizedRules, #nonPrioritizedRules").on('click', 'li', function (e) {
        if (e.ctrlKey || e.metaKey) {
        	if ($(this).hasClass("selected")) {
        		$(this).removeClass("selected");
        	} else {
        		$(this).addClass("selected");
        	}
        } else {
            $(this).addClass("selected").siblings().removeClass('selected');
        }
    }).sortable({
        connectWith: ".connectedSortable",
        revert: true,
        scroll: true,
        delay: 150,
        helper: function (e, item) {
            //Basically, if you grab an unhighlighted item to drag, it will deselect (unhighlight) everything else
            if (!item.hasClass('selected')) {
                item.addClass('selected').siblings().removeClass('selected');
            }
            
            //////////////////////////////////////////////////////////////////////
            //HERE'S HOW TO PASS THE SELECTED ITEMS TO THE `stop()` FUNCTION:
            
            //Clone the selected items into an array
            var elements = item.parent().children('.selected').clone();
            
            //Add a property to `item` called 'multidrag` that contains the 
            //  selected items, then remove the selected items from the source list
            item.data('multidrag', elements).siblings('.selected').remove();
            
            //Now the selected items exist in memory, attached to the `item`,
            //  so we can access them later when we get to the `stop()` callback
            
            //Create the helper
            var helper = $('<li/>');
            return helper.append(elements);
        },
        stop: function (e, ui) {
            //Now we access those items that we stored in `item`s data!
            var elements = ui.item.data('multidrag');
            
            //`elements` now contains the originally selected items from the source list (the dragged items)!!
            
            //Finally I insert the selected items after the `item`, then remove the `item`, since 
            //  item is a duplicate of one of the selected items.
            ui.item.after(elements).remove();
            
            //Remove the selection class
            $(".selected").removeClass("selected");
        }
      }).addClass( "listOverflow" );
    
} );

function populatePrioritizedRules(ruleType) {
	var selectmenu = $("#prioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	var action = "ruleType=" + ruleType;
	$.ajax({
	  beforeSend: function(){
		  //$("#formServerError").hide();
		  //$("#formLoading").show();
      },
      complete: function(){
    	  //$("#formLoading").hide();
      },
      "accepts": {
    	  mycustomtype: "application/json"
      },
	  "cache": false,
	  "dataType": "json",
	  "data": action,
	  "type": "GET",
	  "url": dssRulePrioritizedRulesUrl,
	  "timeout": 30000, // optional if you want to handle timeouts (which you should)
	  "error": handlePopulatePrioritizedRulesError, // this sets up jQuery to give me errors
	  "success": function (data) {
		  populatePrioritizedRuleList(data);
      }
	});
}

function populatePrioritizedRuleList(data) {
	var selectmenu = $("#prioritizedRules");
	$.each(data, function (index, value) {
		var ruleEntryId = value.ruleEntryId || null;
		if (ruleEntryId != null) {
			var rule = value.rule || null;
			if (rule != null) {
				var listItem = '<li id="' + ruleEntryId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable('refresh');
}

function handlePopulatePrioritizedRulesError(xhr, textStatus, error) {
	//$( "#cacheMessage" ).html("An error occurred clearing the cache:\n" + error);
    //$( "#clearCacheCompleteDialog" ).dialog("open");
}

function populateNonPrioritizedRules(ruleType) {
	var selectmenu = $("#nonPrioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	var action = "ruleType=" + ruleType;
	$.ajax({
	  beforeSend: function(){
		  //$("#formServerError").hide();
		  //$("#formLoading").show();
      },
      complete: function(){
    	  //$("#formLoading").hide();
      },
      "accepts": {
    	  mycustomtype: "application/json"
      },
	  "cache": false,
	  "dataType": "json",
	  "data": action,
	  "type": "GET",
	  "url": dssRuleNonPrioritizedRulesUrl,
	  "timeout": 30000, // optional if you want to handle timeouts (which you should)
	  "error": handlePopulateNonPrioritizedRulesError, // this sets up jQuery to give me errors
	  "success": function (data) {
		  populateNonPrioritizedRuleList(data);
      }
	});
}

function populateNonPrioritizedRuleList(data) {
	var selectmenu = $("#nonPrioritizedRules");
	$.each(data, function (index, value) {
		var ruleEntryId = value.ruleEntryId || null;
		if (ruleEntryId != null) {
			var rule = value.rule || null;
			if (rule != null) {
				var listItem = '<li id="' + ruleEntryId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable('refresh');
}

function handlePopulateNonPrioritizedRulesError(xhr, textStatus, error) {
	//$( "#cacheMessage" ).html("An error occurred clearing the cache:\n" + error);
    //$( "#clearCacheCompleteDialog" ).dialog("open");
}