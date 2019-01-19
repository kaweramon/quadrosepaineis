package com.quadrosepaineisapi.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.util.UrlConstants;

@RequestMapping(path = UrlConstants.URL_IMAGES)
@RestController
public class ImageResource {

	@GetMapping(path = "/image-resource/{productId}/{imageName}/{size}")
	public void getImageByPath(@PathVariable Long productId, @PathVariable String imageName, @PathVariable String size, HttpServletResponse response) {
		
		try {
			String fileSeparator = System.getProperty("file.separator");
			System.out.println("Separador de arquivo: " + fileSeparator);
			File file = new File("produtos" + fileSeparator  + productId + fileSeparator + imageName + "_" + size + ".jpg");
			InputStream in = new FileInputStream(file);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			IOUtils.copy(in, response.getOutputStream());
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException("Imagem não encontrada: " + e.getMessage());
		} catch (IOException e) {
			throw new ResourceNotFoundException("Imagem não encontrada: " + e.getMessage());
		}
		
	}
}
