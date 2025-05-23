package id.ac.ui.cs.advprog.payment.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    @Value("${order.service.baseurl}")
    private String orderServiceBaseUrl;

    @Value("${order.service.endpoint.all-orders}")
    private String allOrdersEndpoint;

    public OrderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OrderData> getAllOrders() {
        System.out.println("Memanggil Order Service untuk mengambil semua order (Thread: " +
                Thread.currentThread().getName() + ")");

        String finalAllOrdersUrl = orderServiceBaseUrl + allOrdersEndpoint;

        try {
            ResponseEntity<OrderListResponseFromExternal> responseEntity = restTemplate.exchange(
                    finalAllOrdersUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<OrderListResponseFromExternal>() {}
            );

            OrderListResponseFromExternal listResponse = responseEntity.getBody();

            if (listResponse != null && listResponse.getOrders() != null) {
                System.out.println("Berhasil mengambil semua order. Jumlah: " + listResponse.getCount());
                return listResponse.getOrders();
            } else {
                System.out.println("Mendapat respons, tapi tidak ada list order di dalamnya.");
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