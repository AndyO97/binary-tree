package com.jszheng.searchtree.splay;

import com.jszheng.base.BinaryTree;
import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.node.BinTreeNode;
import com.jszheng.searchtree.BstSearch;
import com.jszheng.searchtree.SelfBalancingBst;
import com.jszheng.searchtree.rotation.LlRotation;
import com.jszheng.searchtree.rotation.RotationState;
import com.jszheng.searchtree.rotation.RrRotation;

public class SplayTree<E extends Comparable<? super E>> extends SelfBalancingBst<E> {

    public SplayTree(BinaryTree<E> component) {
        super(component);
    }

    @Override
    public BinaryTree<E> copy(boolean deep) {
        return new SplayTree<>(component.copy(deep));
    }

    @Override
    public SplayTree<E> newTree() {
        return new SplayTree<>(component.newTree());
    }

    @Override
    protected InsertionAlgo<E> createInsertionAlgo() {
        if (insertionAlgo == null)
            insertionAlgo = new SplayInsertion<>();
        return insertionAlgo;
    }

    @Override
    protected BstSearch<E> createSearchAlgo() {
        if (searchAlgo == null)
            searchAlgo = new SplaySearch<>();
        return searchAlgo;
    }

    public void fixAfterOperation(BinTreeNode<E> targetNode) {
        if (targetNode == null) return;

        RotationState state;
        while (!targetNode.isRoot()) {
            BinTreeNode<E> parent = targetNode.getParent();
            if (parent == null) return;
            BinTreeNode<E> grandParent = parent.getParent();
            BinTreeNode<E> rotateTarget;

            if (grandParent == null) { // targetNode is child of min
                boolean isTargetNodeLChild = targetNode.isLeftChild();
                rotateTarget = parent;
                state = isTargetNodeLChild ? new LlRotation() : new RrRotation();
            } else {
                rotateTarget = grandParent;
                state = getRotationState(parent, targetNode);
                if (state instanceof RrRotation || state instanceof LlRotation) {
                    state.rotate(this, rotateTarget);
                    rotateTarget = parent;
                }
            }

            state.rotate(this, rotateTarget);
        }
    }
}
