package com.adeo.pro.replenishment.coding.challenge.stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class ProductSaleAnalysisTrainingCandidate {

	/**
	 * The main.
	 * @param args
	 */
	public static void main(String[] args) {
		
		WebClient webClient = new WebClient();

		/*
		 * I wish a stream of products sorted by quantity (desc for FR and IT BUs).
		 * Note : No aggregation by product name.
		 */
		System.out.println(" > Sorted by quantity (desc) for FR and IT BUs :");
		
//		webClient.getProductSale().
//			forEach(System.out::println);
		
		webClient.getProductSale()
        .filter(product -> (product.getBusinessUnit().equals(BusinessUnit.FR.name()) ||
                product.getBusinessUnit().equals(BusinessUnit.IT.name())))
                .sorted((product1, product2) -> product1.getQuantity().compareTo(product2.getQuantity()) * -1)
        .forEach(System.out::println);

		
		System.out.println(" > -------------- ");
		
		
		/*
		 * I would like a stream of products sorted by the total amount (desc).
		 * For this case, all BUs are necessary.
		 * Note : No aggregation by product name.
		 */
		System.out.println(" > Sorted by the total amount (desc) for all BUs :");
		
//		webClient.getProductSale().
//			map(ProductSaleEnrichment::new).
//			forEach(System.out::println);
		
		webClient.getProductSale().map(ProductSaleEnrichment::new).map(
                product -> {
                    product.setTotalAmount(product.getPricePerUnit() * product.getQuantity());
                    return product;
                })
                .sorted((product1, product2) -> product1.getTotalAmount().compareTo(product2.getTotalAmount()))
                .forEach(System.out::println);
		
		System.out.println(" > -------------- ");
		
		
		/*
		 * I would like a stream which contains the product with lowest sale.
		 * Note : No aggregation by product name.
		 */
		System.out.println(" > The product with lowest sale (lowest quantity) for all BUs :");
		
//		Optional<ProductSale> lowestSale = webClient.getProductSale().
//			findFirst();
        Optional<ProductSale> lowestSale = webClient.getProductSale()
                .min((product1, product2) -> product1.getQuantity().compareTo(product2.getQuantity()));
		
		System.out.println(" > The product sale with the lowest sale is ".concat(lowestSale.get().toString()));
		System.out.println(" > -------------- ");
		
		
		/*
		 * I would like a stream which contains the product with the highest total amount.
		 * Note : No aggregation by product name.
		 */
		System.out.println(" > The product with highest total amount for all BUs :");
		
//		Optional<ProductSaleEnrichment> highestTotalAmount = webClient.getProductSale().
//			map(ProductSaleEnrichment::new).
//			findFirst();
		
		Optional<ProductSaleEnrichment> highestTotalAmount = webClient.getProductSale().map(ProductSaleEnrichment::new).map(
                product -> {
                    product.setTotalAmount(product.getPricePerUnit() * product.getQuantity());
                    return product;
                })
                .max((product1, product2) -> product1.getTotalAmount().compareTo(product2.getTotalAmount()));
		
		
		System.out.println(" > The product sale with the highest total amount is ".concat(highestTotalAmount.get().toString()));
		System.out.println(" > -------------- ");
		
		
		/*
		 * I would like a stream which contains the product with the highest total amount.
		 * Note : An aggregation by product name is necessary.
		 */
		System.out.println(" > The product with highest total amount with an aggregation by product name :");
		
//		webClient.getProductSale();

// 		webClient.getProductSale().map(ProductSaleEnrichment::new).map(
//              product -> {
//                  product.setTotalAmount(product.getPricePerUnit() * product.getQuantity());
//                  return product;
//              })
//              .collect(groupingBy(ProductSaleEnrichment::getName, TreeMap::new, summingDouble(ProductSaleEnrichment::getTotalAmount)))
//              .entrySet().stream()
//              .sorted(Map.Entry.comparingByValue())
//              .forEach(product -> System.out.println(product.getKey() + "= " + product.getValue()));
//      
//      Mais on a pas le produit :-(

		webClient.getProductSale().map(ProductSaleEnrichment::new).map(
              product -> {
                  product.setTotalAmount(product.getPricePerUnit() * product.getQuantity());
                  return product;
              })
                .collect(groupingBy(ProductSaleEnrichment::getName, collectingAndThen(reducing(
                        (product1, product2) -> new ProductSaleEnrichment(product1.getId(), product1.getName(), product1.getQuantity() + product2.getQuantity(), null, null,
                                product1.getTotalAmount() + product2.getTotalAmount())),
                      Optional<ProductSaleEnrichment>::get)))
              .entrySet().stream()
                .sorted((product1, product2) -> product1.getValue().getTotalAmount().compareTo(product2.getValue().getTotalAmount()) * -1)
              .forEach(product -> System.out.println(product.getValue()));
		
		System.out.println(" > -------------- ");
	}

	/*
	 * Your several methods.
	 */
	
	/*
	 * The embedded library.
	 */
	
	/**
	 * The WebClient which allows to send an http request to the API REST.
	 */
	public static final class WebClient {
		
		public static List<ProductSale> inMemory;
		
		public Stream<ProductSale> getProductSale() {
			
			List<ProductSale> data = new ArrayList<>();
			
			Arrays.asList(BusinessUnit.values()).
				forEach(bu -> {
			
						Arrays.asList(ProductName.values()).
							forEach(p -> data.add(ProductSale.
											builder().
											id(NonceService.getId()).
											name(p.name()).
											pricePerUnit(NonceService.getPrice(1)).
											businessUnit(bu.name()).
											quantity(NonceService.getQuantity()).
											build())
							);
					});
		
			if(inMemory == null) inMemory = data;
			
			return inMemory.
					stream();
		}
	}
	
	/**
	 * Extend the {@link ProductSale} and I enrich this class to manage the total amount.
	 */
	@Getter @Setter @ToString(callSuper = true)
	public static final class ProductSaleEnrichment extends ProductSale {
		
		private Double totalAmount;
		
		public ProductSaleEnrichment(ProductSale productSale) {
			super(productSale.getId(), productSale.getName(), productSale.getQuantity(), productSale.getPricePerUnit(), productSale.getBusinessUnit());
		}

        public ProductSaleEnrichment(String id, String name, Integer quantity, Double pricePerUnit, String businessUnit, Double totalAmount) {
            super(id, name, quantity, pricePerUnit, businessUnit);
            this.totalAmount = totalAmount;
        }

	}
	
	public static enum BusinessUnit {
		
		FR,
		IT,
		ES,
		PT;
	}
	
	public static enum ProductName {
		
		Driller,
		Hammer,
		Screwdriver,
		MultiPlug,
		Chainsaw;
	}
}
