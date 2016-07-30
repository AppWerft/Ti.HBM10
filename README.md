#TiAirlino

This is a Titanium module for handling airlino device.



<img src="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTviFhBPGfPsHhfypRPOjvsGZcid7GyMLuRxgVsroC8wEG05MxuBsXxDcgJxUI_1fiYAL1zoto&usqp=CAE" width=130>"

##Usage

First you can detect all devices in your Wifi:

```javascript
var Airlino = require("de.appwerft.airlino");
Airlino.getAllDevices(function(_e) {
    Airlino.setDevice(_e.devices[0]);
    Airlino.play("http://…");
});

```