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
        Airlino.playStation({
            url:"http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m,
            station : "Deutschlandfunk"});
        Airlino.setFavoriteStation({
            url:"http://dradio_mp3_dlf_m.akacast.akamaistream.net/7/249/142684/v1/gnl.akacast.akamaistream.net/dradio_mp3_dlf_m,
            station : "Deutschlandfunk"
        });
        Airlino.stop();
        Airlino.getCurrentStation({
            onSuccess: function(_e) {
            }
        });
        Airlino.getFavoriteStation({
            onSuccess: function(_e) {
            }
        });
        Airlino.getPlaylist({
            onSuccess: function(_e) {
            }
        });
    },
    onError: function() {
        console.log("not found");
    }
});

```