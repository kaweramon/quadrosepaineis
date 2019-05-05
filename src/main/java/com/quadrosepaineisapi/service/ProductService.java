package com.quadrosepaineisapi.service;

import com.quadrosepaineisapi.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    Product update(Long id, Product product);

    void updateIsActiveProperty(Long id, Boolean isActive);

    void updateSequence(List<Product> products);

    Product view(Long id);

    void uploadImage(Long id, MultipartFile photo);

    void uploadGallery(Long productId, List<MultipartFile> gallery);

    void updateGallery(Long id, List<MultipartFile> galleryToUpdate, List<Long> listProductImgDeleted);

}
