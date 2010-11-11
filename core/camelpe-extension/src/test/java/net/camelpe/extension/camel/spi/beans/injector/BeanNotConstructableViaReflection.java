package net.camelpe.extension.camel.spi.beans.injector;

public class BeanNotConstructableViaReflection {

    @SuppressWarnings("unused")
    private final String constructorArg;

    public BeanNotConstructableViaReflection(final String constructorArg) {
        this.constructorArg = constructorArg;
    }
}
