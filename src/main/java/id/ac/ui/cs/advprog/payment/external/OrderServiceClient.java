package id.ac.ui.cs.advprog.payment.external;

import id.ac.ui.cs.advprog.payment.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceClient {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${order.service.baseurl:}")
    private String orderServiceBaseUrl;

    @Value("${order.service.endpoint.all-orders}")
    private String allOrdersEndpoint;

    // Modifikasi constructor untuk menerima JwtTokenProvider
    public OrderServiceClient(RestTemplate restTemplate, JwtTokenProvider jwtTokenProvider) {
        this.restTemplate = restTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String generateServiceToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return token;
        }
        return null;
    }

    public List<OrderData> getAllOrders(HttpServletRequest request) {
        System.out.println("Memanggil Order Service untuk mengambil semua order (Thread: " +
                Thread.currentThread().getName() + ")");

        String finalAllOrdersUrl = orderServiceBaseUrl + allOrdersEndpoint;
        String generatedToken = generateServiceToken(request);

        HttpHeaders headers = new HttpHeaders();
        if (generatedToken != null && !generatedToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + generatedToken);
        }

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            ResponseEntity<OrderListResponseFromExternal> responseEntity = restTemplate.exchange(
                    finalAllOrdersUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<OrderListResponseFromExternal>() {}
            );

            OrderListResponseFromExternal listResponse = responseEntity.getBody();

            if (listResponse != null && listResponse.getOrders() != null) {
                System.out.println("Berhasil mengambil semua order. Jumlah: " + listResponse.getCount());
                return listResponse.getOrders();
            } else {
                System.out.println("Mendapat respons valid, tapi tidak ada list order di dalamnya atau format tidak sesuai.");
                return Collections.emptyList();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP saat mengambil semua order (" + finalAllOrdersUrl + "): " +
                    e.getStatusCode() + " - Pesan: " + e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            System.err.println("Error koneksi saat mengambil semua order (" + finalAllOrdersUrl + "): " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Kesalahan tidak terduga saat mengambil semua order (" + finalAllOrdersUrl + "): " + e.getMessage());
            return Collections.emptyList();
        }
    }
}