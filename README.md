#TiAirlino HBM10
 
Airlino is a devives for Wifi transmission for in highest level. This device is embedded as OEM (HBM10) in a lot of active HiFi boxes. The spec you [can read here](https://github.com/AppWerft/Ti.Airlino/blob/master/SPEC_HBM10-v4.2.0.pdf)

<img src="http://i.imgur.com/yoUFziR.png" width=600 />

This is a Titanium module for handling airlino device.



<img src="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTviFhBPGfPsHhfypRPOjvsGZcid7GyMLuRxgVsroC8wEG05MxuBsXxDcgJxUI_1fiYAL1zoto&usqp=CAE" width=150>"


###Testing
You can test the Bonjour browsing without device with
```sh
dns-sd -R NAME _dockset._tcp. local PORTNUMBER
``


##Usage


```javascript
var URL = "http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m",
var Airlino = require("de.appwerft.airlino");
var devices = [];

// test if at leat one is available (i.e. for activating button)

airlinoBrowser = Airlino.createBrowser();
airlinoBrowser.isAvailable({
    timeout : 2000,
    onResult : function(e) {
        console.log(e.found); // true or false
    }
})

airlinoBrowser.startScan({
    onSuccess: function(e) {
        devices = e.devices;
        devices.forEach(function(device){
            console.log(device.name + " " + device.host +":"+ device.port);
        });
    },
    onError: function() {
        console.log("not found");
    }
});

airlinoBrowser.stopScan();
```

Now you have a list of available devices. You can show an UI for user selection. After this you can:
```javascript
var airlinoConnection = Airlino.createConnection(device.endpoint);

airlinoConnection.playRadio({
    url : "http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m",
    name : "Deutschlandfunk Köln",
    onResult : function(){
        
    });
airlinoConnection.stopRadio();

airlinoConnection.deviceStatus({
    onResult ; function(res) {
        console.log(res);
    }
}));

```
<img src="http://i.imgur.com/nxZSfPp.png" width=600 />