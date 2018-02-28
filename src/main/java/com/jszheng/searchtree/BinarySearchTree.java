package com.jszheng.searchtree;

import com.jszheng.base.BinaryTree;
import com.jszheng.base.BtDecorator;
import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.search.SearchResult;

public class BinarySearchTree<E extends Comparable<? super E>> extends BtDecorator<E> {

    protected InsertionAlgo<E> insertionAlgo;
    protected BstDeletion<E> deletionAlgo;
    protected BstSearch<E> searchAlgo;

    public BinarySearchTree(BinaryTree<E> component) {
        super(component);
    }

    @Override
    public BinaryTree<E> copy(boolean deep) {
        return new BinarySearchTree<>(component.copy(deep));
    }

    @Override
    public BinarySearchTree<E> newTree() {
        return new BinarySearchTree<>(component.newTree());
    }

    // The subtree of the "binary search tree" is still BST.
    public void delete(E data) {
        delete(data, true);
    }

    public void delete(E data, boolean replaceByLMax) {
        BstDeletion<E> algo = createDeletionAlgo();
        algo.setReplaceByLMax(replaceByLMax);
        algo.delete(this, data);
    }

    public SearchResult<E> searchMax() {
        return createSearchAlgo().searchMax(this);
    }

    public SearchResult<E> searchMin() {
        return createSearchAlgo().searchMin(this);
    }

    protected BstDeletion<E> createDeletionAlgo() {
        if (deletionAlgo == null)
            deletionAlgo = new BstDeletion<>();
        return deletionAlgo;
    }

    @Override
    protected InsertionAlgo<E> createInsertionAlgo() {
        if (insertionAlgo == null)
            insertionAlgo = new BstInsertion<>();
        return insertionAlgo;
    }

    @Override
    protected BstSearch<E> createSearchAlgo() {
        if (searchAlgo == null)
            searchAlgo = new BstSearch<>();
        return searchAlgo;
    }
}
