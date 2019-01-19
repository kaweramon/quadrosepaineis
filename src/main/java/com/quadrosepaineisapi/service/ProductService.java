package com.quadrosepaineisapi.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.model.ProductImage;
import com.quadrosepaineisapi.model.ProductImgUrl;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.util.Constants;
import com.quadrosepaineisapi.util.ErrorMessages;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;

@Service
public class ProductService {

	public static final String PATH_PRODUCT_GALLERY = "produtos";
	
	private String fileSeparator = System.getProperty("file.separator");
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private QuadrosePaineisServiceUtil serviceUtil;
	
	private String[] imageTypes = {"mini", "small", "product", "large", "main", "gallery"};
	
	private Map<String, int[]> mapImgHorizontalSizes = new HashMap<>();
	private Map<String, int[]> mapImgVerticalSizes = new HashMap<>();
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");
	
	@Transactional(readOnly = false)
	public Product create(Product product) {
		product.setSequence(repository.getProductsLength() + 1);
		product.setRegisterDate(LocalDateTime.now());
		product.setIsActive(true);
		
		return repository.save(product);
	}
	
	@Transactional(readOnly = false)
	public Product update(Long id, Product product) {
		Product productSaved = serviceUtil.getProductById(id);
		BeanUtils.copyProperties(product, productSaved, "id", "registerDate", "sequence", "listImgUrl");
		return repository.save(productSaved);
	}

	@Transactional(readOnly = false)
	public void updateIsActiveProperty(Long id, Boolean isActive) {
		Product productSaved = serviceUtil.getProductById(id);
		productSaved.setIsActive(isActive);
		repository.save(productSaved);
	}
	
	@Transactional(readOnly = false)
	public void updateSequence(List<Product> products) {
		//TODO: Verificar sequencias
		List<Product> productsBD = new ArrayList<Product>();
		for (Product product : products) {
			Product prodBD = serviceUtil.getProductById(product.getId());
			prodBD.setSequence(product.getSequence());
			productsBD.add(prodBD);
		}
		repository.save(productsBD);
	}
	
	
	@Transactional(readOnly = true)
	public Product view(Long id) {
		Product product = serviceUtil.getProductById(id);
		
		File productGalleryPath = new File(PATH_PRODUCT_GALLERY + fileSeparator + id);
		if (productGalleryPath.exists()) {
			List<ProductImage> galleryPaths = new ArrayList<ProductImage>();
			
			for (int i = 1; i <= productGalleryPath.listFiles().length / 4; i++) {
				String path = Constants.ENVIRONMENT + "images/image-resource/" + id + "/" + i + "/";
				galleryPaths.add(new ProductImage(i, path + "mini", path + "small", path + "product", path + "large"));
			}
			
//			product.setGalleryPaths(galleryPaths);
		}
		
		return product;
	}
	
	public void uploadImage(Long id, MultipartFile photo) {
		if (photo != null) {
			
			String dtHrFormatted = this.getStringDtHourNow();
			
			for (String imgSize : this.imageTypes) {
				File fileImg = verifyProductAndCreateFolder(id, "main", imgSize, true, dtHrFormatted);
				
				int[] sizes = null;
				try {
					BufferedImage bufferedImg = ImageIO.read(photo.getInputStream());
					if (this.getImgOrientation(photo.getInputStream()) == 0) {
						if (this.mapImgHorizontalSizes.size() == 0)
							this.initMapImgHorizontalSizes();
						sizes = this.mapImgHorizontalSizes.get(imgSize);
					} else {
						if (this.mapImgVerticalSizes.size() == 0)
							this.initMapImgVerticalSizes();
						sizes = this.mapImgVerticalSizes.get(imgSize);
					}
					switch (imgSize) {
						case "mini":
							bufferedImg = resize((bufferedImg), sizes[0], sizes[1]);
							break;
						case "small":
							bufferedImg = resize((bufferedImg), sizes[0], sizes[1]);
							break;
						case "product":
							bufferedImg = resize((bufferedImg), sizes[0], sizes[1]);
							break;
						case "large":
							bufferedImg = resize((bufferedImg), sizes[0], sizes[1]);
							break;
					}
					
					if (bufferedImg == null)
						throw new BadRequestException("Imagem inválida");
					ImageIO.write(bufferedImg, "jpg", fileImg);
				} catch (IOException e) {
					throw new BadRequestException("Ocorreu um error ao tentar salvar a foto: " + e.getMessage());
				}
			}
		} else 
			throw new BadRequestException("Foto é obrigatória");
	}
	
	public void uploadGallery(Long productId, List<MultipartFile> gallery) {
		// TODO: salvar imagem nas seguintes resoluções: mini: 48x48, small: 100x100, product: 240x240, large: 400x400
		if (gallery == null)
			throw new BadRequestException(ErrorMessages.GALLERY_NOT_NULL);
		
		if (gallery.size() > 5)
			throw new BadRequestException(ErrorMessages.GALLERY_MAX);
		
		int index = 1;
		int[] sizes = null;
		
		Product product = serviceUtil.getProductById(productId);

		List<ProductImgUrl> listProdImgUrl = new ArrayList<>();	
		
		String dtHrFormatted = this.getStringDtHourNow();
		
		for (MultipartFile image: gallery) {
			try {
				
				for (String type: imageTypes) {
					if (this.getImgOrientation(image.getInputStream()) == 0) {
						if (this.mapImgHorizontalSizes.size() == 0)
							this.initMapImgHorizontalSizes();
						sizes = this.mapImgHorizontalSizes.get(type);
					} else {
						if (this.mapImgVerticalSizes.size() == 0)
							this.initMapImgVerticalSizes();
						sizes = this.mapImgVerticalSizes.get(type);
					}
					File imageFile = verifyProductAndCreateFolder(productId, String.valueOf(index), type, false, dtHrFormatted);
					BufferedImage bufferedImg = resize(ImageIO.read(image.getInputStream()), sizes[0], sizes[1]);
					if (bufferedImg == null)
						throw new BadRequestException("Imagem inválida");
					ImageIO.write(bufferedImg, "jpg", imageFile);
					
				}
			} catch (IOException e) {
				throw new BadRequestException("Ocorreu um error ao tentar salvar a galeria: " + e.getMessage());
			}
			listProdImgUrl.add(new ProductImgUrl(productId + "/" + index + "_" + dtHrFormatted));
			index++;
		}
		
		product.setListImgUrl(listProdImgUrl);
		
		this.repository.save(product);
		
	}
	
	@Transactional(readOnly = false)
	public void updateGallery(Long id, List<MultipartFile> galleryToUpdate, List<Long> listProductImgDeleted) {
		
		if (galleryToUpdate.size() > 5) {
			throw new BadRequestException(ErrorMessages.GALLERY_MAX);
		}
		
		Product product = serviceUtil.getProductById(id);
		
		for (Long prodImgUrlId : listProductImgDeleted) {
			product.getListImgUrl().removeIf(prodImgUrl -> prodImgUrl.getId() == prodImgUrlId);
		}
		
		repository.save(product);
		
		uploadGallery(id, galleryToUpdate);
	}
	
	private File verifyProductAndCreateFolder(Long productId, String imgName, String imageType, boolean isMainPhoto, String dtHrFormatted) {
		serviceUtil.getProductById(productId);
		File fileFolderProduct = new File(PATH_PRODUCT_GALLERY + fileSeparator + productId);
		if (!fileFolderProduct.exists())
			fileFolderProduct.mkdirs();
		
		String photoPath = PATH_PRODUCT_GALLERY + fileSeparator + productId + fileSeparator + 
				imgName;
		
		if (!isMainPhoto)
			photoPath +=  "_" + dtHrFormatted;
		
		photoPath += "_" + imageType + ".jpg";
		
		return new File(photoPath);
	}

	private BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
	
	private void initMapImgHorizontalSizes() {
		int[] sizes = null;
		for (String size: this.imageTypes) {
			switch (size) {
				case "mini":
					sizes = new int[]{80,39};
					break;
				case "small":
					sizes = new int[]{300,144};
					break;
				case "large":
					sizes = new int[]{900,599};
					break;
				case "product":
					sizes = new int[]{400,193};
					break;
				case "gallery":
					sizes = new int[]{600,400};
					break;
				default:
					sizes = new int[]{600,289};
					break;
			}
			this.mapImgHorizontalSizes.put(size, sizes);
		}
	}
	
	private void initMapImgVerticalSizes() {
		int[] sizes = null;
		for (String size: this.imageTypes) {
			switch (size) {
				case "mini":
					sizes = new int[]{60,71};
					break;
				case "small":
					sizes = new int[]{200,233};
					break;
				case "product":
					sizes = new int[]{400,467};
					break;
				case "large":
					sizes = new int[]{600,700};
					break;
				case "gallery":
					sizes = new int[]{380, 400};
					break;
				default:
					break;
			}
			this.mapImgVerticalSizes.put(size, sizes);
		}
	}
	
	private int getImgOrientation(InputStream is) {
		try {
			BufferedImage bufferedImg = ImageIO.read(is);
			if (bufferedImg.getWidth() > bufferedImg.getHeight())
				return 0;
			else
				return 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private String getStringDtHourNow() {
		LocalDateTime now = LocalDateTime.now();
		
		String dtString = now.format(dateFormatter);
		
		dtString = dtString.replaceAll("/", "_").replaceAll(":", "_").replace(" ", "_");
		
		return dtString;
	}
}
