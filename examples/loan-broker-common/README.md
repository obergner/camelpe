CamelPE - Loan Broker Example on CDI
====================================

This is a simplified version of the loan broker example that ships with Apache
Camel. All credits go to the [Apache Camel](http://camel.apache.org) team.

I have modified it to serve as a demo for how to use CamelPE in a CDI environ-
ment for a somewhat less than trivial use case. Contrary to the original version,
this one is not a java executable. Instead, the driver is implemented as a unit
test based on [JBoss Arquillian](http://www.jboss.org/arquillian). If you haven't
done so yet, I urge you to check this cool project out. In-container testing has
just become a lot easier, though still not exactly what I would call trivial.
Be that as it may, it's a leap forward. 

The original Camel version, in turn, is an implementation of the (almost)
classical loan broker example as laid out in the (definitely) classical EIP book.

Of the two versions found in Apache Camel, one based on JMS queues, the other
using web services, only the first one survived. My condolences to the web service
example's relatives.

Documentation for the original Camel example can be found 
[here](http://camel.apache.org/loan-broker-example.html).
