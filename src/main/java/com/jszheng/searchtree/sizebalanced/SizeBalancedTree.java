package com.jszheng.searchtree.sizebalanced;

import com.jszheng.Env;
import com.jszheng.base.BinaryTree;
import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.node.TreeNode;
import com.jszheng.searchtree.SelfBalancingBst;
import com.jszheng.searchtree.rotation.LlRotation;
import com.jszheng.searchtree.rotation.RrRotation;

public class SizeBalancedTree<E extends Comparable<? super E>> extends SelfBalancingBst<E> {

    public SizeBalancedTree(BinaryTree<E> component) {
        super(component);
    }

    @Override
    public BinaryTree<E> copy(boolean deep) {
        return new SizeBalancedTree<>(component.copy(deep));
    }

    @Override
    public SizeBalancedTree<E> newTree() {
        return new SizeBalancedTree<>(component.newTree());
    }

    @Override
    protected InsertionAlgo<E> createInsertionAlgo() {
        if (insertionAlgo == null)
            insertionAlgo = new SbtInsertion<>();
        return insertionAlgo;
    }

    @Override
    public String getNodeString(TreeNode<E> node) {
        Object data = node != null ? node.getData() : null;
        return data != null ? node.getData() + "(" + count(node) + ")" :
                (getRoot() == node ? "⊙" : " ");
    }

    public TreeNode<E> selectKth(int k) {
        TreeNode<E> target = getRoot();

        while (target != null) {
            TreeNode<E> lChild = target.getLeftChild();
            int lChildSize = sizeOf(lChild);

            int rankOfTarget = lChildSize + 1;

            if (k == rankOfTarget) return target;
            else if (k < rankOfTarget) target = target.getLeftChild();
            else {
                k = k - rankOfTarget;
                target = target.getRightChild();
            }
        }

        return null;
    }

    // witchcraft (Omit the maintenance of size)
    private int sizeOf(TreeNode<E> node) {
        return count(node);
    }

    void maintain(TreeNode<E> node, boolean checkLGrandSon) {
        if (node == null) return;
        TreeNode<E> lChild = node.getLeftChild();
        TreeNode<E> rChild = node.getRightChild();
        int lChildSize = sizeOf(lChild);
        int rChildSize = sizeOf(rChild);

        boolean basicCondition = (checkLGrandSon && lChild != null) || (!checkLGrandSon && rChild != null);

        if (!basicCondition) return;

        if (checkLGrandSon) {
            if (sizeOf(lChild.getLeftChild()) > rChildSize) {
                // case 1: s[left[left[t]]>s[right[t]]
                if (Env.debug) System.out.println("case 1 -- target: " + node.getData());
                new LlRotation().rotate(this, node);
            } else if (sizeOf(lChild.getRightChild()) > rChildSize) {
                // case 2: s[right[left[t]]>s[right[t]]
                if (Env.debug) System.out.println("case 2 -- target: " + node.getData());
                new RrRotation().rotate(this, lChild);
                new LlRotation().rotate(this, node);
            } else
                return;
        } else {
            if (sizeOf(rChild.getRightChild()) > lChildSize) {
                // case 3: s[right[right[t]]>s[left[t]]
                if (Env.debug) System.out.println("case 3 -- target: " + node.getData());
                new RrRotation().rotate(this, node);
            } else if (sizeOf(rChild.getLeftChild()) > lChildSize) {
                // case 4: s[left[right[t]]>s[left[t]]
                if (Env.debug) System.out.println("case 4 -- target: " + node.getData());
                new LlRotation().rotate(this, rChild);
                new RrRotation().rotate(this, node);
            } else
                return;
        }

        maintain(node.getLeftChild(), true);   //maintain the left subtree
        maintain(node.getRightChild(), false);   //maintain the right subtree
        maintain(node, true);      //maintain the whole tree
        maintain(node, false);       //maintain the whole tree
    }
}
