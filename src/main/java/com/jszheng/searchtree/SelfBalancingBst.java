package com.jszheng.searchtree;

import com.jszheng.node.BinTreeNode;
import com.jszheng.searchtree.rotation.*;

public abstract class SelfBalancingBst<E extends Comparable<? super E>> extends AbstractBst<E> {

    private RotationState rRRotate;
    private RotationState lLRotate;
    private RotationState rLRotate;
    private RotationState lRRotate;

    public RotationState createRotationState(BinTreeNode<E> pivot, BinTreeNode<E> child) {
        RotationState state;

        boolean isPivotLeft = pivot.isLeftChild();
        boolean isChildLeft = child.isLeftChild();

        if (!isPivotLeft && !isChildLeft)
            state = createRrRotate();
        else if (isPivotLeft && isChildLeft)
            state = createLlRotate();
        else if (!isPivotLeft)
            state = createRlRotate();
        else
            state = createLrRotate();

        return state;
    }

    public RotationState createRrRotate() {
        if (rRRotate == null)
            rRRotate = new RrRotation();
        return rRRotate;
    }

    public RotationState createLlRotate() {
        if (lLRotate == null)
            lLRotate = new LlRotation();
        return lLRotate;
    }

    protected RotationState createRlRotate() {
        if (rLRotate == null)
            rLRotate = new RlRotation();
        return rLRotate;
    }

    protected RotationState createLrRotate() {
        if (lRRotate == null)
            lRRotate = new LrRotation();
        return lRRotate;
    }
}
