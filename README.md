<p align="center">
	<a href="" rel="noopener">
	 <img width=190px height=160px src="adeo.jpg" alt="Project logo">
 </a>
</p>

<h3 align="center">Pro Replenishment coding challenge</h3>

<div align="center">

  [![Status](https://img.shields.io/badge/status-active-success.svg)]()

</div>

---

Good reading! 🌈

tags : `Stream`

## 📝 Table of Contents
- [Presentation](#presentation)
- [Coding challenge](#coding-challenge)
	- [Stream](#stream)

## Presentation

Welcome 😀 

This file presents several trainings on the Java language. Indeed, you guessed it, it is like a `coding challenge`. For the moment, one `coding challenge` is available on `Stream` With Java.

## Coding challenge

### Stream

I propose you a first coding challenge using the `Stream` with the Java language.

You are several components like the `WebClient`, `BusinessUnit`,  `ProductName` and `ProductSaleEnrichment`.

These components allows you to retrieve many data of `ProductSale`. The `WebClient` has a single method named `getProductSale` and returns `Stream<ProductSale>`. 
This component simulates an http request to REST API.

Now, your job is to add several features to filter, sort and aggregate the data.

- Sorted by product quantity for the FR and IT BUs
- Sorted by the total amount for all BUs
- Get the product with the lowest sale for all BUs
- Get the product with the highest total amount for all BUs
- Get the product with the highest total amount with an aggregation by the product name





