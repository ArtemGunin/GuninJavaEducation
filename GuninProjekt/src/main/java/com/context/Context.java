package com.context;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Context {

    private final HashSet<Object> cache = new HashSet<>();

    public void run() {
        cacheClassesSingletonWith();
        cacheClassesConstructorsAutowiredWith();
        printAll();
    }

    private void cacheClassesSingletonWith() {
        final Reflections reflections = new Reflections("com");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Singleton.class);
        annotatedClasses.forEach(aClass -> {
            try {
                if (aClass.getSimpleName().endsWith("Repository")) {
                    Constructor<?> constructor = aClass.getDeclaredConstructor();
                    cache.add(createRepository(constructor));
                } else if (aClass.getSimpleName().endsWith("Service")) {
                    Constructor<?>[] constructorsService = aClass.getDeclaredConstructors();
                    Constructor<?> constructorService = constructorsService[0];
                    cache.add(createService(constructorService));
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    private void cacheClassesConstructorsAutowiredWith() {
        final Reflections reflections = new Reflections("com");
        Set<Constructor> getConstructorsAnnotatedWith = reflections.getConstructorsAnnotatedWith(Autowired.class);
        getConstructorsAnnotatedWith.forEach(aConstructor -> {
            try {
                Object createdObject;
                Object checkedClass = null;
                for (Object hashSetObject : cache) {
                    if (hashSetObject.getClass().getDeclaredConstructor().equals(aConstructor)) {
                        checkedClass = hashSetObject;
                    }
                }
                if (checkedClass == null) {
                    createdObject = switch (aConstructor.getParameterCount()) {
                        case 0 -> createRepository(aConstructor);
                        case 1 -> createService(aConstructor);
                        default -> throw new IllegalArgumentException("Unknown number of constructor parameters!");
                    };
                    if (createdObject.getClass().isAnnotationPresent(Singleton.class)) {
                        cache.add(createdObject);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    private Object createRepository(Constructor<?> constructor) {
        Object createdObject = null;
        try {
            constructor.setAccessible(true);
            createdObject = constructor.newInstance();
            Field instance = createdObject.getClass().getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(createdObject, createdObject);
        } catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchFieldException
                | SecurityException e) {
            e.printStackTrace();
        }
        return createdObject;
    }

    private Object createService(Constructor<?> constructor) {
        constructor.setAccessible(true);
        Pattern pattern = Pattern.compile("\\.([a-zA-Z]+)Service");
        Matcher matcher = pattern.matcher(constructor.getName());
        String productName = "";
        Object createdObject = null;
        if (matcher.find()) {
            productName = matcher.group(1);
        }
        Class<?> repository;
        try {
            repository = Class.forName("com.repository." + productName + "Repository");
            Constructor<?> constructorRepository = repository.getDeclaredConstructor();
            constructorRepository.setAccessible(true);
            Object o = constructorRepository.newInstance();
            createdObject = constructor.newInstance(o);
            Field instance = createdObject.getClass().getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(createdObject, createdObject);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return createdObject;
    }

    public void printAll() {
        cache.forEach(System.out::println);
    }
}
