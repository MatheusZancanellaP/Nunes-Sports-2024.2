package com.nunessports.nunes_products.controller;

import com.nunessports.nunes_products.model.Product;
import com.nunessports.nunes_products.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
        model.addAttribute("product", product);
        return "view";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "form";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute Product product, BindingResult result, Model model) {

    if (result.hasErrors()) {
        if (result.hasFieldErrors("price")) {
            String priceError = "Invalid price. It must be a positive value greater than zero.";
            logger.error(priceError);
            model.addAttribute("errorMessage", priceError);
        } else if (result.hasFieldErrors("code")) {
            String codeError = "Invalid or already existing code.";
            logger.error(codeError);
            model.addAttribute("errorMessage", codeError);
        } else {
            logger.error("Error processing the form: {}", result.getAllErrors());
            model.addAttribute("errorMessage", "There was an error processing the form. Please check the fields and try again.");
        }
        return "form";
    }

    try {
        productRepository.save(product);
    } catch (Exception e) {
        if (e.getMessage().contains("duplicate key value violates unique constraint")) {
            String codeError = "Error: Product code already exists.";
            logger.error(codeError);
            model.addAttribute("errorMessage", codeError);
        } else {
            logger.error("Error saving the product: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error saving the product: " + e.getMessage());
        }
        return "form";
    }

    return "redirect:/products";
}


    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult result, Model model) {

    if (result.hasErrors()) {
        if (result.hasFieldErrors("price")) {
            String priceError = "Invalid price. It must be a positive number greater than zero.";
            logger.error(priceError);
            model.addAttribute("errorMessage", priceError);
        } else if (result.hasFieldErrors("code")) {
            String codeError = "Invalid or duplicate code.";
            logger.error(codeError);
            model.addAttribute("errorMessage", codeError);
        } else {
            logger.error("Form processing error: {}", result.getAllErrors());
            model.addAttribute("errorMessage", "An error occurred while processing the form. Please check the fields and try again.");
        }
        return "form";
    }

    try {
        productRepository.save(product);
    } catch (Exception e) {
        if (e.getMessage().contains("duplicate key value violates unique constraint")) {
            String codeError = "Error: Product code already exists.";
            logger.error(codeError);
            model.addAttribute("errorMessage", codeError);
        } else {
            logger.error("Error saving the product: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error saving the product: " + e.getMessage());
        }
        return "form";
    }

    return "redirect:/products";
}


    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
        model.addAttribute("product", product);
        return "form";
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
        productRepository.delete(product);
        return "redirect:/products";
    }

    @GetMapping
    public String listAllProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "list";
    }
}
