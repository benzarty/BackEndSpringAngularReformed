package tn.esprit.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
	
import tn.esprit.spring.domaine.Response;
import tn.esprit.spring.entity.Client;
import tn.esprit.spring.entity.Produit;
import tn.esprit.spring.entity.Rayon;
import tn.esprit.spring.entity.Stock;
import tn.esprit.spring.repository.ProduitRepository;
import tn.esprit.spring.repository.RayonRepository;
import tn.esprit.spring.repository.StockRepository;
import tn.esprit.spring.service.APIResponse;
import tn.esprit.spring.service.ProduitServiceImpl;
import tn.esprit.spring.service.RayonServiceImpl;



@RestController
@RequestMapping("/Produit")
public class ProduitController {

	@Autowired
	ProduitServiceImpl serviceproduit;

	@Autowired
	ProduitRepository repoproduit;
	@Autowired  ServletContext context;
	@Autowired
	StockRepository repostock;

	@Autowired
	RayonRepository reporepo;
	@Autowired
	RayonServiceImpl rayonservice;


	// http://localhost:8089/SpringMVC/client/retrieve-all-clients
	@GetMapping("/retrieve-all-produits")
	@ResponseBody
	public List<Produit> getProduits() {
	List<Produit> listproduits = serviceproduit.retrieveAllProduits();
	return listproduits; 
	}
	
	// http://localhost:8089/SpringMVC/client/retrieve-client/8
			@GetMapping("/retrieve-produit/{produit-id}")
			@ResponseBody
			public Produit retrieveProduit(@PathVariable("produit-id") Long produitId) {
			return serviceproduit.retrieveProduit(produitId);
			}
	
	
		
			
			
	// http://localhost:8089/SpringMVC/Produit/modify-produit
	@PutMapping("/modify-produit")
	@ResponseBody
	public Produit modifyProduit(@RequestBody Produit produit) {
		return serviceproduit.updateproduit(produit);
	}

	


	// http://localhost:8089/SpringMVC/client/remove-client/{client-id}
			@DeleteMapping("/remove-produit/{produit-id}")
		     @ResponseBody
			public void removeProduit(@PathVariable("produit-id") Long produitId) {
				serviceproduit.deleteproduit(produitId);
			}
			
			
			 @GetMapping("/pagination/{offset}/{pageSize}")
			    private APIResponse<Page<Produit>> getProductsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
			        Page<Produit> productsWithPagination = serviceproduit.findProductsWithPagination(offset, pageSize);
			        return new APIResponse<>(productsWithPagination.getSize(), productsWithPagination);
			    }
			 
				// http://localhost:8089/SpringMVC/Produit/assignProduitToStockgogo
				@PutMapping("/assignProduitToStockgo/{idProduit}/{stockproduit}")
				@ResponseBody
				public void assignProduitToStockgo(@PathVariable("idProduit") Long produitId,@PathVariable("stockproduit") Long stockproduit)
				{
					 serviceproduit.assignProduitToStockgo(produitId, stockproduit);
					
				}
				

				@GetMapping("/trier/{field}")
			    private APIResponse<List<Produit>> getProductsWithSort(@PathVariable String field) {
			        List<Produit> allProducts = serviceproduit.findProductsWithSorting(field);
			        return new APIResponse<>(allProducts.size(), allProducts);
			    }
				@PutMapping("/affecterProduitToimage/{idProduit}/{stringfile}")
				@ResponseBody
				public void affecterProduitToFournisseur(@PathVariable("idProduit") Long idProduit,@PathVariable("stringfile") String file)
				{
					serviceproduit.afecterProduitimage(idProduit, file);
					
				} 
			
				

				


	
	@PostMapping("/create-produit/{idRayon}/{idStock}")
	@ResponseBody
	public Produit createProduit(@RequestBody Produit p1,@PathVariable Long idRayon,@PathVariable Long idStock) {
		
			Produit p=serviceproduit.addProduit(p1, idRayon, idStock);
			return p;
		

	}
	
	
	
	




	
	
	
	

	
	
	
	

	 
	 @PostMapping("/file/{idStock}/{idRayon}")
	 public ResponseEntity<Response> createArticle (@RequestParam("file") MultipartFile file,
			 @RequestParam("produit") String produit,@PathVariable Long idStock,@PathVariable Long idRayon) throws JsonParseException , JsonMappingException , Exception
	 {
		 

		 
		 Rayon rayon=reporepo.getById(idStock);
		Stock stock=repostock.getById(idRayon);

	
		
		
		
		
		 
		 System.out.println("Ok .............");
        Produit arti = new ObjectMapper().readValue(produit, Produit.class);
        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit)
        {
        	new File (context.getRealPath("/Images/")).mkdir();
        	System.out.println("mk dir.............");
        }
        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
        File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
        try
        {
        	System.out.println("Image");
        	 FileUtils.writeByteArrayToFile(serverFile,file.getBytes());
        	 
        }catch(Exception e) {
        	e.printStackTrace();
        }

       
        arti.setFileName(newFileName);
        arti.setLibelle(arti.getLibelle());
        
        arti.setRayonproduit(rayon);
        arti.setStockproduit(stock);
        
    	


        Produit art = repoproduit.save(arti);
        
        
        if (art != null)
        {
        	return new ResponseEntity<Response>(new Response (""),HttpStatus.OK);
        }
        else
        {
        	return new ResponseEntity<Response>(new Response ("Article not saved"),HttpStatus.BAD_REQUEST);	
        }
	 }
	 
	 @GetMapping(path="/Imgarticles/{idProduit}")
	 public byte[] getPhoto(@PathVariable("idProduit") Long idProduit) throws Exception{
		 Produit Article   = repoproduit.findById(idProduit).get();
		 return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Article.getFileName()));
	 }
	
	
	 
	 
	 
	 
	 
	
	

}
