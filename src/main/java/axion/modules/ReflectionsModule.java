package axion.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import org.reflections.Reflections;
import org.reflections.scanners.*;

public class ReflectionsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Reflections.class).toProvider(ReflectionsProvider.class).in(Scopes.SINGLETON);
    }

    public static class ReflectionsProvider implements Provider<Reflections> {

        @Override
        public Reflections get() {
            return new Reflections("axion.domain", new MethodAnnotationsScanner(),
                    new TypeAnnotationsScanner(), new TypeElementsScanner(), new SubTypesScanner());
        }
    }
}
