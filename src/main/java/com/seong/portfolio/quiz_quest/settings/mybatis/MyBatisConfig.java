package com.seong.portfolio.quiz_quest.settings.mybatis;




import com.seong.portfolio.quiz_quest.settings.mybatis.handler.SetObjectTypeHandler;
import com.seong.portfolio.quiz_quest.settings.mybatis.handler.StringListTypeHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

@Configuration
public class MyBatisConfig {

    @Value("${spring.datasource.mapper-locations}")
    String mPath;

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource DataSource()
    {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name="SqlSessionFactory")
    public SqlSessionFactory SqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mPath));

        sqlSessionFactoryBean.setConfiguration(createMyBatisConfiguration());

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name="SessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private org.apache.ibatis.session.Configuration createMyBatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register(List.class, new StringListTypeHandler());
        registry.register(Set.class, new SetObjectTypeHandler());
        return configuration;
    }


}
