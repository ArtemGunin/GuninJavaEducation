package com.model;

import com.model.product.Product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ProductVersioningLinkedList<E extends Product> implements Iterable<E> {
    private int size = 0;
    private final Set<Integer> versions;
    private ProductElement<E> lastVersion;
    private ProductElement<E> firstVersion;

    public ProductVersioningLinkedList() {
        versions = new HashSet<>();
    }

    public void addNewVersion(E e, int version) {
        if (versions.contains(version)) {
            throw new IllegalArgumentException("Version already contained");
        } else if (lastVersion != null && lastVersion.version > version) {
            throw new IllegalArgumentException("New version, should be the highest");
        }

        final ProductElement<E> firstElement = lastVersion;
        final ProductElement<E> newProductElement = new ProductElement<>(null, e, firstElement, version);
        lastVersion = newProductElement;
        if (firstElement == null) {
            firstVersion = newProductElement;
        } else {
            firstElement.prev = newProductElement;
        }
        size++;
        versions.add(newProductElement.version);
    }

    public ProductElement<E> findProductElementByVersion(int version) {
        if (versions.contains(version)) {
            for (ProductElement<E> element = lastVersion; element != null; element = element.next) {
                if (element.version == version) {
                    return element;
                }
            }
        }
        throw new IllegalArgumentException("This version is not available");
    }

    public E findProductByVersion(int version) {
        return findProductElementByVersion(version).product;
    }

    public void deleteProductByVersion(int version) {
        final ProductElement<E> productElement = findProductElementByVersion(version);
        final ProductElement<E> next = productElement.next;
        final ProductElement<E> prev = productElement.prev;

        if (prev == null) {
            lastVersion = next;
        } else {
            prev.next = next;
            productElement.prev = null;
        }

        if (next == null) {
            firstVersion = prev;
        } else {
            next.prev = prev;
            productElement.next = null;
        }

        productElement.product = null;
        versions.remove(version);
        size--;
    }

    public void updateProductByVersion(E product, int version) {
        findProductElementByVersion(version).product = product;
    }

    public int getVersionsCount() {
        return versions.size();
    }

    public LocalDateTime getFirstVersionDate() {
        return firstVersion.dateOfAdding;
    }

    public LocalDateTime getLastVersionDate() {
        return lastVersion.dateOfAdding;
    }

    public int size() {
        return size;
    }

    public static class ProductElement<E> {
        E product;
        int version;
        LocalDateTime dateOfAdding;
        ProductElement<E> next;
        ProductElement<E> prev;

        ProductElement(ProductElement<E> prev, E product, ProductElement<E> next, int version) {
            this.prev = prev;
            this.product = product;
            this.next = next;
            this.version = version;
            dateOfAdding = LocalDateTime.now();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ProductVersioningIterator();
    }

    private class ProductVersioningIterator implements Iterator<E> {
        private ProductElement<E> next;
        private int nextIndex;

        public ProductVersioningIterator() {
            next = (lastVersion == null) ? null : lastVersion;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ProductVersioningLinkedList.ProductElement<E> lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.product;
        }
    }
}
