package my_spring;

import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    //todo finish this
    // context should cache all objects, which class marked by @Singleton
    // in case object is not singleton, or still not present in cache
    // use ObjectFactory to create

    private ObjectFactory objectFactory;
    private Map<Class, Object> cache = new ConcurrentHashMap<>();
    private Config config;

    public ApplicationContext(Config config) {
        this.config = config;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public <T> T getObject(Class<T> tClass) {
        if (cache.containsKey(tClass)) {
            return (T) cache.get(tClass);
        }
        Class<? extends T> implClass = tClass;
        if (tClass.isInterface()) {
            tClass = config.getImplClass(tClass);
        }
        final T object = objectFactory.createObject(implClass);
        if (implClass.isAnnotationPresent(Singleton.class)) {
            cache.put(implClass, object);
        }
        return object;
    }
}
