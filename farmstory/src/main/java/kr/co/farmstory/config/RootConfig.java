package kr.co.farmstory.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    @Autowired
    private BuildProperties buildProperties;
    //그래들 정보가 이쪽으로 주입됨

    @Bean
    public  AppInfo appInfo(){
        String name = buildProperties.getName();
        String version = buildProperties.getVersion();

        return new AppInfo(name, version);
    }
    @Bean
    public ModelMapper modelMapper(){

        // Entity의 @Setter 선언없이 바로 private 속성으로 초기화 설정
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


}