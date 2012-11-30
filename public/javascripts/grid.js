$(document).ready(function() {

    $('#spinner').hide();
    
    var currentCell;

    var successHandler = function(data, textSuccess, jqXHR) {
        console.log(data);
        $('.box td').each(function(idx, elem) {
            var $elem = $(elem);
            if($elem.text() === " " && data.grid[idx] !== " ") {
                $elem.text(data.grid[idx]);
                $elem.css("color", "blue");
            }
        });
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
        if (currentCell != null) {
            currentCell.css("background-color", "white");
        }
        currentCell = $(this);
        currentCell.css("background-color", "#FAF0E6");
        $('#input-pad').show();
    });

    $('#input-pad td,#input-pad th').click(function(e) {
        e.preventDefault();
        var digit = $(this).text();
        if (digit != "Cancel") {
            currentCell.text(digit === "Clear" ? " " : digit);
        }
        currentCell.css("background-color", "white");
        $('#input-pad').delay(250).fadeOut();
    });

});
