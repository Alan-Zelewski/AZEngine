package pl.engine.az.ecs;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import pl.engine.az.ecs.component.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentScanner {

    @SuppressWarnings("unchecked")
    public static List<Class<? extends Component>> findComponents(String packageName) {
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().acceptPackages(packageName).scan()) {
            ClassInfoList componentClasses = scanResult.getClassesImplementing(Component.class);
            List<Class<? extends Component>> resultList = new ArrayList<>();
            for (ClassInfo classInfo : componentClasses) {
                resultList.add((Class<? extends Component>) classInfo.loadClass());
            }
            return resultList;
        }
    }
}
