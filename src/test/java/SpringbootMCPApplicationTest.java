import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.Duration;

@Slf4j
public class SpringbootMCPApplicationTest {
    @Test
    void testCient() {
        String jarFile = "/Users/johnbrinnand/IdeaProjects/springboot-mcp-server/target/springboot-mcp-server-1.0-SNAPSHOT.jar";
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", jarFile,
                      "--spring.ai.mcp.server.stdio=true")
                .build();

        var stdioTransport = new StdioClientTransport(stdioParams);
        // Create a sync client with custom configuration
        McpSyncClient client = McpClient.sync(stdioTransport)
                .requestTimeout(Duration.ofSeconds(30))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)      // Enable roots capability
                        .sampling()       // Enable sampling capability
                        .build())
                .sampling(request ->
                        McpSchema.CreateMessageResult.builder()
                                .message(String.valueOf(request.messages().getFirst()))
                                .build()
                )
                .build();

        // Initialize connection
        client.initialize();

        // List available tools
        final McpSchema.ListToolsResult listToolsResult = client.listTools();
        Assert.assertNotNull(listToolsResult);

        log.info("Available tools: {}", listToolsResult.tools());
        listToolsResult.tools().forEach(tool ->
            log.info("Tool: {}  Description: {}", tool.name(), tool.description())
        );
        client.closeGracefully();
    }
    @Test
    void testCientGetAllPeople() {
        ObjectMapper om = new ObjectMapper();
        String jarFile = "/Users/johnbrinnand/IdeaProjects/springboot-mcp-server/target/springboot-mcp-server-1.0-SNAPSHOT.jar";
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", jarFile,
                        "--spring.ai.mcp.server.stdio=true")
                .build();

        var stdioTransport = new StdioClientTransport(stdioParams);
        // Create a sync client with custom configuration
        McpSyncClient client = McpClient.sync(stdioTransport)
                .requestTimeout(Duration.ofSeconds(30))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)      // Enable roots capability
                        .sampling()       // Enable sampling capability
                        .build())
                .sampling(request ->
                        McpSchema.CreateMessageResult.builder()
                                .message(String.valueOf(request.messages().getFirst()))
                                .build()
                )
                .build();

        // Initialize connection
        client.initialize();

        // List available tools
        final McpSchema.ListToolsResult listToolsResult = client.listTools();
        Assert.assertNotNull(listToolsResult);

        log.info("Available tools: {}", listToolsResult.tools());
        listToolsResult.tools().forEach(tool ->
                {
                    try {
                        log.info("Tool: {}  \n Description: {} \n Schema {}", tool.name(), tool.description(),
                                om.writerWithDefaultPrettyPrinter().writeValueAsString(tool.inputSchema()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        client.closeGracefully();
    }
}
