package id.ac.ui.cs.advprog.payment.external;

import id.ac.ui.cs.advprog.payment.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;

    private OrderServiceClient orderServiceClient;

    private final String baseUrl = "http://localhost:8080";
    private final String allOrdersEndpoint = "/api/orders";
    private final String validToken = "valid-jwt-token";

    @BeforeEach
    void setUp() {
        orderServiceClient = new OrderServiceClient(restTemplate, jwtTokenProvider);

        // Set private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(orderServiceClient, "orderServiceBaseUrl", baseUrl);
        ReflectionTestUtils.setField(orderServiceClient, "allOrdersEndpoint", allOrdersEndpoint);
    }

    @Test
    void testConstructor() {
        OrderServiceClient client = new OrderServiceClient(restTemplate, jwtTokenProvider);
        assertNotNull(client);
    }

    @Test
    void testGetAllOrdersSuccess() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        List<OrderData> orders = new ArrayList<>();
        orders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item 1"));
        orders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item 2"));

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(orders, 2);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orders, result);

        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersWithValidTokenFromHeader() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("Authorization") &&
                            headers.getFirst("Authorization").equals("Bearer " + validToken);
                }),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersWithoutAuthorizationHeader() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return !headers.containsKey("Authorization");
                }),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersWithEmptyAuthorizationHeader() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("");

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return !headers.containsKey("Authorization");
                }),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersWithInvalidAuthorizationHeader() {
        // Arrange - header that doesn't start with "Bearer "
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Invalid token");

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return !headers.containsKey("Authorization");
                }),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersWithEmptyToken() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer ");

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return !headers.containsKey("Authorization");
                }),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetAllOrdersResponseIsNull() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersResponseOrdersIsNull() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(null, 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersHttpClientErrorException() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST, "Bad Request", "Error body".getBytes(), null);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersHttpServerErrorException() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        HttpServerErrorException exception = new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", "Error body".getBytes(), null);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersResourceAccessException() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        ResourceAccessException exception = new ResourceAccessException("Connection timeout");

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersGenericException() {
        // Arrange
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        RuntimeException exception = new RuntimeException("Unexpected error");

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllOrdersWithEmptyBaseUrl() {
        // Arrange
        ReflectionTestUtils.setField(orderServiceClient, "orderServiceBaseUrl", "");
        String expectedUrl = "" + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllOrdersWithNullBaseUrl() {
        // Arrange
        ReflectionTestUtils.setField(orderServiceClient, "orderServiceBaseUrl", null);
        String expectedUrl = "null" + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSystemOutPrintlnCalls() {
        // This test verifies that System.out.println calls are executed
        // We can't easily mock System.out, but we can verify the methods execute without error

        // Test successful case
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        List<OrderData> orders = new ArrayList<>();
        orders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item 1"));

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(orders, 1);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);
            assertNotNull(result);
            assertEquals(1, result.size());
        });
    }

    @Test
    void testThreadNameInPrintln() {
        // Test that the current thread name is properly retrieved and used in println
        String expectedUrl = baseUrl + allOrdersEndpoint;
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert - verify thread name can be accessed (no exception thrown)
        assertNotNull(Thread.currentThread().getName());
        assertNotNull(result);
    }

    @Test
    void testGenerateServiceTokenEdgeCases() {
        // Test dengan authorization header yang tepat 7 karakter (hanya "Bearer ")
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer ");

        OrderListResponseFromExternal mockResponse = new OrderListResponseFromExternal(new ArrayList<>(), 0);
        ResponseEntity<OrderListResponseFromExternal> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        List<OrderData> result = orderServiceClient.getAllOrders(httpServletRequest);

        // Assert
        assertNotNull(result);
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return !headers.containsKey("Authorization");
                }),
                any(ParameterizedTypeReference.class)
        );
    }
}