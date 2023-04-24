package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderClassLoader extends ClassLoader {

    private final String path;
    private final Map<String, Class<?>> cache = new WeakHashMap<>();
//    private final Map<Integer, Class<?>> hashed = new WeakHashMap<>();

    private FolderClassLoader(String path) {
        this.path = path;
    }

    //TODO подумать нормально ли что мы загружаем сразу все классы? или лучше загружать по одному, когда они требуются?
    public static FolderClassLoader create(String path) {
        FolderClassLoader classloader = new FolderClassLoader(path);
        classloader.fetchAllClasses(path);
        return classloader;
    }

    //TODO почему родительский метод findLoadedClass объявлен как final ???
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (cache.containsKey(name)) {
            System.out.println("Get from cache class " + name);
            return cache.get(name);
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // зараза ломает regExp (при \\. ) или компилятор (при \\\.)
//                String nameLikePath = name.replaceAll("\\.", File.separator);
            String nameLikePath = DotsActionUtil.replaceAllDots(name);
            String pathWithClassName = path + File.separator + nameLikePath + ".class";
            byte[] classFile = Files.readAllBytes(Paths.get(pathWithClassName));
            return this.defineClass(name, classFile, 0, classFile.length);
        } catch (FileNotFoundException e) {
            throw new ClassNotFoundException("Class " + name + " not found");
        } catch (IOException e) {
            throw new ClassNotFoundException(String.format("IOException on reading %s.class", name));
        }
    }
    /*
    javac -target 1.8 -bootclasspath C:\Users\Anton_Lukyanau1\.jdks\corretto-1.8.0_352\lib ExternalPerson.java
    */
    //TODO переписать так, чтобы был метод загрузки одного класса
    public void fetchAllClasses(String location) {
        Path startDirectory = Paths.get(location);
        try (Stream<Path> pathStream = Files.walk(startDirectory)) {
            List<Path> classFiles = pathStream
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".class"))
                    .toList();
            for (Path file : classFiles) {
                Path relativePath = startDirectory.relativize(file);
                String fileName = DotsActionUtil.placeAllDots(relativePath.toString());
                String classFullName = fileName.substring(0, fileName.length() - ".class".length());
                byte[] content = Files.readAllBytes(file);
                Class<?> externalClass = this.defineClass(classFullName, content, 0, content.length);
//                int contentHash = Objects.hash(content);
//                hashed.put(contentHash, externalClass);
                cache.put(classFullName, externalClass);
            }
        } catch (IOException e) {
            System.err.println("Some problem with file reading");
            e.printStackTrace();
        }
    }


    public void printCache() {
        cache.forEach((key, value) -> System.out.printf("%34s - %s\n", key, value.toString()));
    }

}

