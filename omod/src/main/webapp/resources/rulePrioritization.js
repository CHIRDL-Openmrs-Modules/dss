var dssRulePrioritizedRulesUrl = ctx + "/module/dss/getPrioritizedRuleEntries.htm?";
var dssRuleNonPrioritizedRulesUrl = ctx + "/module/dss/getNonPrioritizedRuleEntries.htm?";
var dssRuleDisassociatedRulesUrl = ctx + "/module/dss/getDisassociatedRules.htm?";
var dssRuleSaveRulesUrl = ctx + "/module/dss/saveRules.htm?";
$( function() {
    $( "#ruleTypeSelect" )
      .selectmenu({
          change: function( event, data ) {
        	  var ruleType = data.item.value;
        	  populatePrioritizedRules(ruleType);
        	  populateNonPrioritizedRules(ruleType);
        	  populateAvailableRules(ruleType);
        	  event.preventDefault();
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
    
    $("#errorDialog").dialog({
        open: function() { 
            $(".ui-dialog").addClass("ui-dialog-shadow"); 
            $(".ui-dialog").addClass("no-close");
        },
        autoOpen: false,
        modal: true,
        resizable: false,
        show: {
          effect: "fade",
          duration: 500
        },
        hide: {
          effect: "fade",
          duration: 500
        },
        buttons: [
          {
	          text:"OK",
	          click: function() {
	        	  $(this).dialog("close");
	          }
          }
        ]
    });
    
    $( ".progressBarDiv" ).hide();
    
    $( "#submitButton" ).button();
    $("#submitButton").click(function(event) {
    	$("#submitConfirmationDialog").dialog("open");
		event.preventDefault();
	});
    
    $("#submitConfirmationDialog").dialog({
        open: function() { 
            $(".ui-dialog").addClass("ui-dialog-shadow"); 
            $(".ui-dialog").addClass("no-close");
        },
        autoOpen: false,
        modal: true,
        resizable: false,
        show: {
          effect: "fade",
          duration: 500
        },
        hide: {
          effect: "fade",
          duration: 500
        },
        buttons: [
          {
	          text:"Yes",
	          click: function() {
	        	  $(this).dialog("close");
	        	  save();
	          }
          },
          {
	          text:"No",
	          click: function() {
	        	  $(this).dialog("close");
	          }
          }
        ]
    });
} );

function populatePrioritizedRules(ruleType) {
	var selectmenu = $("#prioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	var action = "ruleType=" + ruleType;
	$.ajax({
	  beforeSend: function(){
		  $( "#prioritizedRulesPB" ).show();
      },
      complete: function(){
    	  $( "#prioritizedRulesPB" ).hide();
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
				var listItem = '<li rule_entry_id="' + ruleEntryId + '" rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable('refresh');
}

function handlePopulatePrioritizedRulesError(xhr, textStatus, error) {
	$( "#errorMessage" ).html("An error occurred loading the prioritized rules list:\n" + error);
    $( "#errorDialog" ).dialog("open");
}

function populateNonPrioritizedRules(ruleType) {
	var selectmenu = $("#nonPrioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	var action = "ruleType=" + ruleType;
	$.ajax({
	  beforeSend: function(){
		  $( "#nonPrioritizedRulesPB" ).show();
      },
      complete: function(){
    	  $( "#nonPrioritizedRulesPB" ).hide();
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
				var listItem = '<li rule_entry_id="' + ruleEntryId + '" rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable('refresh');
}

function handlePopulateNonPrioritizedRulesError(xhr, textStatus, error) {
	$( "#errorMessage" ).html("An error occurred loading the non-prioritized rules list:\n" + error);
    $( "#errorDialog" ).dialog("open");
}

function populateAvailableRules(ruleType) {
	var selectmenu = $("#availableRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	var action = "ruleType=" + ruleType;
	$.ajax({
	  beforeSend: function(){
		  $( "#availableRulesPB" ).show();
      },
      complete: function(){
    	  $( "#availableRulesPB" ).hide();
      },
      "accepts": {
    	  mycustomtype: "application/json"
      },
	  "cache": false,
	  "dataType": "json",
	  "data": action,
	  "type": "GET",
	  "url": dssRuleDisassociatedRulesUrl,
	  "timeout": 30000, // optional if you want to handle timeouts (which you should)
	  "error": handlePopulateDisassociatedRulesError, // this sets up jQuery to give me errors
	  "success": function (data) {
		  populateAvailableRuleList(data);
      }
	});
}

function populateAvailableRuleList(data) {
	var selectmenu = $("#availableRules");
	$.each(data, function (index, value) {
		var rule = value.rule || null;
		if (rule != null) {
			var listItem = '<li rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
			selectmenu.append(listItem);
		}
    });
	
	selectmenu.sortable('refresh');
}

function handlePopulateDisassociatedRulesError(xhr, textStatus, error) {
	$( "#errorMessage" ).html("An error occurred loading the available rules list:\n" + error);
    $( "#errorDialog" ).dialog("open");
}

function save() {
	//run an AJAX post request to your server-side script, $this.serialize() is the data from your form being added to the request
	var availableRules = constructJSON($("#availableRules li"));
	var prioritizedRules = constructJSON($("#prioritizedRules li"));
	var nonPrioritizedRules = constructJSON($("#nonPrioritizedRules li"));
    $.ajax({
    	beforeSend: function (xhr) {
		    
	    },
	    complete: function(){
    	    //$( "#availableRulesPB" ).hide();
        },
        "cache": false,
        "data": {availableRulesSave: JSON.stringify(availableRules), prioritizedRulesSave: JSON.stringify(prioritizedRules), nonPrioritizedRulesSave: JSON.stringify(nonPrioritizedRules)},
        "dataType": "text",
        "type": "POST",
        "url": dssRuleSaveRulesUrl,
        "timeout": 30000, // optional if you want to handle timeouts (which you should)
        "error": handleSaveError, // this sets up jQuery to give me errors
        "success": function (xml) {
        	//window.parent.closeIframe();
        }
    });
}

function handleSaveError(xhr, textStatus, error) {
	$( "#errorMessage" ).html("An error occurred saving the changes:\n" + error);
    $( "#errorDialog" ).dialog("open");
}

function constructJSON(availableRuleListItems) {
	//construct JSON for current available rules
	var rulesJson = [];
	
	availableRuleListItems.each(function(idx, li) {
	    var listItem = $(li);
	    var ruleEntryId = listItem.attr("rule_entry_id") || null;
	    var ruleId = listItem.attr("rule_id");
	    var ruleEntry = new RuleEntry(ruleEntryId, ruleId)
	    rulesJson.push(ruleEntry);
	    
	});
	
	return rulesJson;
}

function RuleEntry(ruleEntryId, ruleId) {
	this.ruleEntryId = ruleEntryId;
	this.rule = new Rule(ruleId);
}

function Rule(ruleId) {
	this.ruleId = ruleId;
}