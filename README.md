#TiAirlino

Airlino is a devives for Wifi transmission for in highest level.

This is a Titanium module for handling airlino device.



<img src="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTviFhBPGfPsHhfypRPOjvsGZcid7GyMLuRxgVsroC8wEG05MxuBsXxDcgJxUI_1fiYAL1zoto&usqp=CAE" width=130>"

##Usage


```javascript
var URL = "http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m",
var Airlino = require("de.appwerft.airlino");
var devices = [];

// test if at leat one is available (i.e. for activating button)
Airlino.isAvailable({
    timeout : 2000,
    onResult : function(e) {
        console.log(e.found); // true or false
    }
})

Airlino.startScan({
    onSuccess: function(e) {
        devices = e.devices;
        devices.forEach(function(device){
            console.log(device.name + " " + device.endpoint);
        });
    },
    onError: function() {
        console.log("not found");
    }
});

Airlino.stopScan();


Airlino.play({
    device : device[0],
    url : URL
});

```