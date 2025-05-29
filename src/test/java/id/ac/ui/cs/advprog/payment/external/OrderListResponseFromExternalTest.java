package id.ac.ui.cs.advprog.payment.external;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

class OrderListResponseFromExternalTest {

    private OrderListResponseFromExternal response;
    private List<OrderData> testOrders;

    @BeforeEach
    void setUp() {
        testOrders = new ArrayList<>();
        testOrders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item 1"));
        testOrders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item 2"));
    }

    @Test
    void testNoArgsConstructor() {
        response = new OrderListResponseFromExternal();

        assertNotNull(response);
        assertNull(response.getOrders());
        assertEquals(0, response.getCount());
    }

    @Test
    void testAllArgsConstructor() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertNotNull(response);
        assertEquals(testOrders, response.getOrders());
        assertEquals(2, response.getCount());
    }

    @Test
    void testSettersAndGetters() {
        response = new OrderListResponseFromExternal();
        response.setOrders(testOrders);
        response.setCount(5);
        assertEquals(testOrders, response.getOrders());
        assertEquals(5, response.getCount());
    }

    @Test
    void testSetOrdersNull() {
        response = new OrderListResponseFromExternal(testOrders, 2);
        response.setOrders(null);

        assertNull(response.getOrders());
        assertEquals(2, response.getCount());
    }

    @Test
    void testSetEmptyOrders() {
        response = new OrderListResponseFromExternal();
        List<OrderData> emptyList = new ArrayList<>();

        response.setOrders(emptyList);
        response.setCount(0);

        assertEquals(emptyList, response.getOrders());
        assertTrue(response.getOrders().isEmpty());
        assertEquals(0, response.getCount());
    }

    @Test
    void testSetNegativeCount() {
        response = new OrderListResponseFromExternal();

        response.setCount(-1);
        assertEquals(-1, response.getCount());
    }

    @Test
    void testSetMaxIntCount() {
        response = new OrderListResponseFromExternal();

        response.setCount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, response.getCount());
    }

    @Test
    void testSetMinIntCount() {
        response = new OrderListResponseFromExternal();

        response.setCount(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, response.getCount());
    }

    @Test
    void testEquals() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(testOrders, 2);

        assertEquals(response1, response2);
        assertTrue(response1.equals(response2));
    }

    @Test
    void testEqualsWithSameReference() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertTrue(response.equals(response));
    }

    @Test
    void testEqualsWithNull() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertFalse(response.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertFalse(response.equals("different class"));
    }

    @Test
    void testEqualsWithDifferentOrders() {
        List<OrderData> differentOrders = new ArrayList<>();
        differentOrders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Different Item"));

        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(differentOrders, 2);

        assertNotEquals(response1, response2);
    }

    @Test
    void testEqualsWithDifferentCount() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(testOrders, 3);

        assertNotEquals(response1, response2);
    }

    @Test
    void testEqualsWithBothOrdersNull() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(null, 0);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(null, 0);

        assertEquals(response1, response2);
    }

    @Test
    void testEqualsWithOneOrdersNull() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(null, 2);

        assertNotEquals(response1, response2);
        assertNotEquals(response2, response1);
    }

    @Test
    void testHashCode() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(testOrders, 2);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        int firstHash = response.hashCode();
        int secondHash = response.hashCode();

        assertEquals(firstHash, secondHash);
    }

    @Test
    void testHashCodeWithNullOrders() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(null, 0);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(null, 0);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testHashCodeDifferentObjects() {
        List<OrderData> differentOrders = new ArrayList<>();
        differentOrders.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Different"));

        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(differentOrders, 2);

        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        response = new OrderListResponseFromExternal(testOrders, 2);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("OrderListResponseFromExternal"));
        assertTrue(toString.contains("orders="));
        assertTrue(toString.contains("count=2"));
    }

    @Test
    void testToStringWithNullOrders() {
        response = new OrderListResponseFromExternal(null, 0);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("OrderListResponseFromExternal"));
        assertTrue(toString.contains("orders=null"));
        assertTrue(toString.contains("count=0"));
    }

    @Test
    void testToStringWithEmptyOrders() {
        List<OrderData> emptyList = new ArrayList<>();
        response = new OrderListResponseFromExternal(emptyList, 0);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("OrderListResponseFromExternal"));
        assertTrue(toString.contains("orders=[]"));
        assertTrue(toString.contains("count=0"));
    }

    @Test
    void testToStringWithNegativeCount() {
        response = new OrderListResponseFromExternal(testOrders, -1);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("count=-1"));
    }

    @Test
    void testCanEqual() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal();

        assertTrue(response1.canEqual(response2));
        assertTrue(response2.canEqual(response1));
    }

    @Test
    void testCanEqualWithDifferentType() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertFalse(response.canEqual("string"));
        assertFalse(response.canEqual(null));
        assertFalse(response.canEqual(123));
    }

    @Test
    void testCanEqualWithSameInstance() {
        response = new OrderListResponseFromExternal(testOrders, 2);

        assertTrue(response.canEqual(response));
    }

    @Test
    void testOrderListModification() {
        List<OrderData> modifiableList = new ArrayList<>(testOrders);
        response = new OrderListResponseFromExternal(modifiableList, 2);

        assertEquals(2, response.getOrders().size());

        OrderData newOrder = new OrderData(UUID.randomUUID(), UUID.randomUUID(), "New Item");
        response.getOrders().add(newOrder);

        assertEquals(3, response.getOrders().size());
        assertTrue(response.getOrders().contains(newOrder));
    }

    @Test
    void testOrderListClear() {
        response = new OrderListResponseFromExternal(new ArrayList<>(testOrders), 2);

        response.getOrders().clear();

        assertTrue(response.getOrders().isEmpty());
        assertEquals(0, response.getOrders().size());
        assertEquals(2, response.getCount());
    }

    @Test
    void testEqualsSymmetry() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(testOrders, 2);

        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response1));
    }

    @Test
    void testEqualsTransitivity() {
        OrderListResponseFromExternal response1 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response2 = new OrderListResponseFromExternal(testOrders, 2);
        OrderListResponseFromExternal response3 = new OrderListResponseFromExternal(testOrders, 2);

        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response3));
        assertTrue(response1.equals(response3));
    }

    @Test
    void testCountZero() {
        response = new OrderListResponseFromExternal(testOrders, 0);

        assertEquals(0, response.getCount());
        assertEquals(testOrders, response.getOrders());
    }

    @Test
    void testLargeOrdersList() {
        List<OrderData> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(new OrderData(UUID.randomUUID(), UUID.randomUUID(), "Item " + i));
        }

        response = new OrderListResponseFromExternal(largeList, 1000);

        assertEquals(1000, response.getCount());
        assertEquals(1000, response.getOrders().size());
        assertEquals(largeList, response.getOrders());
    }
}