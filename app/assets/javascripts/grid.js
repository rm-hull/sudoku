$(document).ready(function() {

    var isIPad = navigator.userAgent.match(/iPad/i) != null;
    var currentCell;

    var successHandler = function(data, textSuccess, jqXHR) {
        console.log(data);
        $('.box td').each(function(idx, elem) {
            var $elem = $(elem);
            if($elem.text() === " " && data.grid[idx] !== " ") {
                $elem.text(data.grid[idx]).css("color", "#80C846");
            }
        });
    }

    var errorHandler = function(e) {
        var response = $.parseJSON(e.responseText);
        console.log(response);
        alert(response.message);
    }

    $('#solve-button').bind('click', function(e) {
        e.preventDefault();
        $('input[type=submit]').attr('disabled', true);
        var spinner = $('#spinner');
        if (!spinner.is(":visible")) {
            spinner.show();
            $.getJSON( "/solve", { cells: $('.box td').text() })
             .done(successHandler)
             .fail(errorHandler)
             .always(function() { 
                spinner.hide();
                $('input[type=submit]').attr('disabled', false);
              });
        }
    });

    $('#clear-button').bind('click', function(e) {
        e.preventDefault();
        $('.box td').each(function(idx, elem) {
            $(elem).text(" ").css("color", "");
        });
    });

    $('#reset-button').bind('click', function(e) {
        e.preventDefault();
        $('.box td').each(function(idx, elem) {
            var $elem = $(elem)
            $elem.text($elem.data("initial")).css("color", "");
        });
    });

    $('#new-button').bind('click', function(e) {
        e.preventDefault();
        location.href = '/';
    });

    $('.box td').bind('click', function(e) {
        e.preventDefault();
        if (currentCell != null) {
            currentCell.css("background-color", "white");
        }
        currentCell = $(this);
        currentCell.css({"background-color": "#FAF0E6", "color": ""});
        if (isIPad) {
            $('#input-pad').show();
        } else {
            $('#input-pad').fadeIn();
        }
    });

    $('#input-pad td,#input-pad th').bind('click', function(e) {
        e.preventDefault();
        var digit = $(this).text();
        if (digit != "Cancel") {
            currentCell.text(digit === "Clear" ? " " : digit);
        }
        currentCell.css("background-color", "white");
        if (isIPad) {
            $('#input-pad').delay(250).hide();
        } else {
            $('#input-pad').delay(250).fadeOut();
        }
    });

});
