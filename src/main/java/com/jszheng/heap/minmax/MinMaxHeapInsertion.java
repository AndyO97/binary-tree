package com.jszheng.heap.minmax;

import com.jszheng.base.BinaryTree;
import com.jszheng.heap.HeapInsertion;
import com.jszheng.node.TreeNode;

public class MinMaxHeapInsertion<E extends Comparable<? super E>> extends HeapInsertion<E> {

    @Override
    protected MinMaxHeap<E> getBt() {
        BinaryTree<E> bt = super.getBt();
        return (MinMaxHeap<E>) bt;
    }

    @Override
    protected void fixAfterInsertion(TreeNode<E> newNode) {
        TreeNode<E> parent = newNode.getParent();
        if (parent == null) return;

        MinMaxHeap<E> heap = getBt();
        E data = newNode.getData();
        E parentData = parent.getData();

        int compareWithParent = data.compareTo(parentData);
        boolean isParentMinLevel = heap.isMinLevel(parent);
        boolean adjustMax = isParentMinLevel;
        boolean swapCondition = isParentMinLevel ? compareWithParent < 0 : compareWithParent > 0;

        TreeNode<E> adjustStart = newNode;

        if (swapCondition) {
            // Swap data between newNode and its parent
            parent.setData(data);
            newNode.setData(parentData);

            adjustStart = parent;
            adjustMax = !isParentMinLevel;
        }

        heap.upHeap(adjustStart, adjustMax);
    }
}
