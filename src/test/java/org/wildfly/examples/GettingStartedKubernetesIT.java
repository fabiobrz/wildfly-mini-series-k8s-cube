package org.wildfly.examples;

import io.fabric8.kubernetes.api.model.Service;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.arquillian.cube.kubernetes.annotations.Named;
import org.arquillian.cube.kubernetes.annotations.PortForward;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URISyntaxException;
import java.net.URL;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Run integration tests on Kubernetes with Arquillian Cube!
 */
@RunWith(Arquillian.class)
public class GettingStartedKubernetesIT {

    @Named("my-jaxrs-app-service")
    @ArquillianResource
    private Service myJaxrsAppService;

    @Named("my-jaxrs-app-service")
    @PortForward
    @ArquillianResource
    private URL url;

    @Test
    public void shouldFindServiceInstance() {
        assertNotNull(myJaxrsAppService);
        assertNotNull(myJaxrsAppService.getSpec());
        assertNotNull(myJaxrsAppService.getSpec().getPorts());
        assertFalse(myJaxrsAppService.getSpec().getPorts().isEmpty());
    }

    @Test
    public void shouldShowHelloWorld() throws URISyntaxException {
        assertNotNull(url);
        try (Client client = ClientBuilder.newClient()) {
            final String name = "World";
            Response response = client
                    .target(url.toURI())
                    .path("/hello/" + name)
                    .request()
                    .get();
            Assert.assertEquals(200, response.getStatus());
            Assert.assertEquals(String.format("Hello '%s'.", name), response.readEntity(String.class));
        }
    }
}
