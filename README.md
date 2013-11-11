zfs-java-wrapper
================

[![Build Status](https://consolving.de/jenkins/buildStatus/icon?job=de.javastream.zfs-java-wrapper)](https://consolving.de/jenkins/job/de.javastream.zfs-java-wrapper/)

This is a basic java-wrapper for zfs.
There were a JNA Version of it (at least for the original ZFS - aka on Solaris), but it is not well maintenanced and modifying the JNA Bindings for every derivate (Solaris / Illumos / FreeBSD / FreeNAS...) is very time consuming.
So this Library will using the CLI Tools and then will parse the output.
There are some Text Snippets for that in _src/test/resources_.
Although most parsing works, there are some more minor errors.
