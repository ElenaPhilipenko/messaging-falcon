function publishMessage(url, msg) {
    $.ajax({
        url: url,
        data: JSON.stringify({message: msg}),
        contentType: "application/json; charset=utf-8",
        type: 'POST',
        dataType: 'json',
        success: function (res) {
            console.log("published");
        }
    })
}

function subscribeForMessages(subscribePath, messageHandler) {
    function composeSubscribeUrl(path) {
        var location = window.location;
        return ( ( location.protocol === "https:" ) ? "wss://" : "ws://" )
            + location.hostname + ( ( ( location.port != 80 ) && ( location.port != 443 ) ) ? ":"
            + location.port : "" ) + path.trim();
    }

    if ("WebSocket" in window) {
        var ws = new WebSocket(composeSubscribeUrl(subscribePath));
        ws.onmessage = function (evt) {
            messageHandler(evt.data);
        };
    } else {
        alert("WebSocket NOT supported by your Browser!");
    }
}

function removeAllMessages(url, callback) {
    $.ajax({
        url: url,
        type: 'GET',
        success: function (res) {
            console.log("Messages were removed");
            callback();
        }

    })
}

