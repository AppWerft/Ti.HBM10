#TiAirlino

Airlino is a devives for Wifi transmission for in highest level.

This is a Titanium module for handling airlino device.



<img src="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTviFhBPGfPsHhfypRPOjvsGZcid7GyMLuRxgVsroC8wEG05MxuBsXxDcgJxUI_1fiYAL1zoto&usqp=CAE" width=130>"

##Usage

First you can detect all devices in your Wifi:

```javascript
var Airlino = require("de.appwerft.airlino");
Airlino.connect({
    onSuccess: function(_e) {
        Airlino.play("http://…");
        Airlino.stop();
    },
    onError: function() {
        console.log("not found");
    }
});

```