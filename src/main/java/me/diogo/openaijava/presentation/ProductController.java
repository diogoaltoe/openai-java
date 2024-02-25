package me.diogo.openaijava.presentation;

import me.diogo.openaijava.operation.ProductUseCase;
import me.diogo.openaijava.presentation.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductUseCase productUseCase;

    @GetMapping("/{product}/category")
    @ResponseBody
    public ResponseEntity<ProductResponse> getCategory(@PathVariable("product") String product) {
        final String category = productUseCase.findCategory(product);

        return ResponseEntity.ok(new ProductResponse(product, category));
    }
}
