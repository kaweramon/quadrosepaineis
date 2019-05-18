package com.quadrosepaineisapi.product.services;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.model.ProductImage;
import com.quadrosepaineisapi.model.ProductImgUrl;
import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.product.ProductRepository;
import com.quadrosepaineisapi.util.Constants;
import com.quadrosepaineisapi.util.ErrorMessages;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;
import io.minio.MinioClient;
import io.minio.ServerSideEncryption;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.crypto.KeyGenerator;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;

	private final QuadrosePaineisServiceUtil serviceUtil;

	public static final String PATH_PRODUCT_GALLERY = "produtos";
	
	private String fileSeparator = System.getProperty("file.separator");
	
	private String[] imageTypes = {"mini", "small", "product", "large", "main", "gallery"};
	
	private Map<String, int[]> mapImgHorizontalSizes = new HashMap<>();
	private Map<String, int[]> mapImgVerticalSizes = new HashMap<>();
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");

	private final String BUCKET_NAME = "quadrosepaineis";
	

	public Product create(Product product) {
		product.setSequence(repository.getProductsLength() + 1);
		product.setRegisterDate(LocalDateTime.now());
		product.setIsActive(true);
		
		return repository.save(product);
	}

	public Product update(Long id, Product product) {
		Product productSaved = serviceUtil.getProductById(id);
		BeanUtils.copyProperties(product, productSaved, "id", "registerDate", "sequence", "listImgUrl");
		return repository.save(productSaved);
	}

	public void updateIsActiveProperty(Long id, Boolean isActive) {
		Product productSaved = serviceUtil.getProductById(id);
		productSaved.setIsActive(isActive);
		repository.save(productSaved);
	}

	public void updateSequence(List<Product> products) {
		//TODO: Verificar sequencias
		List<Product> productsBD = new ArrayList<>();
		for (Product product : products) {
			Product prodBD = serviceUtil.getProductById(product.getId());
			prodBD.setSequence(product.getSequence());
			productsBD.add(prodBD);
		}
		repository.save(productsBD);
	}

	public Product view(Long id) {
		Product product = serviceUtil.getProductById(id);
		
		File productGalleryPath = new File(PATH_PRODUCT_GALLERY + fileSeparator + id);
		if (productGalleryPath.exists()) {
			List<ProductImage> galleryPaths = new ArrayList<ProductImage>();
			
			for (int i = 1; i <= productGalleryPath.listFiles().length / 4; i++) {
				String path = Constants.ENVIRONMENT + "images/image-resource/" + id + "/" + i + "/";
				galleryPaths.add(new ProductImage(i, path + "mini", path + "small",
						path + "product", path + "large"));
			}
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

	@Override
	public void uploadToMinio(Long id, MultipartFile photo) {

		MinioClient minioClient = null;
		try {
			 minioClient = new MinioClient("https://play.min.io:9000",
							"Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
		} catch (InvalidPortException e) {
			e.printStackTrace();
		}

		try {
			if (!minioClient.bucketExists(BUCKET_NAME))
				minioClient.makeBucket(BUCKET_NAME);
		} catch (MinioException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (XmlPullParserException e) {
			log.error(e.getMessage(), e);
		}

		try {

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Content-Type", "application/octet-stream");

			ServerSideEncryption sse = getServerSideEncryption();

			minioClient.putObject(BUCKET_NAME, "prod" + id,
					photo.getInputStream(), photo.getSize(), headerMap, sse, "application/octet-stream");

		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			e.printStackTrace();
		} catch (InternalException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			e.printStackTrace();
		}
	}

	private ServerSideEncryption getServerSideEncryption() {
		ServerSideEncryption sse = null;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256);
			sse = ServerSideEncryption.withCustomerKey(keyGen.generateKey());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sse;
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
