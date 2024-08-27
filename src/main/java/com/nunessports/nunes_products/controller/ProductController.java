package com.nunessports.nunes_products.controller;

import com.nunessports.nunes_products.model.Product;
import com.nunessports.nunes_products.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

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
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult result, Model model) {
        productRepository.save(product);
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
