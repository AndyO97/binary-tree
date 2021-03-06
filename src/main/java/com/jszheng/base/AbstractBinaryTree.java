package com.jszheng.base;

import com.jszheng.Env;
import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.node.BinTreeNode;
import com.jszheng.search.SearchAlgo;
import com.jszheng.search.SearchResult;

import static com.jszheng.base.SkewedState.*;

abstract class AbstractBinaryTree<E> implements BinaryTree<E> {

    protected InsertionAlgo<E> insertionAlgo;
    protected SearchAlgo<E> searchAlgo;

    @Override
    public int height() {
        return height(getRoot());
    }

    @Override
    public int height(BinTreeNode<E> node) {
        if (node == null)
            return 0;

        int hL = height(node.getLeftChild());
        int hR = height(node.getRightChild());
        return Math.max(hL, hR) + 1;
    }

    @Override
    public int leavesCount() {
        return leavesCount(getRoot());
    }

    @Override
    public int leavesCount(BinTreeNode<E> node) {
        if (node == null)
            return 0;

        int nL = leavesCount(node.getLeftChild());
        int nR = leavesCount(node.getRightChild());
        int sum = nL + nR;

        return sum > 0 ? sum : 1;
    }

    @Override
    public int maxCount() {
        int maxLevel = height();
        return BinaryTreeLemma.maxCount(maxLevel);
    }

    @Override
    public BinTreeNode<E> search(E data) {
        if (data == null)
            return null;

        SearchAlgo<E> algo = createSearchAlgo();
        SearchResult<E> result = algo.search(this, data);

        return result != null ? result.getNode() : null;
    }

    @Override
    public SkewedState skewedState() {
        SkewedState leftState = checkTreeSkewed(true);
        if (leftState != null)
            return leftState;
        else
            return checkTreeSkewed(false);
    }

    @Override
    public void swap() {
        swap(getRoot());
    }

    @Override
    public void swap(BinTreeNode<E> node) {
        if (node != null) {
            swap(node.getLeftChild());
            swap(node.getRightChild());

            BinTreeNode<E> tmpLeft = node.getLeftChild();
            BinTreeNode<E> tmpRight = node.getRightChild();
            if (tmpLeft == null && tmpRight == null) return;

            node.setLeftChild(tmpRight);
            node.setRightChild(tmpLeft);
            node.isRoot();
        }
    }

    @Override
    public void insert(E... data) {
        if (data.length < 1) return;

        // Default Algo
        InsertionAlgo<E> algo = createInsertionAlgo();
        insertDataArr(algo, data);
    }

    @Override
    public int size(BinTreeNode<E> node) {
        if (node == null)
            return 0;

        int nL = size(node.getLeftChild());
        int nR = size(node.getRightChild());
        return nL + nR + 1;
    }

    protected abstract InsertionAlgo<E> createInsertionAlgo();

    protected abstract SearchAlgo<E> createSearchAlgo();

    protected void insertDataArr(InsertionAlgo<E> algo, E[] data) {
        for (E d : data) {
            if (Env.debug) System.out.println("[insert] data: " + d);
            algo.insert(this, d);
            if (Env.debug) System.out.println();
        }
    }

    BinTreeNode<E> copyNodes(BinTreeNode<E> node, boolean deep) {
        if (node == null)
            return null;

        BinTreeNode<E> copyNode;

        if (!deep)
            copyNode = node;
        else {
            copyNode = node.newNode();

            copyNode.setData(node.getData());
            copyNode.setLeftChild(copyNodes(node.getLeftChild(), true));
            copyNode.setRightChild(copyNodes(node.getRightChild(), true));
        }
        return copyNode;
    }

    private SkewedState checkTreeSkewed(boolean checkLeft) {
        BinTreeNode<E> lastNode = null;
        BinTreeNode<E> root = getRoot();
        BinTreeNode<E> currentNode = root;

        // Check Root.
        if (getRoot() == null)
            return EMPTY;

        // Check Left or Right.
        while (currentNode != null) {

            boolean hasLChild = currentNode.hasLeftChild();
            boolean hasRChild = currentNode.hasRightChild();

            if (hasLChild && hasRChild)
                return NORMAL; // 非歪斜

            if (checkLeft && hasRChild
                    && currentNode != root) // Must check min.
                return NORMAL;

            if (!checkLeft && hasLChild
                    && currentNode != root) // Must check min.
                return NORMAL;

            lastNode = currentNode;
            currentNode = checkLeft ? currentNode.getLeftChild()
                    : currentNode.getRightChild();
        }

        if (lastNode == root)
            return null;
        else
            return checkLeft ? LEFT_SKEWED : RIGHT_SKEWED;
    }
}
