CamelPE - A Portable CDI Extension for Apache Camel
===================================================

## Intro

CamelPE is, as its name suggests, a [portable CDI extension](http://docs.jboss.org/weld/reference/1.1.0.Beta1/en-US/html/extend.html) for [Apache Camel](http://camel.apache.org), a lightweight routing engine written in Java. As such, CamelPE offers an alternative configuration mechanism much in the same vein Apache Camel's [Guice module](http://camel.apache.org/guice.html) replaces the default [Spring](http://www.springframework.org) DI container.

It follows that CamelPE does *not* extend Apache Camel's feature set. Instead, it strives to provide a [CDI](http://seamframework.org/Weld)-friendly way of exposing what Apache Camel already has to offer.

## Usage

In its current incarnation, Apache Camel supports mainly three use cases:

### Use CamelPE to adapt Apache Camel to your runtime environment

Apache Camel was designed to be usable in a vast array of diverse environments, each of which may have special requirements that need to be fulfilled by Camel's runtime. For instance, auto-discovery of `TypeConverters` and `RouteBuilders` rests on Camel's ability to scan the classpath. By default, this capability is provided by `DefaultPackageScanClassResolver`, the default implementation of the interface - you guessed it - `PackageScanClassResolver`. Some application server environments, however, have special classloading needs that cause this default implementation to fail. Apache Camel, therefore, defines an [SPI](http://camel.apache.org/maven/camel-2.2.0/camel-core/apidocs/index.html) - **S**ervice **P**rovider **I**nterface - that allows users to swap out default implementations of many core classes for custom ones tailored to the target environment.

CamelPE partially exposes this SPI to the CDI container. It achieves this by introducing a custom [Qualifier](http://docs.jboss.org/weld/reference/1.1.0.Beta1/en-US/html/injection.html#d0e1225), `net.camelpe.api.CamelContextInjectable`. A developer may annotate a custom implementation of one of Camel's SPI's as e.g. PackageScanClassResolver and thus have it injected into the CamelContext.

For a brief usage example take a look [here](https://github.com/obergner/camelpe/blob/master/core/camelpe-extension/src/test/java/net/camelpe/extension/configuration_samples/SamplePackageScanClassResolver.java).

### Use CamelPE to delegate discovery, instantiation, configuration and registration of Routes to your CDI container

This is almost a no-brainer since CamelPE will scan each CDI-enabled module - those sporting a `META-INF/beans.xml` file - for subclasses of Apache Camel's [RouteBuilder](http://camel.apache.org/maven/camel-2.2.0/camel-core/apidocs/org/apache/camel/builder/RouteBuilder.html), instantiate and configure those like any other CDI bean, and will register the set of discovered `RouteBuilders` in the `CamelContext` immediately prior to starting it.

Maybe the only caveat you should be aware of is that you cannot make a `RouteBuilder` `@ApplicationScoped` since it does not declare a public no-args constructor and thus [cannot be proxied](http://docs.jboss.org/weld/reference/1.1.0.Beta1/en-US/html/injection.html#d0e1439). While this may seem conceptually dissatisfying - it certainly seemed so to me - in practice this should rarely if ever be a problem. Routes discovery and registration is a one-time process executed at system startup. So while not `ApplicationScoped` in the strict CDI sense, `RouteBuilder` instances will nonetheless exist for the lifetime of the application.

For a brief usage example take a look [here](https://github.com/obergner/camelpe/blob/master/core/camelpe-extension/src/test/java/net/camelpe/extension/advanced_samples/AdvancedRoutes.java).

### Use CamelPE to delegate discovery, instantiation, configuration and registration of TypeConverters to your CDI container

Easy. Just annotate your TypeConverters with [`org.apache.camel.Converter`](http://camel.apache.org/maven/camel-2.2.0/camel-core/apidocs/org/apache/camel/Converter) or [`org.apache.camel.FallbackConverter`](http://camel.apache.org/maven/camel-2.2.0/camel-core/apidocs/org/apache/camel/FallbackConverter) and you are good to go. CamelPE will discover, instantiate and configure those just like any other CDI bean, and will take care of registering them in the `CamelContext`.

You should, however, take care *not* to use Apache Camel's standard mechanism of listing packages containing TypeConverters in `META-INF/services/org/apache/camel/TypeConverter`. Otherwise, they will be registered *twice* in the CamelContext, once by CamelPE and then again by Apache Camel's [`AnnotationTypeConverterLoader`](http://camel.apache.org/maven/camel-2.2.0/camel-core/apidocs/org/apache/camel/impl/converter/AnnotationTypeConverterLoader.html). CamelPE does *not* disable the latter since it is required to load the set of built-in `TypeConverters` which are essential to Apache Camel's working properly.

What may seem counterintuitive is that you have to apply org.apache.camel.Converter *twice*, once on the class level and then again on the method level. While this is strictly speaking not necessary, it is in keeping with the restrictions imposed by Apache Camel itself, and I consciously chose not to relax this requirement.

For a brief usage example take a look [here](https://github.com/obergner/camelpe/blob/master/core/camelpe-extension/src/test/java/net/camelpe/extension/advanced_samples/DateToLongConverter.java).

## Building

Check out the source with

	$ git clone git://github.com/obergner/camelpe.git

This will create a new subdirectory `camelpe` in the current directory, containing the CamelPE multi-module project. In the module `camelpe/project` you will find [`settings-sample.xml`](https://github.com/obergner/camelpe/blob/master/project/settings-sample.xml). Copy the profile `net.camelpe.profile` into Maven's user-level `settings.xml`, usually found in `~/.m2/settings.xml`. You need to do this because CamelPE does consciously *not* declare any repositories in its master pom. For an explanation for why this is usually (though not always) a bad idea look [here](http://www.sonatype.com/people/2009/02/why-putting-repositories-in-your-poms-is-a-bad-idea/).

Now, after 

	$ cd camelpe/project

you may build CamelPE with e.g.

	$ mvn -Pnet.camelpe.profile clean install

and you're done. 
