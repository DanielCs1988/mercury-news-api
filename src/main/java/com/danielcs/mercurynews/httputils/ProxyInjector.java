package com.danielcs.mercurynews.httputils;

import com.danielcs.mercurynews.services.HttpRequest;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Set;

@Component
public class ProxyInjector {

    /**
     * This wrapper is needed because I cannot access Spring's component pool directly to inject my proxy object,
     * which is ambiguous in it's nature, so Spring could not inject it properly.
     */
    private HttpRequest wrapperClass;

    public ProxyInjector(HttpRequest wrapperClass, RequestInvoker requestInvoker) {
        this.wrapperClass = wrapperClass;
        scanClasspath(requestInvoker);
    }

    private void scanClasspath(RequestInvoker requestInvoker) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.danielcs.mercurynews"))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner())
        );
        Set<Class<?>> assemblers = reflections.getTypesAnnotatedWith(HttpRequestAssembler.class);
        Set<Field> injectionPoints = reflections.getFieldsAnnotatedWith(HttpRequestAssembler.class);
        Class[] proxyClasses = assemblers.toArray(new Class[0]);
        Object proxy = Proxy.newProxyInstance(
                ProxyInjector.class.getClassLoader(),
                proxyClasses,
                requestInvoker
        );
        for (Field ip : injectionPoints) {
            ip.setAccessible(true);
            try {
                ip.set(wrapperClass, ip.getType().cast(proxy));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
