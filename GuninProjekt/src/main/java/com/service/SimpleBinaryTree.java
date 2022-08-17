package com.service;

import com.model.ProductComparator;
import com.model.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintStream;

@Setter
@Getter
public class SimpleBinaryTree<E extends Product> {

    private Node<E> root;
    private final ProductComparator<E> productComparator = new ProductComparator<>();

    private Node<E> addRecursive(Node<E> current, E value) {
        if (current == null) {
            return new Node<>(value);
        }

        if (productComparator.compare(current.item, value) < 0) {
            current.left = addRecursive(current.left, value);
        } else if (productComparator.compare(current.item, value) > 0) {
            current.right = addRecursive(current.right, value);
        } else {
            return current;
        }
        return current;
    }

    public void add(E item) {
        root = addRecursive(root, item);
    }

    private String traversePreOrder(Node<E> root) {
        if (root == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root.toString());

        String pointerRight = "└── ";
        String pointerLeft = "├── ";

        traverseNodes(
                stringBuilder,
                "",
                pointerLeft,
                root.left,
                root.right != null
        );
        traverseNodes(
                stringBuilder,
                "",
                pointerRight,
                root.right,
                false
        );

        return stringBuilder.append("\n").toString();
    }

    private void traverseNodes(StringBuilder stringBuilder,
                               String padding,
                               String pointer,
                               Node<E> node,
                               boolean hasRightSibling) {

        if (node != null) {
            stringBuilder.append("\n");
            stringBuilder.append(padding);
            stringBuilder.append(pointer);
            stringBuilder.append(node);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└── ";
            String pointerLeft = "├── ";

            traverseNodes(
                    stringBuilder,
                    paddingForBoth,
                    pointerLeft,
                    node.getLeft(),
                    node.getRight() != null
            );

            traverseNodes(
                    stringBuilder,
                    paddingForBoth,
                    pointerRight,
                    node.getRight(),
                    false
            );
        }
    }

    public void printBinaryTree(PrintStream os) {
        os.print(traversePreOrder(root));
    }

    private double summaryCost(Node<E> node) {
        if (node == null) {
            return 0.0;
        }
        return ((node.item.getPrice() * node.item.getCount())
                + summaryCost(node.left)
                + summaryCost(node.right));
    }

    public double summaryCoastRightBranch() {
        if (root.right == null) {
            return 0.0;
        }
        return summaryCost(root.right);
    }

    public double summaryCoastLeftBranch() {
        if (root.left == null) {
            return 0.0;
        }
        return summaryCost(root.left);
    }

    @Getter
    @Setter
    private static class Node<T extends Product> {
        private T item;
        private Node<T> left;
        private Node<T> right;

        Node(T item) {
            this.item = item;
            left = null;
            right = null;
        }

        @Override
        public String toString() {
            return "Price - " + String.format("%.2f", item.getPrice())
                    + ", " + item.getTitle()
                    + ", Count - " + item.getCount();
        }
    }
}
