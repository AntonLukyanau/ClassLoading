package org.example;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        String strWithDots = "vv.evrevre.ervr.Fgrgr";
//        String regExp = new StringBuilder()
//                .append('\\')
//                .append('.').toString();
//        String strWithSeparators = strWithDots.replaceAll(regExp, File.separator);
//        System.out.println(strWithSeparators);

        String location = "C:\\Users\\Anton_Lukyanau1\\Documents\\learning\\JAT\\classloader_sources";
        String location2 = "C:\\Users\\Anton_Lukyanau1\\Documents\\learning\\JAT\\classloader_sources_copy";

        FolderClassLoader myLoader = FolderClassLoader.create(location);
//        myLoader.fetchAllClasses(location2);
        FolderClassLoader myLoader2 = FolderClassLoader.create(location2);
        myLoader.printCache();
        System.out.println("---------------------------------------------------");
        myLoader2.printCache();

        loadExternalPersonBy(myLoader);
        loadExternalPersonBy(myLoader2);
//        myLoader2.printCache();
//        System.out.println("fill the heap");
//        List<Main> trash = new LinkedList<>();
//        for (long i = 0; i < 10000L; i++) {
//            trash.add(new Main());
//        }
//        System.out.println("_________________");
//        myLoader.printCache();
//        Class<?> clazz = myLoader.loadClass("other.ICanPayCalculate");
//        System.out.println(clazz.isInterface() ? "Loaded interface!" : "This is NOT an interface!");
//        Arrays.stream(clazz.getConstructors()).forEach(System.out::println);
//        Arrays.stream(clazz.getDeclaredFields()).forEach(System.out::println);
//        Arrays.stream(clazz.getDeclaredMethods()).forEach(System.out::println);

    }

    private static void loadExternalPersonBy(FolderClassLoader myLoader) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> externalPerson1 = myLoader.loadClass("ExternalPerson");
        Constructor<?> constructor1 = externalPerson1.getConstructor();
        Object person1 = constructor1.newInstance();
        Method getName = externalPerson1.getMethod("getName");
        System.out.println(getName.invoke(person1));
    }
}