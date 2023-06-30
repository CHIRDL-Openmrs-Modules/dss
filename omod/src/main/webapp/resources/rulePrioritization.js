var dssRulePrioritizedRulesUrl = ctx + "/module/dss/getPrioritizedRuleEntries.form?";
var dssRuleNonPrioritizedRulesUrl = ctx + "/module/dss/getNonPrioritizedRuleEntries.form?";
var dssRuleDisassociatedRulesUrl = ctx + "/module/dss/getDisassociatedRules.form?";
var dssRuleSaveRulesUrl = ctx + "/module/dss/saveRules.form?";
var dssRuleCreateRuleTypeUrl = ctx + "/module/dss/createRuleType.form?";
var allFields;
var ruleTypeName;
var ruleTypeDescription;
var tips;
var newRuleTypeDialog;
var prevAvailableChecked = null;
// position 0 is available rule selection, 1 is prioritized, and 2 is non-prioritized
var prevCheckedArray = [null, null, null];



$j( function() {
	ruleTypeName = $j( "#ruleTypeName" );
	ruleTypeDescription = $j("#ruleTypeDescription" );
	allFields = $j( [] ).add( ruleTypeName ).add( ruleTypeDescription );
	tips = $j( ".validateTips" );
	
    $j( "#ruleTypeSelect" )
      .selectmenu({
          change: function( event, data ) {
        	  event.preventDefault();
        	  loadRuleTypeSelection(data.item.value);
        	  $j("#availableRuleSearch").val("");
        	  $j("#prioritizedRuleSearch").val("");
        	  $j("#nonPrioritizedRuleSearch").val("");
          }
         }
      )
      .selectmenu( "menuWidget" )
        .addClass( "overflow" );
    
    $j("#availableRules").on("click", "li", function (e) {
    	handleListClick(0, this, e);
    });
    
    $j("#prioritizedRules").on("click", "li", function (e) {
    	handleListClick(1, this, e);
    });
    
    $j("#nonPrioritizedRules").on("click", "li", function (e) {
    	handleListClick(2, this, e);
    });
    
    $j("#availableRules, #prioritizedRules, #nonPrioritizedRules").sortable({
        connectWith: ".connectedSortable",
        revert: true,
        scroll: true,
        delay: 150,
        helper: function (e, item) {
        	//Disable the scroll of the list being copied from.  Weird behavior
        	//can occur when dragging downward.
        	$j( this ).sortable( "option", "scroll", false );
            //Basically, if you grab an unhighlighted item to drag, it will deselect (unhighlight) everything else
            if (!item.hasClass("selected")) {
                item.addClass("selected").siblings().removeClass("selected");
            }
            
            //////////////////////////////////////////////////////////////////////
            //HERE'S HOW TO PASS THE SELECTED ITEMS TO THE `stop()` FUNCTION:
            
            //Clone the selected items into an array
            var elements = item.parent().children(".selected").clone();
            
            //Add a property to `item` called 'multidrag` that contains the 
            //  selected items, then remove the selected items from the source list
            item.data("multidrag", elements).siblings(".selected").remove();
            
            //Now the selected items exist in memory, attached to the `item`,
            //  so we can access them later when we get to the `stop()` callback
            
            //Create the helper
            var helper = $j("<li/>");
            return helper.append(elements);
        },
        stop: function (e, ui) {
            //Now we access those items that we stored in `item`s data!
            var elements = ui.item.data("multidrag");
            
            //`elements` now contains the originally selected items from the source list (the dragged items)!!
            
            //Finally I insert the selected items after the `item`, then remove the `item`, since 
            //  item is a duplicate of one of the selected items.
            ui.item.after(elements).remove();
            
            //Remove the selection class because this indexes get messed up 
            //when copying across lists.
            $j(".selected").removeClass("selected");
            
            //Re-enable the sorting of the list
            $j( this ).sortable( "option", "scroll", true );
            
            //Highlight the first item that matches what's in the search field
            locatePrioritizedRules(false);
        }
      }).addClass( "listOverflow" );
    
    $j("#errorDialog, #successDialog").dialog({
        open: function() { 
            $j(".ui-dialog").addClass("ui-dialog-shadow"); 
            $j(".ui-dialog").addClass("no-close");
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
	        	  $j(this).dialog("close");
	          }
          }
        ]
    });
    
    $j( ".progressBarDiv" ).hide();
    $j( "#mainPB" ).hide();
    
    $j( "#submitButton" ).button();
    $j("#submitButton").click(function(event) {
    	$j("#submitConfirmationDialog").dialog("open");
		event.preventDefault();
	});
    
    $j("#submitConfirmationDialog").dialog({
        open: function() { 
            $j(".ui-dialog").addClass("ui-dialog-shadow"); 
            $j(".ui-dialog").addClass("no-close");
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
	        	  $j(this).dialog("close");
	        	  save();
	          }
          },
          {
	          text:"No",
	          click: function() {
	        	  $j(this).dialog("close");
	          }
          }
        ]
    });
    
    newRuleTypeDialog = $j( "#newRuleTypeDialog" ).dialog({
        autoOpen: false,
        height: 400,
        width: 350,
        modal: true,
        resizable: false,
        buttons: {
          "Create Rule Type": function() {
        	  addRuleType();
          },
          Cancel: function() {
        	  $j("#ruleTypeSelect").val("Please Choose a Rule Type");
      		  $j("#ruleTypeSelect").selectmenu("refresh");
        	  loadRuleTypeSelection("Please Choose a Rule Type");
        	  newRuleTypeDialog.dialog( "close" );
          }
        },
        open: function() { 
            $j(".ui-dialog").addClass("ui-dialog-shadow"); 
            $j(".ui-dialog").addClass("no-close");
        },
        close: function() {
        	newRuleTypeForm[ 0 ].reset();
        	allFields.removeClass( "ui-state-error" );
        	tips.text("");
        }
    });
    
    var newRuleTypeForm = newRuleTypeDialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
        addRuleType();
    });
} );

function populatePrioritizedRules(ruleType) {
	var action = "ruleType=" + ruleType;
	$j.ajax({
	  beforeSend: function() {
		  $j( "#prioritizedRulesPB" ).show();
      },
      complete: function() {
    	  $j( "#prioritizedRulesPB" ).hide();
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
	var message = data.message;
	if (message) {
		$j( "#errorMessage" ).html("An error occurred loading the prioritized rules list:\n" + message);
	    $j( "#errorDialog" ).dialog("open");
	    return false;
	}
	
	var selectmenu = $j("#prioritizedRules");
	$j.each(data, function (index, value) {
		var ruleEntryId = value.ruleEntryId || null;
		if (ruleEntryId != null) {
			var rule = value.rule || null;
			if (rule != null) {
				var listItem = '<li rule_entry_id="' + ruleEntryId + '" rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable("refresh");
}

function handlePopulatePrioritizedRulesError(xhr, textStatus, error) {
	$j( "#errorMessage" ).html("An error occurred loading the prioritized rules list:\n" + error);
    $j( "#errorDialog" ).dialog("open");
}

function populateNonPrioritizedRules(ruleType) {
	var action = "ruleType=" + ruleType;
	$j.ajax({
	  beforeSend: function() {
		  $j( "#nonPrioritizedRulesPB" ).show();
      },
      complete: function() {
    	  $j( "#nonPrioritizedRulesPB" ).hide();
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
	var message = data.message;
	if (message) {
		$j( "#errorMessage" ).html("An error occurred loading the prioritized rules list:\n" + message);
	    $j( "#errorDialog" ).dialog("open");
	    return false;
	}
	
	var selectmenu = $j("#nonPrioritizedRules");
	$j.each(data, function (index, value) {
		var ruleEntryId = value.ruleEntryId || null;
		if (ruleEntryId != null) {
			var rule = value.rule || null;
			if (rule != null) {
				var listItem = '<li rule_entry_id="' + ruleEntryId + '" rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
				selectmenu.append(listItem);
			}
		}
    });
	
	selectmenu.sortable("refresh");
}

function handlePopulateNonPrioritizedRulesError(xhr, textStatus, error) {
	$j( "#errorMessage" ).html("An error occurred loading the non-prioritized rules list:\n" + error);
    $j( "#errorDialog" ).dialog("open");
}

function populateAvailableRules(ruleType) {
	var action = "ruleType=" + ruleType;
	$j.ajax({
	  beforeSend: function() {
		  $j( "#availableRulesPB" ).show();
      },
      complete: function() {
    	  $j( "#availableRulesPB" ).hide();
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
	var message = data.message;
	if (message) {
		$j( "#errorMessage" ).html("An error occurred loading the prioritized rules list:\n" + message);
	    $j( "#errorDialog" ).dialog("open");
	    return false;
	}
	
	var selectmenu = $j("#availableRules");
	$j.each(data, function (index, value) {
		var rule = value.rule || null;
		if (rule != null) {
			var listItem = '<li rule_id="' + rule.ruleId + '" class="ui-state-default">' + rule.tokenName + '</li>';
			selectmenu.append(listItem);
		}
    });
	
	selectmenu.sortable("refresh");
}

function handlePopulateDisassociatedRulesError(xhr, textStatus, error) {
	$j( "#errorMessage" ).html("An error occurred loading the available rules list:\n" + error);
    $j( "#errorDialog" ).dialog("open");
}

function save() {
	//run an AJAX post request to your server-side script, $j(this).serialize() is the data from your form being added to the request
	var ruleType = $j("#ruleTypeSelect option:selected").val();
	if (ruleType === "Create New" || ruleType === "Please Choose a Rule Type") {
		$j( "#errorMessage" ).html("Please select a valid rule type.");
	    $j( "#errorDialog" ).dialog("open");
	    return;
	}
	
	var availableRules = constructJSON($j("#availableRules li"));
	var prioritizedRules = constructJSON($j("#prioritizedRules li"));
	var nonPrioritizedRules = constructJSON($j("#nonPrioritizedRules li"));
    $j.ajax({
    	beforeSend: function() {
    		$j( "#mainPB" ).show();
        },
        complete: function() {
        	$j( "#mainPB" ).hide();
        },
    	"cache": false,
        "data": {availableRulesSave: JSON.stringify(availableRules), 
        	prioritizedRulesSave: JSON.stringify(prioritizedRules), 
        	nonPrioritizedRulesSave: JSON.stringify(nonPrioritizedRules), 
        	ruleType: ruleType},
        "dataType": "text",
        "type": "POST",
        "url": dssRuleSaveRulesUrl,
        "timeout": 60000, // optional if you want to handle timeouts (which you should)
        "error": handleSaveError, // this sets up jQuery to give me errors
        "success": function (data) {
        	if (data === "success") {
        		$j( "#successMessage" ).html("Changes were successfully saved.");
        		$j( "#successDialog" ).dialog("open");
        	} else if (data === "error") {
        		$j( "#errorMessage" ).html("An error occurred saving the changes." +
        				"  Please check the server logs for details.");
        	    $j( "#errorDialog" ).dialog("open");
        	}
        }
    });
}

function handleSaveError(xhr, textStatus, error) {
	$j( "#errorMessage" ).html("An error occurred saving the changes:\n" + error);
    $j( "#errorDialog" ).dialog("open");
}

function constructJSON(availableRuleListItems) {
	//construct JSON for current available rules
	var rulesJson = [];
	
	availableRuleListItems.each(function(idx, li) {
	    var listItem = $j(li);
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

function clearLists() {
	var selectmenu = $j("#prioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	
	selectmenu = $j("#nonPrioritizedRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
	
	selectmenu = $j("#availableRules");
	selectmenu.find("li").remove().end();
	selectmenu.sortable("refresh");
}

function addRuleType() {
	var valid = true;
    allFields.removeClass( "ui-state-error" );
    valid = valid && checkLength( ruleTypeName, "Name", 1, 255 );
    valid = valid && checkLength( ruleTypeDescription, "Description", 0, 255 );
    if (valid) {
    	var ruleTypeNameStr = ruleTypeName.val();
    	var action = "ruleType=" + ruleTypeNameStr + "&description=" + ruleTypeDescription.val();
        $j.ajax({
        	beforeSend: function() {
      		  $j( "#mainPB" ).show();
            },
            complete: function() {
          	  $j( "#mainPB" ).hide();
            },
        	"cache": false,
            "data": action,
            "dataType": "text",
            "type": "POST",
            "url": dssRuleCreateRuleTypeUrl,
            "timeout": 30000, // optional if you want to handle timeouts (which you should)
            "error": handleCreateRuleTypeError, // this sets up jQuery to give me errors
            "success": function (data) {
            	if (data === "success") {
            		newRuleTypeDialog.dialog("close");
            		$j( "#successMessage" ).html("Rule type successfully created.");
            		$j( "#successDialog" ).dialog("open");
            		$j( '<option value="' + ruleTypeNameStr + '">' + ruleTypeNameStr + '</option>' ).appendTo($j("#ruleTypeSelect"));
            		sortRuleTypeSelect();
            		$j("#ruleTypeSelect").selectmenu("refresh");
            		$j("#ruleTypeSelect").val(ruleTypeNameStr);
            		$j("#ruleTypeSelect").selectmenu("refresh");
            		loadRuleTypeSelection(ruleTypeNameStr);
            	} else if (data === "duplicate") {
            		$j( "#errorMessage" ).html("The specified rule type already exists.");
            	    $j( "#errorDialog" ).dialog("open");
            	} else if (data === "error") {
            		$j( "#errorMessage" ).html("An error occurred saving the rule type." +
    					"  Please check the server logs for details.");
            		$j( "#errorDialog" ).dialog("open");
            	}
            }
        });
    }
}

function handleCreateRuleTypeError(xhr, textStatus, error) {
	$j( "#errorMessage" ).html("An error occurred creating the new rule type:\n" + error);
    $j( "#errorDialog" ).dialog("open");
}

function checkLength( o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min ) {
      o.addClass( "ui-state-error" );
      updateTips( "Length of " + n + " must be between " +
        min + " and " + max + "." );
      return false;
    } else {
      return true;
    }
}

function updateTips( t ) {
    tips
      .text( t )
      .addClass( "ui-state-highlight" );
    setTimeout(function() {
      tips.removeClass( "ui-state-highlight", 1500 );
    }, 500 );
}

function loadRuleTypeSelection(ruleType) {
	clearLists();
	if (ruleType !== "Create New" && ruleType !== "Please Choose a Rule Type") {
		populatePrioritizedRules(ruleType);
  	  	populateNonPrioritizedRules(ruleType);
  	  	populateAvailableRules(ruleType);
	} else if (ruleType === "Create New") {
		newRuleTypeDialog.dialog("open");
	}
}

function filterAvailableRules() {
	filterResults($j("#availableRuleSearch"), document.getElementById("availableRules"));
}

function filterNonPrioritizedRules() {
	filterResults($j("#nonPrioritizedRuleSearch"), document.getElementById("nonPrioritizedRules"));
}

function locatePrioritizedRules(reposition) {
	var filter = $j("#prioritizedRuleSearch").val().toUpperCase();
	var found = false;
	$j("#prioritizedRules li").each(function(idx, li) {
	    var listItem = $j(li);
	    var rule = listItem.text().toUpperCase();
	    if (filter === "") {
	    	listItem.removeClass("selected");
	    } else if (rule.indexOf(filter) > -1) {
	    	if (!found) {
	    		listItem.addClass("selected");
	    		if (reposition) {
	    			listItem[0].scrollIntoView();
	    		}
	    	} else {
	    		listItem.removeClass("selected");
	    	}
	    	
	    	found = true;
        } else {
        	listItem.removeClass("selected");
        }
	});
}

function filterResults(searchField, list) {
	var li = list.getElementsByTagName("li");
	var filter = searchField.val().toUpperCase();
	for ( i = 0; i < li.length; i++) {
		var rule = li[i].innerHTML.toUpperCase();
		if (rule.indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
	}
}

function handleListClick(prevCheckedArrayPosition, listItem, event) {
	var prevChecked = prevCheckedArray[prevCheckedArrayPosition];
	if (!prevChecked) {
		prevChecked = listItem;
		prevCheckedArray[prevCheckedArrayPosition] = prevChecked;
		$j(listItem).addClass("selected");
	}
	
	if (event.shiftKey) {
		var startIndex = $j(listItem).index();
		var endIndex = $j(prevChecked).index();
		$j(listItem).parent().children().slice(Math.min(startIndex,endIndex), 1 + Math.max(startIndex,endIndex)).addClass("selected");
	} else if (event.ctrlKey || event.metaKey) {
    	if ($j(listItem).hasClass("selected")) {
    		$j(listItem).removeClass("selected");
    		prevChecked = null;
    		prevCheckedArray[prevCheckedArrayPosition] = prevChecked;
    	} else {
    		$j(listItem).addClass("selected");
    	}
    } else {
    	prevChecked = listItem;
    	prevCheckedArray[prevCheckedArrayPosition] = prevChecked;
        $j(listItem).addClass("selected").siblings().removeClass("selected");
    }
}

function sortRuleTypeSelect() {
    // Remove the "Please Choose a Rule Type" and "Create new" options
    $j('#ruleTypeSelect option:first').remove();
    $j('#ruleTypeSelect option:first').remove();
    
    $j( "#ruleTypeSelect" ).html($j( "#ruleTypeSelect" ).children("option").sort(function (x, y) {
        return $j(x).text().toUpperCase() < $j(y).text().toUpperCase() ? -1 : 1;
    }));
    
    // Add back the "Please Choose a Rule Type" and "Create new" options
    $j( "#ruleTypeSelect" ).prepend('<option value="Create New">Create New...</option>');
    $j( "#ruleTypeSelect" ).prepend('<option value="Please Choose a Rule Type">Please Choose a Rule Type</option>');
	
    $j( "#ruleTypeSelect" ).get(0).selectedIndex = 0;
}