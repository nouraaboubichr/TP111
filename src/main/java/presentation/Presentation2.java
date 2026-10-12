package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class Presentation2 {

    public static void main(String[] args) {
        try {
            // 1. Lecture de config.txt (classpath)
            String[] classNames = loadClassNames("config.txt");

            // 2. Instanciation dynamique
            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());

            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());

            // 3. Injection de dépendance par réflexion
            injectDependency(metier, "setDao", dao);

            // 4. Affichage de la valeur du DAO (100.0)
            System.out.println("Valeur DAO = " + dao.getValue());

            // 5. Affichage du résultat métier (200.0)
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        InputStream is = Presentation2.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new FileNotFoundException(fileName + " introuvable dans le classpath");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            for (int i = 0; i < classNames.length; i++) {
                String line = reader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    throw new IOException("config.txt : ligne " + (i + 1) + " manquante ou vide");
                }
                classNames[i] = line.trim();
            }
        }
        return classNames;
    }

    private static Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    private static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equals(setterName)
                    && m.getParameterCount() == 1
                    && m.getParameterTypes()[0].isAssignableFrom(dependency.getClass())) {
                m.invoke(target, dependency);
                return;
            }
        }
        throw new NoSuchMethodException(setterName + " compatible avec " + dependency.getClass().getName());
    }
}