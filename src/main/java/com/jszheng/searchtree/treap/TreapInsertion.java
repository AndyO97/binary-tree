package com.jszheng.searchtree.treap;

import com.jszheng.base.BinaryTree;
import com.jszheng.node.BinTreeNode;
import com.jszheng.searchtree.BstInsertion;

/*
 * O(Log n)
 */
class TreapInsertion<E extends Comparable<? super E>> extends BstInsertion<E> {

    private boolean maxHeap = true;
    private boolean specifiedPriority = false;
    private int priority;

    TreapInsertion() {
    }

    @Override
    protected void fixAfterInsertion(BinTreeNode<E> newNode) {
        Treap<E> treap = getBt();
        if (specifiedPriority)
            treap.putPriority(newNode, priority);
        else
            treap.putRandomPriority(newNode);

        treap.upHeap(newNode, maxHeap);
    }

    @Override
    protected Treap<E> getBt() {
        BinaryTree<E> bt = super.getBt();
        return (Treap<E>) bt;
    }

    void setMaxHeap(boolean maxHeap) {
        this.maxHeap = maxHeap;
    }

    void specifiedPriority(int priority) {
        this.specifiedPriority = true;
        this.priority = priority;
    }

    void useRandomPriority() {
        this.specifiedPriority = false;
    }
}
