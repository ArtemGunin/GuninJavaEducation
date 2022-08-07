package solid.com.example.service;

import solid.com.example.model.NotifiableProduct;
import solid.com.example.model.ProductType;
import solid.com.example.repository.ProductRepositoryImpl;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NotifiableProductService extends ProductService {

    protected NotifiableProductService(ProductRepositoryImpl repository) {
        super(repository);
    }

    public NotifiableProduct createRandomProduct() {
        Random random = new Random();
        NotifiableProduct notifiableProduct = new NotifiableProduct();
        notifiableProduct.setType(ProductType.NOTIFIABLE_PRODUCT);
        notifiableProduct.setId(random.nextLong());
        notifiableProduct.setTitle(random.nextFloat() + "" + random.nextDouble());
        notifiableProduct.setAvailable(random.nextBoolean());
        notifiableProduct.setChannel(random.nextBoolean() + "" + random.nextDouble());
        notifiableProduct.setPrice(random.nextDouble());
        return notifiableProduct;
    }

    public int filterNotifiableProductsAndSendNotifications() {
        int notifications = 0;
        List<NotifiableProduct> products = repository.getAll().stream()
                .filter(it -> it.getType().equals(ProductType.NOTIFIABLE_PRODUCT))
                .map(it -> (NotifiableProduct) it)
                .collect(Collectors.toList());
        for (NotifiableProduct product : products) {
            //sending some notifications here
            notifications++;
        }
        return notifications;
    }
}
