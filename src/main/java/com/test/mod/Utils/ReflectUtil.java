//===============================================//
//重新编译已禁用.请用JDK运行Recaf//
//===============================================//

// Decompiled with: FernFlower
// Class Version: 8
package com.test.mod.Utils;

import com.test.mod.Client;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectUtil {

    public static Object invoke(Object target, String methodName, String obfName, Class[] methodArgs, Object[] args) {
        Class clazz = target.getClass();
        Method method = ReflectionHelper.findMethod(clazz, target, Client.isObfuscate ? new String[]{obfName} : new String[]{methodName}, methodArgs);
        method.setAccessible(true);

        try {
            return method.invoke(target, args);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static Object getField(String field, String obfName, Class clazz) {
        Field fField = ReflectionHelper.findField(clazz, new String[]{field, obfName});
        fField.setAccessible(true);

        try {
            return fField.get(clazz);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static Object getField(String field, String obfName, Object instance) {
        Field fField = ReflectionHelper.findField(instance.getClass(), Client.isObfuscate ? new String[]{obfName} : new String[]{field});
        fField.setAccessible(true);

        try {
            return fField.get(instance);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class class_, String... arrstring) {
        for(Field field : class_.getDeclaredFields()) {
            field.setAccessible(true);

            for(String string : arrstring) {
                if (field.getName().equals(string)) {
                    return field;
                }
            }
        }

        return null;
    }


    public static void setField(Class targetClass, Object instance, Object newValue, boolean isFinal, String... arrstring) {
        Field strField = ReflectionHelper.findField(targetClass, arrstring);
        strField.setAccessible(true);

        try {
            if (isFinal) {
                Field modField = Field.class.getDeclaredField("modifiers");
                modField.setAccessible(true);
                modField.setInt(strField, strField.getModifiers() & -17);
            }

            strField.set(instance, newValue);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static void setField(String string, String obfName, Object instance, Object newValue, boolean isFinal) {
        Field strField = ReflectionHelper.findField(instance.getClass(), new String[]{string, obfName});
        strField.setAccessible(true);

        try {
            if (isFinal) {
                Field modField = Field.class.getDeclaredField("modifiers");
                modField.setAccessible(true);
                modField.setInt(strField, strField.getModifiers() & -17);
            }

            strField.set(instance, newValue);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static void setField(String string, String obfName, Class targetClass, Object instance, Object newValue, boolean isFinal) {
        Field strField = ReflectionHelper.findField(targetClass, new String[]{string, obfName});
        strField.setAccessible(true);

        try {
            if (isFinal) {
                Field modField = Field.class.getDeclaredField("modifiers");
                modField.setAccessible(true);
                modField.setInt(strField, strField.getModifiers() & -17);
            }

            strField.set(instance, newValue);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }
}
 