package com.verification;

import com.model.product.Phone;
import com.service.PhoneService;
import com.service.SimpleBinaryTree;


public class BinaryTreeDisplayed {

    public static void displayed() {
        SimpleBinaryTree<Phone> phoneSimpleBinaryTree = new SimpleBinaryTree<>();
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(30);
        for (Phone phone : phoneService.getAll()) {
            phoneSimpleBinaryTree.add(phone);
        }
        phoneSimpleBinaryTree.printBinaryTree(System.out);

        System.out.println("***".repeat(10));
        System.out.println("Summary coast of left branch: " + phoneSimpleBinaryTree.summaryCoastLeftBranch());
        System.out.println("***".repeat(10));
        System.out.println("Summary coast of right branch: " + phoneSimpleBinaryTree.summaryCoastRightBranch());
        System.out.println("***".repeat(10));
    }
}
