package com.jszheng.heap.deap;

import com.jszheng.base.BinaryTree;
import com.jszheng.base.BinaryTreeLemma;
import com.jszheng.base.complete.CompleteBinaryTree;
import com.jszheng.heap.AbsBinDoubleEndedHeap;
import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.node.BinTreeNode;

// Source -- https://pdfs.semanticscholar.org/e1e5/eafb44abc04834caf752b147256335a73bb3.pdf
public class Deap<E extends Comparable<? super E>> extends AbsBinDoubleEndedHeap<E> implements CompleteBinaryTree<E> {

    public Deap(BinaryTree<E> component) {
        super(component);
    }

    @Override
    public Deap<E> copy(boolean deep) {
        return new Deap<>(component.copy(deep));
    }

    @Override
    public Deap<E> newTree() {
        return new Deap<>(component.newTree());
    }

    @Override
    public E deleteMax() {
        return deleteExtrema(true);
    }

    @Override
    public E searchMax() {
        BinTreeNode<E> maxNode = searchExtremaNode(true);
        return maxNode != null ? maxNode.getData() : null;
    }

    @Override
    public E deleteMin() {
        return deleteExtrema(false);
    }

    @Override
    public E searchMin() {
        BinTreeNode<E> minNode = searchExtremaNode(false);
        return minNode != null ? minNode.getData() : null;
    }

    @Override
    protected InsertionAlgo<E> createInsertionAlgo() {
        if (insertionAlgo == null)
            insertionAlgo = new DeapInsertion<>();
        return insertionAlgo;
    }

    /*
     * call the element to be insert X
     *
     * if (the first free position is in the minheap){
     *   compare X with the corresponding element in the maxheap (indexed I)
     *
     *   if (X is the larger){
     *     move the element in position I to the freed position
     *     perform a binary search in the path from I to the min of the heap we are working on [3]
     *     move all elements smaller than X one level down in the maxheap
     *     store X in the freed position
     *   } else {
     *     perform the binary search and data movements in the corresponding way in th minheap
     *   }
     * } else {
     *   proceed in a corresponding way as above
     * }
     */
    void insertionFixUp(BinTreeNode<E> newNode) {
        // 取得 Deap 對應節點
        BinTreeNode<E> correspondingNode = getCorrespondingNode(newNode);
        if (correspondingNode == null || correspondingNode == getRoot()) return;

        E data = newNode.getData();
        E correspondingNodeData = correspondingNode.getData();
        // 新節點 是否位於左子樹 (min-heap)
        boolean isNewNodeInLTree = BinaryTreeLemma.isDescendantIndex(1, newNode.getIndex());

        // 將 新節點鍵值 與 對應節點鍵值 作比較
        int compareResult = data.compareTo(correspondingNodeData);
        boolean swapCondition = isNewNodeInLTree ? compareResult > 0 : compareResult < 0;

        if (swapCondition) {
            newNode.setData(correspondingNodeData);
            correspondingNode.setData(data);

            // 若新節點原本位於 min-heap，則進行 max-heap 之調整；反之
            // (因資料已交換)
            upHeap(correspondingNode, isNewNodeInLTree);
        } else
            // 若新節點原本位於 min-heap，則進行 min-heap 之調整；反之
            upHeap(newNode, !isNewNodeInLTree);
    }

    private E deleteExtrema(boolean max) {
        BinTreeNode<E> target = searchExtremaNode(max);
        if (target == null) return null;
        E extrema = target.getData();

        BinTreeNode<E> lastNode = getLastNode();
        E lastNodeData = lastNode.getData();
        lastNode.deleteParent();

        // If the target is lastNode, simply delete it.
        if (lastNode == target)
            return extrema;

        while (target.degree() != 0) {
            BinTreeNode<E> lChild = target.getLeftChild();
            BinTreeNode<E> rChild = target.getRightChild();
            BinTreeNode<E> extremaDescendant = compareNode(lChild, rChild, max);
            E extremaDescendantData = extremaDescendant.getData();

            target.setData(extremaDescendantData);
            target = extremaDescendant;
        }

        target.setData(lastNodeData);
        insertionFixUp(target);

        return extrema;
    }

    private BinTreeNode<E> getCorrespondingNode(BinTreeNode<E> node) {
        BinTreeNode<E> result = null;

        int currentIndex = node.getIndex();
        boolean isNodeInLTree = BinaryTreeLemma.isDescendantIndex(1, currentIndex);
        int lastLevel = node.getLevel() - 1;
        int lastLevelMaxNodeCount = BinaryTreeLemma.getMaxCountByLevel(lastLevel);

        int correspondingIndex = isNodeInLTree ? currentIndex + lastLevelMaxNodeCount
                : currentIndex - lastLevelMaxNodeCount;

        while (result == null && correspondingIndex > -1) {
            result = getNodeByIndex(correspondingIndex);

            if (result == null)
                correspondingIndex = BinaryTreeLemma.parentIndex(correspondingIndex);
        }

        return result;
    }
}
