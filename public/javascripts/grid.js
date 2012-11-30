$(document).ready(function() {

    $('#spinner').hide();
    
    var currentCell;

    var successHandler = function(data, textSuccess, jqXHR) {
        //alert("All done");
        console.log(data);
    }

    var errorHandler = function() {
        alert("Error");
    }

    $('#solve-button').click(function(e) {
        e.preventDefault();
        $('#spinner').show();
        $.getJSON(
            "solve", 
            {
                name: "Dave", 
                grid: $('.box td').text() 
            }
        ).done(successHandler)
         .fail(errorHandler)
         .always(function() { $('#spinner').hide(); });
    });

    $('#clear-button').click(function(e) {
        e.preventDefault();
        $('.box td').each(function(idx, elem) {
            $(elem).text(" ");
        });
    });

    $('.box td').click(function(e) {
        e.preventDefault();
        currentCell = $(this);
        $('.box td').css("background-color", "white");
        currentCell.css("background-color", "#FAF0E6");
        $('#input-pad').show();
    });

    $('#input-pad td,#input-pad th').click(function(e) {
        e.preventDefault();
        var digit = $(this).text();
        if (digit != "Cancel") {
            currentCell.text(digit == "Clear" ? " " : digit);
        }
        currentCell.css("background-color", "white");
        $('#input-pad').delay(250).fadeOut();
    });

});
