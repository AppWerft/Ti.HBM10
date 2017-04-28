# TiAirlino HBM10
 
Airlino is a devives for Wifi transmission for in highest level. This device is embedded as OEM (HBM10) in a lot of active HiFi boxes. The spec you [can read here](https://github.com/AppWerft/Ti.Airlino/blob/master/SPEC_HBM10-v4.2.0.pdf)

<img src="http://i.imgur.com/yoUFziR.png" width=600 />

This is a Titanium module for handling airlino device.



<img src="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTviFhBPGfPsHhfypRPOjvsGZcid7GyMLuRxgVsroC8wEG05MxuBsXxDcgJxUI_1fiYAL1zoto&usqp=CAE" width=150>"


### Testing
You can test the Bonjour browsing without device with
```sh
dns-sd -R NAME _dockset._tcp. local PORTNUMBER
```
i.e you start on your Mac, Raspi or what ever.

## Usage


```javascript
var URL = "http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m",
var Airlino = require("ti.airlino");
var devices = [];

// test if at least one device is available (i.e. for activating button)

airlinoBrowser = Airlino.createDiscoveryResolver({
	dnstype : "dockset", // for Airlino and OEMs
	onchange : function(e) {
		console.log(e);
	}
});

airlinoBrowser.start();
    
// later, maybe if dialog is closed
airlinoBrowser.stop();
```
### Typical answer of airlino devices (type :"dockset")
```javascript
{
	"dnstype" : "dockset",
	"name" : "airlino",
	"ip" : "192.168.178.53",
	"port" : 8989,
	"fqdn" : "airlino._dockset._tcp.local",
	"txt" : {
		"swver" : "4.1.0",
		"model" : "AirLino",
		"api" : "v14"
	},
	"ttl" : 10,
	"target" : "AirLino-FB22.local"
}
```

### Typical answer of chromecast devices (type :"googlecast")
```javascript
{
	"dnstype" : "googlecast",
	"name" : "Chromecast-8fd9d3e607e2ee11bee877c8cd5008b8",
	"ip" : "192.168.178.22",
	"port" : 8009,
	"fqdn" : "Chromecast-8fd9d3e607e2ee11bee877c8cd5008b8._googlecast._tcp.local",
	"txt" : {
		"st" : "0",
		"md" : "Chromecast",
		"id" : "8fd9d3e607e2ee11bee877c8cd5008b8",
		"fn" : "Elysium",
		"ve" : "05",
		"rs" : "",
		"ic" : "\/setup\/icon.png",
		"bs" : "FA8FCA536F74",
		"rm" : "",
		"ca" : "4101"
	},
	"ttl" : 120,
	"target" : "8fd9d3e6-07e2-ee11-bee8-77c8cd5008b8.local"
}
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
