JPDuckEncoder
=============
Based off of the [duckencoder](http://www.usbrubberducky.com/wiki/doku.php?id=downloads) written by Jason Appelbaum, jpduckencoder aims to fix bugs reported by the community and add feature enhancements.


Changelog
---------
### v0.1 ###
* Ported from v1.0 of Hack5 duckencoder
* Adds in --replacern flag allowing users with text editors that use CRLF line encodings to strip out CR
* Fixed an issue where DELAYs were not being set correctly because of improper type conversion from String to Byte