package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.DashboardStatsDTO;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        // Basic statistics using existing repository methods
        long totalOrders = orderRepository.count();
        long totalProducts = productRepository.count();
        long totalCustomers = userRepository.count();

        // TODO: Implement more detailed statistics when repository methods are added
        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalRevenue(BigDecimal.ZERO)
                .totalOrders(totalOrders)
                .pendingOrders(0L)
                .completedOrders(0L)
                .totalProducts(totalProducts)
                .totalCustomers(totalCustomers)
                .activeProducts(totalProducts)
                .outOfStockProducts(0L)
                .build();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
