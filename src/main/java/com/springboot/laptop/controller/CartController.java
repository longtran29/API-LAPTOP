package com.springboot.laptop.controller;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CartRequestDTO;
import com.springboot.laptop.service.CartService;
import com.springboot.laptop.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    private final CartService cartService;
    private final AppUserService userService;


    @Operation(summary = "Thêm vào giỏ",
            description = "Thên sản phẩm vào giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Thêm thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PostMapping("/add_to_cart")
    public Object addProductToCart(@RequestBody CartRequestDTO cartRequest) throws Exception {
        return ResponseEntity.ok().body(cartService.addToCart(cartRequest.getProductId(), cartRequest.getQuantity()));
    }


    @Operation(summary = "Giỏ hàng ",
            description = "Xem danh sách sản phẩm trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @GetMapping("/all_cart_items")
    public ResponseEntity<?> getAllCartItem() {
        return ResponseEntity.ok().body(cartService.getAllCartDetails());
    }

    @Operation(summary = "Thêm / giảm số lượng ",
            description = "Thêm hoặc giảm số lượng sản phẩm trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PutMapping("/update/{productId}/{type}")
    public ResponseEntity<?> updateQuantityItem(@PathVariable Long productId, @PathVariable String type) {
        return ResponseEntity.ok().body(cartService.updateQuantityItem(productId, type));

    }



    @Operation(summary = "Xoá item trong giỏ hàng ",
            description = "Xoá các items trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Xoá thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long productId) {
       return ResponseEntity.ok().body(cartService.removeCartItem(productId));
    }
}
