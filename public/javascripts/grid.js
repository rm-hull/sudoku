$(document).ready(function() {

    var currentCell;

    $('#solve-button').click(function(e) {
        e.preventDefault();
        alert("Coming soon");
    });

    $('#clear-button').click(function(e) {
        e.preventDefault();
        $('.box td').each(function(idx, elem) {
            $(elem).text("");
        });
    });

    $('.box td').click(function(e) {
        e.preventDefault();
        currentCell = $(this);
        $('.box td').css("background-color", "white");
        currentCell.css("background-color", "#FAF0E6");
        $('#input-pad').fadeIn();
    });

    $('#input-pad td,#input-pad th').click(function(e) {
        e.preventDefault();
        var digit = $(this).text();
        if (digit != "Cancel") {
            currentCell.text(digit == "Clear" ? "" : digit);
        }
        currentCell.css("background-color", "white");
        $('#input-pad').delay(250).fadeOut();
    });
});
