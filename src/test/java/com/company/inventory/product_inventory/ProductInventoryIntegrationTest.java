package com.company.inventory.product_inventory;

import com.company.inventory.product_inventory.data.ProductData;
import com.company.inventory.product_inventory.data.WarehouseData;
import com.company.inventory.product_inventory.model.Product;
import com.company.inventory.product_inventory.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProductInventoryIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        productRepository.deleteAll();
    }
    @Test
    void testProductIsCreated() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productBrazilianChocolate)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatedProductButProductSkuAlreadyExist() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productBrazilianChocolate)))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateProductOK() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);
        productBrazilianChocolate.setName("France chocolate");

        Product productBefore = productRepository.findProductBySku(productBrazilianChocolate.getSku());
        assertEquals("Brazilian chocolate", productBefore.getName());

        mockMvc.perform(put("/products/sku/{sku}", productBrazilianChocolate.getSku())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productBrazilianChocolate)))
                .andExpect(status().isOk());

        Product productAfter = productRepository.findProductBySku(productBrazilianChocolate.getSku());
        assertEquals("France chocolate", productAfter.getName());
    }

    @Test
    void testUpdateProductButProductNotFound() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();

        mockMvc.perform(put("/products/sku/{sku}", productBrazilianChocolate.getSku())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productBrazilianChocolate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProductButIsNotPossibleUpdateSku() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);
        productBrazilianChocolate.setSku(4);

        mockMvc.perform(put("/products/sku/{sku}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productBrazilianChocolate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddWarehouseOK() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        assertEquals(2, productRepository.findProductBySku(1).getInventory().getWarehouses().size());

        mockMvc.perform(patch("/products/sku/{sku}/add-warehouse", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceMG())))
                .andExpect(status().isOk());

        assertEquals(3, productRepository.findProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void testAddWarehouseButProductNotFound() throws Exception {
        mockMvc.perform(patch("/products/sku/{sku}/add-warehouse", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceMG())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddWarehouseButWarehouseAlreadyExist() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(patch("/products/sku/{sku}/add-warehouse", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateWarehouseQuantityIncrementOK() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        assertEquals(12, productRepository.findProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());

        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "10")
                        .param("operation", "INCREMENT")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isOk());

        assertEquals(22, productRepository.findProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
    }

    @Test
    void testUpdateWarehouseQuantityDecrementOK() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        assertEquals(12, productRepository.findProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());

        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "10")
                        .param("operation", "DECREMENT")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isOk());

        assertEquals(2, productRepository.findProductBySku(1).getInventory().getWarehouses().getFirst().getQuantity());
    }

    @Test
    void testUpdateWarehouseQuantityDecrementFailed() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "20")
                        .param("operation", "DECREMENT")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWarehouseQuantityIncrementButProductNotFound() throws Exception {
        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "20")
                        .param("operation", "INCREMENT")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWarehouseQuantityIncrementButWarehouseNotFound() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "RJ")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "1")
                        .param("operation", "DECREMENT")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWarehouseQuantityButOperationNotSuported() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(patch("/products/sku/{sku}/update-warehouse-quantity", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .param("quantityChange", "10")
                        .param("operation", "erro")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(WarehouseData.getWarehousesEcommerceSPBrazilianChocolate())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteWarehouseOK() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        assertEquals(2, productRepository.findProductBySku(1).getInventory().getWarehouses().size());

        mockMvc.perform(delete("/products/sku/{sku}/delete-warehouse", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        assertEquals(1, productRepository.findProductBySku(1).getInventory().getWarehouses().size());
    }

    @Test
    void testDeleteWarehouseButWarehouseNotFound() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);

        mockMvc.perform(delete("/products/sku/{sku}/delete-warehouse", 1)
                        .param("locality", "MG")
                        .param("type", "ECOMMERCE")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteWarehouseButProductNotFound() throws Exception {
        mockMvc.perform(delete("/products/sku/{sku}/delete-warehouse", 1)
                        .param("locality", "SP")
                        .param("type", "ECOMMERCE")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAllProducts() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        assertEquals(3, productRepository.findAll().size());

        mockMvc.perform(delete("/products")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        assertEquals(0, productRepository.findAll().size());
    }

    @Test
    void testDeleteProductBySkuOk() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        assertEquals(3, productRepository.findAll().size());
        assertNotNull(productRepository.findProductBySku(1));

        mockMvc.perform(delete("/products/sku/{sku}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        assertEquals(2, productRepository.findAll().size());
        assertNull(productRepository.findProductBySku(1));
    }

    @Test
    void testDeleteProductBySkuButProductNotFound() throws Exception {
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(delete("/products/sku/{sku}", 1)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product productBrazilianChocolate = ProductData.getProductBrazilianChocolate();
        productRepository.save(productBrazilianChocolate);
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(get("/products")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetProductsByTypeOk() throws Exception {
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(get("/products/warehouse-by-type").param("type", "PHYSICAL_STORE")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("American chocolate"));
    }

    @Test
    void testGetProductsByTypeButProductNotFound() throws Exception {
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(get("/products/warehouse-by-type").param("type", "PHYSICAL_STORE")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductsByLocalityOk() throws Exception {
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(get("/products/warehouse-by-locality").param("locality", "RJ")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("American chocolate"));
    }

    @Test
    void testGetProductsByLocalityButProductNotFound() throws Exception {
        Product productAmericanChocolate = ProductData.getProductAmericanChocolate();
        productRepository.save(productAmericanChocolate);
        Product productFranceChocolate = ProductData.getProductFranceChocolate();
        productRepository.save(productFranceChocolate);

        mockMvc.perform(get("/products/warehouse-by-locality").param("locality", "MG")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}
