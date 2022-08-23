package com.context;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            Arrays.stream(constructors)
                    .forEach(constructor -> {
                        switch (constructor.getParameterCount()) {
                            case 0 -> cache.add(createRepository(constructor));
                            case 1 -> cache.add(createService(constructor));
                            default -> throw new IllegalArgumentException("Correct constructor don't present!");
                        }
                    });
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
        Object createdObject = null;
        Object repository = null;
        Class<?>[] parameterType = constructor.getParameterTypes();
        for (Object hashSetObject : cache) {
            if (hashSetObject.getClass().equals(Class.class)) {
                repository = hashSetObject;
            }
        }
        if (repository == null) {
            try {
                Constructor<?> repositoryConstructor = parameterType[0].getDeclaredConstructor();
                repositoryConstructor.setAccessible(true);
                repository = createRepository(repositoryConstructor);
                createdObject = constructor.newInstance(repository);
                Field instance = createdObject.getClass().getDeclaredField("instance");
                instance.setAccessible(true);
                instance.set(createdObject, createdObject);
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | NoSuchFieldException
                    | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return createdObject;
    }

    public void printAll() {
        cache.forEach(System.out::println);
    }
}
