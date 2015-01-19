=========
PriceList
=========
PriceList is a one-off custom plugin that interfaces with QuickSell's Shop
It will only work if you have a shop called "global" defined in QuickSell config


Also, there's probably a race condition, since PriceList won't wait for
QuickSell's config to be written. It will just complain that it doesn't exist.


Restarting the server will resolve this issue. Fix it if you want, I'm lazy :P

=====
Usage
=====
/quicksell stone


```
PriceList results ---
Cobblestone: 1.3
Stone: 2.5
Sandstone: 0.0
Moss Stone: 1.0
Glowstone: 1.5
Redstone: 0.6

====
Example Config
====

Make sure in QuickSell config.yml you have shops.global.prices.*
```YML
shops:
  global:
    prices:
      COBBLESTONE: 0.0
      GRASS: 0.1
```
