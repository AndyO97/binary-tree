package com.jszheng.searchtree.redblack;

import com.jszheng.insertion.InsertionAlgo;
import com.jszheng.node.BinTreeNode;
import com.jszheng.searchtree.BstDeletion;
import com.jszheng.searchtree.SelfBalancingBst;
import com.jszheng.searchtree.rotation.RotateListener;

import static com.jszheng.Env.osName;

/**
 * 1. Root always BLACK.
 * 2. Color of Node is either BLACK or RED.
 * 3. Null is BLACK.
 * 4. Child of RED Node are always BLACK.
 * 5. Any path from root to leaf has same count of BLACK Node.
 * 6. Longest path always <= 2 * shortest path
 * <p>
 * Created by zhengzhongsheng on 2017/10/24.
 */
public class RedBlackTree<E extends Comparable<? super E>> extends SelfBalancingBst<E> implements RotateListener<E> {

    public static final boolean RED = false;
    public static final boolean BLACK = true;

    // for dev
    public int rotateLeftCount = 0;
    public int rotateRightCount = 0;
    public int colorChangeCount = 0;

    @Override
    public String getNodeString(BinTreeNode<E> node) {
        E data = node != null ? node.getData() : null;
        if (osName.contains("Windows"))
            return data != null ? data.toString() +
                    "(" + (colorOf((RedBlackTreeNode<E>) node) == BLACK ? "黑" : "紅") + ")" : " ";
        else
            return data != null ? data.toString() +
                    "(" + (colorOf((RedBlackTreeNode<E>) node) == BLACK ? "⚫" : "🔴") + ")" : " "; // Keep one space to mock null.
    }

    // use 'public access modifier' to facilitate testing
    public boolean colorOf(RedBlackTreeNode<E> node) {
        // BLACK of default value is prepared for External Node.
        return node == null ? BLACK : node.color;
    }

    public final void insertByTopDown(E datum) {
        InsertionAlgo<E> algo = new RedBlackTopDownInsertion<>();
        algo.insert(this, datum);
    }

    public final void insertByTopDown(E[] data) {
        InsertionAlgo<E> algo = new RedBlackTopDownInsertion<>();
        insertDataWithSpecifyAlgo(data, algo);
    }

    @Override
    public RedBlackTree<E> newTree() {
        return new RedBlackTree<>();
    }

    @Override
    public RedBlackTreeNode<E> getRoot() {
        return (RedBlackTreeNode<E>) super.getRoot();
    }

    @Override
    public void setRoot(E data) {
        super.setRoot(data);
        setColor(getRoot(), BLACK);
    }

    @Override
    public RedBlackTreeNode<E> newNode() {
        return new RedBlackTreeNode<>();
    }

    void setColor(RedBlackTreeNode<E> node, boolean color) {
        if (node != null)
            node.color = color;
    }

    @Override
    public void onRotateLeft(BinTreeNode<E> parent, BinTreeNode<E> pivot, BinTreeNode<E> lChild) {
        rotateLeftCount++;
    }

    @Override
    public void onRotateRight(BinTreeNode<E> parent, BinTreeNode<E> pivot, BinTreeNode<E> rChild) {
        rotateRightCount++;
    }

    @Override
    protected InsertionAlgo<E> createInsertionAlgo() {
        if (insertionAlgo == null)
            insertionAlgo = new RedBlackBottomUpInsertion<>();
        return insertionAlgo;
    }

    @Override
    protected BstDeletion<E> createDeletionAlgo() {
        if (deletionAlgo == null)
            deletionAlgo = new RedBlackDeletion<>();
        return deletionAlgo;
    }
}
