package com.jszheng.heap.binaryheap;

import com.jszheng.base.BinaryTree;
import com.jszheng.heap.HeapInsertion;
import com.jszheng.node.TreeNode;

/*
 * O(log n)
 */
class MaxHeapInsertion<E extends Comparable<? super E>> extends HeapInsertion<E> {

    @Override
    protected MaxBinaryHeap<E> getBt() {
        BinaryTree<E> bt = super.getBt();
        return (MaxBinaryHeap<E>) bt;
    }

    @Override
    protected void fixAfterInsertion(TreeNode<E> newNode) {
        getBt().upHeap(newNode, true);
    }
}
