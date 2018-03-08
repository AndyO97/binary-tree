package com.jszheng.base;

import com.jszheng.node.TreeNode;
import com.jszheng.printer.HorizontalTreePrinter;
import com.jszheng.printer.TreePrinter;

public interface Tree<E, Node extends TreeNode<E>> {

    // util generic method, no need follow generic type of class<E, Node>
    default <El extends Comparable<? super El>, Nd extends TreeNode<El>> Nd compareNode(Nd t1, Nd t2, boolean findGreater) {
        El t1Data = t1 != null ? t1.getData() : null;
        El t2Data = t2 != null ? t2.getData() : null;

        if (t1Data == null && t2Data == null) return null;
        else if (t1Data == null) return t2;
        else if (t2Data == null) return t1;

        int compareResult = t1Data.compareTo(t2Data);
        boolean t1GreaterThanT2 = compareResult > 0;

        if (findGreater)
            return t1GreaterThanT2 ? t1 : t2;
        else
            return t1GreaterThanT2 ? t2 : t1;
    }

    default String getNodeString(Node node) {
        Object data = node != null ? node.getData() : null;
        return data != null ? data.toString() :
                (getRoot() == node ? "⊙" : " "); // Keep one space to mock null.
    }

    @SuppressWarnings("unchecked")
    void insert(E... data);

    Node newNode();

    default void print() {
        TreePrinter printer = new HorizontalTreePrinter();
        printer.print(this);
    }

    int size(Node node);

    default int size() {
        return size(getRoot());
    }

    Node getRoot();

    void setRoot(Node node);

    boolean isEmpty();
}
