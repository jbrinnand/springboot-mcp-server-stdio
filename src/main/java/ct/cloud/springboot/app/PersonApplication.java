package ct.cloud.springboot.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
public class PersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonApplication.class, args);
    }

//    @Bean
//    public ToolCallbackProvider tools(PersonService personService) {
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(personService)
//                .build();
//    }
    @Bean
    public List<ToolCallback> toolCallbacks(PersonService personService) {
        final List<ToolCallback> agents = List.of(ToolCallbacks.from(personService));
//        log.info("toolCallbacks: {}", Arrays.stream(agents.toArray()).toList());
        return agents;
    }
}
