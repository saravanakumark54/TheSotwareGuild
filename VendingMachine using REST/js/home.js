$(document).ready(function() {
    loadItems();
});

function loadItems() {
    $.ajax({
        type: 'GET',
        url: 'http://vending.us-east-1.elasticbeanstalk.com/items',
        success: function(items) {
            $('#items').empty();
            $.each(items, function(index, item) {
                addItem(index, item);
            });
        },
        error: function() {
            $('#errorMessages').empty();
            $('#errorMessages')
            .append($('<li>')
            .attr({class: 'list-group-item list-group-item-danger'})
            .text('Error calling web service. Please try again later.'));
        }
    })
}

function addItem(index, data) {
    var itemId = data.id;
    var entry = '<div class="col-sm-4 d-flex justify-content-around p-0 m-0" onclick=getItemId(' + itemId + ')>';
        entry += '<div class="card w-100 m-1 p-2 border-dark border-2">';
        entry += '<div class="w-100 mb-0">' + data.id + '</div>';
        entry += '<div class="w-100 text-center mb-3">' + data.name + '</div>';
        entry += '<div class="w-100 text-center mb-3">' + displayPrice(data.price) + '</div>';
        entry += '<div class="w-100 text-center mb-3">Quantity Left: ' + data.quantity + '</div>';
        entry += '</div>';
        entry += '</div>';
    $('#items').append(entry);
}
function getItemId(itemId){
$('#itemId').val(itemId);
}


var cash = 0;
const DOLLAR = 1.00;
const QUARTER = 0.25;
const DIME = 0.10;
const NICKEL = 0.05;

function addCash(amount) {
    cash += amount;
    $('#credit').val(cash.toFixed(2));
}
$('#addDollar').click(function(){
    addCash(DOLLAR);
});
$('#addQuarter').click(function(){
    addCash(QUARTER);
});
$('#addDime').click(function(){
    addCash(DIME);
});
$('#addNickel').click(function(){
    addCash(NICKEL);
});

function makePurchase() {
    $.ajax({
        type: 'POST',
        url: 'http://vending.us-east-1.elasticbeanstalk.com/money/' + cash  + '/item/' + $('#itemId').val(),
        success: function(change) {
            loadItems();
            $('#errorMessages').text('Thank You!!!');
            $('#returnButton').hide();
            displayChange(change);
        },
        error: function(error) {
            $('#errorMessages').empty();
            $('#errorMessages')
            .append($('<li>')
            .attr({class: 'list-group-item list-group-item-danger'})
            .text(error.responseJSON.message));
        }
    })
}

function returnChange() {
    var coins = { 'quarters': 0.25, 'dimes': 0.10, 'nickels': 0.05, 'pennies': 0.01 };
    var change = {};

    for (var coin in coins) {
        change[coin] = Math.floor(cash / coins[coin]);
        cash %= coins[coin];
    }

    displayChange(change);
}

function displayChange(change) {
    var display = '&nbsp;';
    for (var coin in change) {
        if (change[coin] > 1) {
            display += change[coin] + ' ' + coin + '&nbsp;';
        }
        if (change[coin] == 1) {
            if (coin == 'pennies') {
                display += change[coin] + ' ' + coin.slice(0, -3) + '&nbsp;';
            }
            else {
                display += change[coin] + ' ' + coin.slice(0, -1) + '&nbsp;';
            }
        }
    }
    cash = 0.00;
    $('#credit').val(displayPrice(cash));
    $('#change').html(display);
    $('#itemId').val('');
}
function displayPrice(money) {
    return '' + String(money.toFixed(2));
}

