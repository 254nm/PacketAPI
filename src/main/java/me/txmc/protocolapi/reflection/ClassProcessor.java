package me.txmc.protocolapi.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassProcessor {

    public static void process(Object o) {
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                if (field.getType() == Field.class && field.isAnnotationPresent(GetField.class)) {
                    setAccessible(field);
                    GetField annotation = field.getDeclaredAnnotation(GetField.class);
                    Class<?> clazz = annotation.clazz();
                    Field annoField = clazz.getDeclaredField(annotation.name());
                    if (annoField == null)
                        throw new NoSuchMethodException(String.format("Field %s in class %s is not present", annotation.name(), clazz.getName()));
                    setAccessible(annoField);
                    field.set(o, annoField);
                } else if (field.getType() == Method.class && field.isAnnotationPresent(GetMethod.class)) {
                    setAccessible(field);
                    GetMethod annotation = field.getDeclaredAnnotation(GetMethod.class);
                    Class<?> clazz = annotation.clazz();
                    Method method = clazz.getDeclaredMethod(annotation.name(), annotation.sig());
                    if (method == null)
                        throw new NoSuchMethodException(String.format("Method %s in class %s with parameters %s is not present", annotation.name(), clazz.getName(), Arrays.toString(annotation.sig())));
                    setAccessible(method);
                    field.set(o, method);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void setAccessible(AccessibleObject object) throws Throwable {
        Field override = AccessibleObject.class.getDeclaredField("override"); //Do it like this because bukkit tries to prevent you from reflecting into nms
        override.setAccessible(true);
        override.set(object, true);
    }

    public static boolean hasAnnotation(Object obj) {
        for (Field declaredField : obj.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(GetField.class) || declaredField.isAnnotationPresent(GetMethod.class)) return true;
        }
        return false;
    }
}
