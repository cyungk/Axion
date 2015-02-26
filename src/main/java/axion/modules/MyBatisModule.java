package axion.modules;

import org.mybatis.guice.XMLMyBatisModule;

public class MyBatisModule extends XMLMyBatisModule {
    @Override
    protected void initialize() {
        setClassPathResource("MapperConfig.xml");
        setEnvironmentId("default");
    }
}
