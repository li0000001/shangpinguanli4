package com.expirytracker.data

import androidx.lifecycle.LiveData

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product): Long {
        return productDao.insert(product)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)
    }
}
