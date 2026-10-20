Positive matches
1. DispatcherServletAutoConfiguration matched:
   - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet' (OnClassCondition)
   - found 'session' scope (OnWebApplicationCondition)
2. JacksonAutoConfiguration matched:
   - @ConditionalOnClass found required class 'tools.jackson.databind.json.JsonMapper' (OnClassCondition)
3. TomcatServletWebServerAutoConfiguration matched:
   - @ConditionalOnClass found required classes 'jakarta.servlet.ServletRequest', 'org.apache.catalina.startup.Tomcat', 'org.apache.coyote.UpgradeProtocol', 'org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory' (OnClassCondition)
   - found 'session' scope (OnWebApplicationCondition)

1. DispatcherServletAutoConfiguration
Сработала, потому что Spring Boot обнаружил класс DispatcherServlet и определил, что приложение является веб-приложением. Отвечает за обработку HTTP-запросов.
2. JacksonAutoConfiguration
Сработала, потому что Spring Boot обнаружил класс JsonMapper из библиотеки Jackson. Используется для преобразования Java-объектов в JSON.
3. TomcatServletWebServerAutoConfiguration
Сработала, потому что Spring Boot нашёл необходимые классы Tomcat и определил, что приложение является веб-приложением. Позволяет автоматически настроить встроенный веб-сервер Tomcat.

Negative matches

1. GsonHttpMessageConvertersConfiguration:
    - Did not match:
    - @ConditionalOnClass did not find required class 'com.google.gson.Gson' (OnClassCondition)
2. JacksonAutoConfiguration.XmlConfiguration:
    - Did not match:
    - @ConditionalOnClass did not find required class 'tools.jackson.dataformat.xml.XmlMapper' (OnClassCondition)
3. JmxAutoConfiguration:
    - Did not match:
    - @ConditionalOnBooleanProperty (spring.jmx.enabled=true) did not find property 'spring.jmx.enabled' (OnPropertyCondition)

1. GsonHttpMessageConvertersConfiguration
Не сработала, потому что Spring Boot не обнаружил класс Gson.
2. JacksonAutoConfiguration.XmlConfiguration
Не сработала, потому что Spring Boot не обнаружил класс XmlMapper.
3. JmxAutoConfiguration
Не сработала, потому что Spring Boot не нашёл настройку spring.jmx.enabled=true.

